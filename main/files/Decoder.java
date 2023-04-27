/* Class: Decoder
 * Author: Christian Torres
 * Date: 2023/3/25
 *
 * Purpose: The Decoder class is responsible for converting string data into appropriate values
 *          for various celestial body attributes such as mass, radius, distance, and color.
 *          It also helps set the relative positions of celestial bodies.
 *
 * Attributes:
 * -logger: Logger
 *
 * Methods:
 * +getMass(String): double
 * +getRadius(String): double
 * +getDistance(String): double
 * +getColor(String): Paint
 * +extractDouble(String): double
 * +setRelative(String, LinkedList<CelestialBody>, CelestialBody): void
 */
package main.files;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import main.utils.Constants;
import main.simulation.bodies.CelestialBody;
import java.util.LinkedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Decoder {
    private Logger logger = LoggerFactory.getLogger(Decoder.class);

    public double getMass(String str) {
        double massValue = extractDouble(str);

        if (str.contains("Ms")) {
            massValue *= Constants.M_SOL;
        } else if (str.contains("Me")) {
            massValue *= Constants.M_EARTH;
        } else if (str.contains("Mj")) {
            massValue *= Constants.M_JUPITER;
        } else if (str.contains("Mm")) {
            massValue *= Constants.M_MOON;
        }

        return massValue;
    }

    public double getRadius(String str) {
        double radiusValue = extractDouble(str);

        if (str.contains("Rs")) {
            radiusValue *= Constants.R_SOL;
        } else if (str.contains("Re")) {
            radiusValue *= Constants.R_EARTH;
        } else if (str.contains("Rj")) {
            radiusValue *= Constants.R_JUPITER;
        } else if (str.contains("Rm")) {
            radiusValue *= Constants.R_MOON;
        }

        return radiusValue;
    }

    public double getDistance(String str) {
        double dist = extractDouble(str);
        if (str.contains("Au")) {
            dist *= Constants.ASTRONOMICAL_UNIT;
        } else if (str.contains("Km")) {
            dist *= Constants.KILOMETER;
        }
        return dist;
    }

    public Paint getColor(String str) {
        if (str.charAt(0) == '#') {
            return Paint.valueOf(str);
        } else {
            logger.warn("Error loading planet color, defaulting to white");
            return Color.WHITE;
        }
    }

    public double extractDouble(String str) {
        try {
            return Double.parseDouble(str.replaceAll("[^\\d.Ee+-]", ""));
        } catch (Exception e) {
            return Double.parseDouble(str.replaceAll("[^\\d.]", ""));
        }
    }

    public void setRelative(String str, LinkedList<CelestialBody> bodies, CelestialBody body) {

        for (CelestialBody otherBody : bodies) {
            if (otherBody.getName().equals(str)) {
                body.setParentObject(otherBody);
                body.getPosition().incrementBy(otherBody.getPosition());
                body.getVelocity().incrementBy(otherBody.getVelocity());
                return;
            }
        }
    }
}
