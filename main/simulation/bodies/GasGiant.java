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
            rings.drawBody(g, scale, screenWidth, screenHeight, relative);
        }

        g.setFill(planetColor);
        g.fillOval(
                ((x - r) + screenWidth/2) - relX,
                ((y - r) + screenHeight/2) - relY,
                2*r,
                2*r
        );
    }

    private class RingSystem {
        private double innerRadius, outerRadius;
        private Paint color;
        private double opacity;
        private Vec2 position;

        public RingSystem(CelestialBody body, double innerRadius, double outerRadius, Paint color, double opacity) {
            position = body.position;
            this.innerRadius = innerRadius * body.radius;
            this.outerRadius = outerRadius * body.radius;
            this.color = color;
            this.opacity = opacity;
        }

        public void drawBody(GraphicsContext g, double scale, double screenWidth, double screenHeight, Vec2 relative) {
            double x, y, ir, or, relX, relY;

            x = position.getX() * scale;
            y = position.getY() * scale;
            ir = innerRadius * scale;
            or = outerRadius * scale;
            relX = relative.getX() * scale;
            relY = relative.getY() * scale;

            g.setFill(Color.BLACK);
            g.fillOval(
                    ((x - or) + screenWidth/2) - relX,
                    ((y - or) + screenHeight/2) - relY,
                    2*or,
                    2*or
            );
            g.setFill(color);
            g.setGlobalAlpha(opacity);
            g.fillOval(
                    ((x - ir) + screenWidth/2) - relX,
                    ((y - ir) + screenHeight/2) - relY,
                    2*ir,
                    2*ir
            );
            g.setGlobalAlpha(1);
        }
    }
}
