/* Class: GasGiant
 * Author: Christian Torres
 * Date: 4/6/2023
 *
 * Purpose: a giant gas planet like jupiter, also could have rings!
 *
 * Attributes: *TO BE FINALIZED*
 *
 * Methods: *TO BE FINALIZED*
 */
package main.simulation.bodies;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import main.customUtils.Constants;
import main.customUtils.Vec2;

public class GasGiant extends CelestialBody {
    private double albedo;
    private RingSystem rings;

    public GasGiant(String name, double mass, double radius, Vec2 position, Vec2 velocity) {
        super(name, mass, radius, position, velocity);
    }

    public void setAlbedo(double albedo) {
        this.albedo = albedo;
    }

    public void setRings(double innerRadius, double outerRadius, Paint color, double opacity) {
        rings = new RingSystem(this, innerRadius, outerRadius, color, opacity);
    }

    @Override
    public void setTemp(Star star) {
        if (star != null) {
            double dist = position.distance(star.getPosition());
            surfaceTemp = Math.sqrt(Math.sqrt((star.getLuminosity() * (1 - albedo))
                    / (16 * Math.PI * (dist * dist) * Constants.STEFAN_BOLTZMANN)));
        } else {
            surfaceTemp = 0;
        }
    }

    @Override
    public void drawBody(GraphicsContext g, double scale, double screenWidth, double screenHeight, Vec2 relative) {
        double x, y, r, relX, relY;

        x = position.getX() * scale;
        y = position.getY() * scale;
        r = radius*scale;
        relX = relative.getX() * scale;
        relY = relative.getY() * scale;

        if (rings != null) {
            rings.drawRing(g, scale, screenWidth, screenHeight, relative);
        }

        g.setFill(planetColor);
        g.fillOval(
                ((x - r) + screenWidth/2) - relX,
                ((y - r) + screenHeight/2) - relY,
                2*r,
                2*r
        );
    }
}
