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
        String file = "\\main\\files\\system.csv";
        reader = new BufferedReader(new FileReader(file));
    }

    public LinkedList<CelestialBody> getData() {
        LinkedList<CelestialBody> bodies = new LinkedList<>();

        try {
            LinkedList<String[]> data = rawData();
            data.removeFirst();
            //0 = name, 1 = Mass, 2 = Radius, (3, 4) Position, (5, 6) Velocity, 7 = relative body
            for (String[] row : data) {
                CelestialBody body = createBody(row);
                if (Boolean.parseBoolean(row[8])) {

                }
                bodies.add(body);
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

        return data;
    }

    private CelestialBody createBody(String[] data) {
        String name = data[0];
        double mass = getMass(data[1]);
        double radius = getRadius(data[2]);
        Vec2 position = new Vec2(getDistance(data[3]), getDistance(data[4]));
        Vec2 velocity = new Vec2(getValue(data[3]), getValue(data[4]));
        Paint planetColor = getColor(data[7]);

        return new CelestialBody();
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

    private Vec2 getVec(String str1, String str2) {
        return new Vec2(getDistance(str1), getDistance(str2));
    }

    private double extractDouble(String str) {
        return Double.parseDouble(str.replaceAll("[^\\d.]", ""));
    }

    private double getValue(String str) {
        return extractDouble(str);
    }
}
