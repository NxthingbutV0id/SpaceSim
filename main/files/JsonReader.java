/* Class: JsonReader
 * Author: Christian Torres
 * Date: 4/6/2023
 *
 * Purpose: reads from a .json file
 *
 * Attributes: TBD
 *
 * Methods: TBD
 */
package main.files;

import javafx.scene.paint.Paint;
import main.customUtils.Vec2;
import main.simulation.bodies.CelestialBody;
import main.simulation.bodies.Star;
import main.simulation.bodies.Terrestrial;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class JsonReader {
    private Decoder decoder = new Decoder();
    private JSONObject rawData;
    private JSONObject settings;
    private LinkedList<CelestialBody> bodies = new LinkedList<>();

    public LinkedList<CelestialBody> loadFile(String path) {
        try {
            parseJSON(new FileReader(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bodies;
    }

    public void parseJSON(FileReader reader) throws IOException, ParseException, InvalidFileFormatException {
        JSONParser parser = new JSONParser();
        var jsonObj = parser.parse(reader);
        rawData = (JSONObject) jsonObj;
        getSettings();
        loadBodies();
    }

    public void loadBodies() throws InvalidFileFormatException {
        JSONArray array = (JSONArray) rawData.get("system");

        var iter = array.iterator();

        while (iter.hasNext()) {
            JSONObject body = (JSONObject) iter.next();
            setupBodies(body);
        }
    }

    public void setupBodies(JSONObject body) throws InvalidFileFormatException {
        var bodyType = (String) body.get("type");
        String name = (String) body.get("name");
        double mass = decoder.getMass(body.get("mass").toString());
        double radius = decoder.getRadius(body.get("radius").toString());
        Vec2 position = arrayToVec((JSONArray) body.get("position"));
        Vec2 velocity = arrayToVec((JSONArray) body.get("velocity"));

        switch (bodyType) {
            case "Star" -> {
                Star temp = new Star(name, mass, radius, position, velocity);
                temp.setPlanetColor((Paint) body.get("color"));
                if (body.get("parent") != null) {
                    decoder.setRelative((String) body.get("parent"),bodies, temp);
                }
                bodies.add(temp);
            }
            case "Terrestrial" -> {
                Terrestrial temp = new Terrestrial(name, mass, radius, position, velocity);
                temp.setPlanetColor(decoder.getColor(body.get("color").toString()));
                temp.setAlbedo((double) body.get("albedo"));
                temp.setHasAtmosphere((boolean) body.get("atmosphere present?"));
                temp.setGreenhouseFactor((double) body.get("greenhouse effect"));
                if (body.get("parent") != null) {
                    decoder.setRelative((String) body.get("parent"),bodies, temp);
                }
                bodies.add(temp);
            }
            case "Gas Giant" -> {}
            default -> throw new InvalidFileFormatException("Error, .json file not in proper format");
        }
    }

    public Vec2 arrayToVec(JSONArray arr) {
        double x, y;
        try {
            x = Double.parseDouble(arr.get(0).toString());
        } catch (Exception e) {
            x = decoder.getDistance(arr.get(0).toString());
        }
        try {
            y = Double.parseDouble(arr.get(1).toString());
        } catch (Exception e) {
            y = decoder.getDistance(arr.get(1).toString());
        }
        return new Vec2(x, y);
    }

    private void getSettings() {
        settings = (JSONObject) rawData.get("settings");
    }

    public double getScale() {
        return Double.parseDouble(settings.get("zoom").toString());
    }

    public double getTimeScale() {
        return Double.parseDouble(settings.get("time scale").toString());
    }
}
