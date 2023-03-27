/* Class: DataManager
 * Author: Christian Torres
 * Created: 2023/3/25
 * Modified:
 *
 * Purpose: To handle File IO by reading from the file "system.csv"
 *
 * Attributes: -reader: BufferedReader
 *
 * Methods: +DataManager(): this
 *          +getData(): LinkedList<CelestialBody>
 *          -rawData(): LinkedList<String[]>
 *          -createBody(String[]): CelestialBody
 *          -getMass(String): double
 *          -getRadius(String): double
 *          -getDistance(String): double
 *          -getColor(String): Paint
 *          -extractDouble(String): double
 *          -getValue(String): double
 *          -setRelative(String, String, LinkedList<CelestialBody>, CelestialBody): void
 */
package main.customUtils;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import main.constants.Constants;
import main.simulation.CelestialBody;
import java.io.*;
import java.util.LinkedList;

public class DataManager {
    private final BufferedReader reader;

    public DataManager() throws FileNotFoundException {
        String file = "main/files/system.csv";
        reader = new BufferedReader(new FileReader(file));
    }

    public LinkedList<CelestialBody> getData() {
        LinkedList<CelestialBody> bodies = new LinkedList<>();

        try {
            LinkedList<String[]> data = rawData();
            /*
            0 = Name
            1 = Mass
            2 = Radius
            3, 4 = Position
            5, 6 = Velocity
            7 = HexColor
            8 = isRelative (boolean)
            9 = name of relative body (x if 8 is false)
             */
            for (String[] row : data) {
                CelestialBody body = createBody(row);
                bodies.add(body);
                setRelative(row[8], row[9], bodies, body);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bodies;
    }

    private LinkedList<String[]> rawData() throws IOException {
        LinkedList<String[]> data = new LinkedList<>();

        String line;
        while ((line = reader.readLine()) != null) {
            data.add(line.split(","));
        }
        data.removeFirst();
        return data;
    }

    private CelestialBody createBody(String[] data) {
        String name = data[0];
        double mass = getMass(data[1]);
        double radius = getRadius(data[2]);
        Vec2 position = new Vec2(getDistance(data[3]), getDistance(data[4]));
        Vec2 velocity = new Vec2(getValue(data[5]), getValue(data[6]));
        Paint planetColor = getColor(data[7]);

        return new CelestialBody(name, mass, radius, position, velocity, planetColor);
    }

    private double getMass(String str) {
        double massValue = extractDouble(str);

        if (str.contains("Ms")) {
            massValue *= Constants.Mass.SOL;
        } else if (str.contains("Me")) {
            massValue *= Constants.Mass.EARTH;
        } else if (str.contains("Mj")) {
            massValue *= Constants.Mass.JUPITER;
        } else if (str.contains("Mm")) {
            massValue *= Constants.Mass.MOON;
        }

        return massValue;
    }

    private double getRadius(String str) {
        double radiusValue = extractDouble(str);

        if (str.contains("Rs")) {
            radiusValue *= Constants.Radius.SOL;
        } else if (str.contains("Re")) {
            radiusValue *= Constants.Radius.EARTH;
        } else if (str.contains("Rj")) {
            radiusValue *= Constants.Radius.JUPITER;
        } else if (str.contains("Rm")) {
            radiusValue *= Constants.Radius.MOON;
        }

        return radiusValue;
    }

    private double getDistance(String str) {
        double dist = extractDouble(str);
        if (str.contains("Au")) {
            dist *= Constants.Distance.AU;
        }
        return dist;
    }

    private Paint getColor(String str) {
        if (str.charAt(0) == '#') {
            return Paint.valueOf(str);
        } else {
            System.out.println("Error at 'getColor()' not proper format, defaulting to white");
            return Color.WHITE;
        }
    }

    private double extractDouble(String str) {
        try {
            return Double.parseDouble(str.replaceAll("[^\\d.Ee+-]", ""));
        } catch (Exception e) {
            return Double.parseDouble(str.replaceAll("[^\\d.]", ""));
        }
    }

    private double getValue(String str) {
        return extractDouble(str);
    }

    private void setRelative(String str1, String str2, LinkedList<CelestialBody> bodies, CelestialBody body) {
        if (Boolean.parseBoolean(str1)) {
            for (CelestialBody otherBody : bodies) {
                if (otherBody.getName().equals(str2)) {
                    body.getPosition().incrementBy(otherBody.getPosition());
                    body.getVelocity().incrementBy(otherBody.getVelocity());
                    return;
                }
            }
        }
    }
}
