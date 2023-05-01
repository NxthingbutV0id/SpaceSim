/* Class: JsonReader
 * Author: Christian Torres
 * Date: 2023/4/6
 *
 * Purpose: The JsonReader class is responsible for reading and parsing JSON files containing celestial body data.
 *              The class loads and creates instances of celestial bodies according to the data in the JSON file.
 *
 * Attributes:
 * -decoder: Decoder
 * -rawData: JSONObject
 * -settings: JSONObject
 * -bodies: LinkedList<CelestialBody>
 * -logger: Logger
 *
 * Methods:
 * +loadFile(String): LinkedList<CelestialBody>
 * +parseJSON(FileReader): void
 * +loadBodies(): void
 * +setupBodies(JSONObject): void
 * +createStar(JSONObject, String, double, double, Vec2, Vec2): void
 * +createTerrestrial(JSONObject, String, double, double, Vec2, Vec2): void
 * +createGasGiant(JSONObject, String, double, double, Vec2, Vec2): void
 * +setupRing(JSONObject, GasGiant): void
 * +arrayToVec(JSONArray): Vec2
 * -getSettings(): void
 * +getScale(): double
 * +getTimeScale(): double
 */
package main.files;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import main.utils.Vec2;
import main.simulation.bodies.CelestialBody;
import main.simulation.bodies.GasGiant;
import main.simulation.bodies.Star;
import main.simulation.bodies.Terrestrial;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class JsonReader {
    private Decoder decoder = new Decoder();
    private JSONObject rawData;
    private JSONObject settings;
    private LinkedList<CelestialBody> bodies = new LinkedList<>();
    private Logger logger = LoggerFactory.getLogger(JsonReader.class);

    public LinkedList<CelestialBody> loadFile(String path) {
        try {
            parseJSON(new FileReader(path));
        } catch (Exception ignore) {
            logger.error("Error loading file");
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

        for (var o : array) {
            JSONObject body = (JSONObject) o;
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
            case "Star" -> createStar(body, name, mass, radius, position, velocity);
            case "Terrestrial" -> createTerrestrial(body, name, mass, radius, position, velocity);
            case "Gas Giant" -> createGasGiant(body, name, mass, radius, position, velocity);
            default -> throw new InvalidFileFormatException("Error, Type not found");
        }

        logger.info("Body \"{}\" successfully loaded!", name);
    }

    public void createStar(JSONObject body, String name, double mass, double radius, Vec2 position, Vec2 velocity) {
        Star temp = new Star(name, mass, radius, position, velocity);
        temp.setPlanetColor(Color.valueOf((String) body.get("color")));
        if (body.get("parent") != null) {
            decoder.setRelative((String) body.get("parent"),bodies, temp);
        }
        bodies.add(temp);
    }

    public void createTerrestrial(JSONObject body, String name, double mass,
                                  double radius, Vec2 position, Vec2 velocity) {
        Terrestrial temp = new Terrestrial(name, mass, radius, position, velocity);
        temp.setPlanetColor(decoder.getColor(body.get("color").toString()));
        if (body.get("albedo") instanceof Number) {
            temp.setAlbedo(((Number) body.get("albedo")).doubleValue());
        }
        temp.setHasAtmosphere((boolean) body.get("atmosphere present?"));
        if (body.get("greenhouse effect") instanceof Number) {
            temp.setGreenhouseFactor(((Number) body.get("greenhouse effect")).doubleValue());
        }
        if (body.get("parent") != null) {
            decoder.setRelative((String) body.get("parent"),bodies, temp);
        }
        bodies.add(temp);
    }

    public void createGasGiant(JSONObject body, String name, double mass, double radius, Vec2 position, Vec2 velocity) {
        GasGiant temp = new GasGiant(name, mass, radius, position, velocity);
        temp.setPlanetColor(decoder.getColor(body.get("color").toString()));
        temp.setAlbedo((double) body.get("albedo"));
        setupRing(body, temp);

        if (body.get("parent") != null) {
            decoder.setRelative((String) body.get("parent"),bodies, temp);
        }
        bodies.add(temp);
    }

    public void setupRing(JSONObject body, GasGiant gg) {
        JSONObject ring = (JSONObject) body.get("ring system");
        if (ring != null) {
            double ir = (double) ring.get("inner radius");
            double or = (double) ring.get("outer radius");
            Paint col = decoder.getColor(ring.get("color").toString());
            double op = Double.parseDouble((String) ring.get("opacity"));
            gg.setRings(ir, or, col, op);
        } else {
            gg.setRings(0, 0, Color.BLACK, 0);
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
        return new Vec2(x, -y);
    }

    private void getSettings() {settings = (JSONObject) rawData.get("settings");}
    public double getScale() {return Double.parseDouble(settings.get("zoom").toString());}
    public double getTimeScale() {return 1/Double.parseDouble(settings.get("time scale").toString());}
}
