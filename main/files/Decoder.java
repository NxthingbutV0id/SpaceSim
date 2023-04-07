/* Class: Decoder
 * Author: Christian Torres
 * Created: 2023/3/25
 * Modified:
 *
 * Purpose: parses data to usable values
 *
 * Attributes: *TO BE FINALIZED*
 *
 * Methods: *TO BE FINALIZED*
 */
package main.files;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import main.customUtils.Constants;
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
        logger.info("getRadius string now is {}", str);
        double radiusValue = extractDouble(str);

        logger.info("Value got from string is {}", radiusValue);

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

    public double getValue(String str) {
        return extractDouble(str);
    }

    public void setRelative(String str, LinkedList<CelestialBody> bodies, CelestialBody body) {

        for (CelestialBody otherBody : bodies) {
            if (otherBody.getName().equals(str)) {
                body.getPosition().incrementBy(otherBody.getPosition());
                body.getVelocity().incrementBy(otherBody.getVelocity());
                return;
            }
        }
    }
}
