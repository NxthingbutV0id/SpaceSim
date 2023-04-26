/* Class: BodyGraphics
 * Author: Christian Torres
 * Date: 2023/4/19
 *
 * Purpose: This class is responsible for rendering celestial bodies
 *          and their associated visual components (e.g., ring systems)
 *          on the simulation canvas.
 *
 * Attributes:
 * -x: double
 * -y: double
 * -relX: double
 * -relY: double
 * -body: CelestialBody
 * -ring: RingSystem
 *
 * Methods:
 * +BodyGraphics(CelestialBody): Constructor
 * +BodyGraphics(RingSystem): Constructor
 * +drawBody(GraphicsContext, double, double, double, Vec2): void
 * +drawBodyText(GraphicsContext, double, double, double, Vec2): void
 * +drawRing(GraphicsContext, double, double, double, Vec2): void
 */
package main.simulation.bodies;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import main.utils.Vec2;

public class BodyGraphics {
    private double x;
    private double y;
    private double relX;
    private double relY;
    private CelestialBody body;
    private RingSystem ring;

    public BodyGraphics(CelestialBody body) {this.body = body;}
    public BodyGraphics(RingSystem ring) {this.ring = ring;}

    public void drawBody(GraphicsContext g, double scale, double screenWidth, double screenHeight, Vec2 relative) {
        if (body != null) {
            x = body.position.getX() * scale;
            y = body.position.getY() * scale;
            double r = body.radius * scale;
            relX = relative.getX() * scale;
            relY = relative.getY() * scale;

            g.setFill(body.planetColor);
            g.fillOval(
                    ((x - r) + screenWidth / 2) - relX,
                    ((y - r) + screenHeight / 2) - relY,
                    2 * r,
                    2 * r
            );
        }
    }

    public void drawBodyText(GraphicsContext g, double scale, double screenWidth, double screenHeight, Vec2 relative) {
        x = body.position.getX() * scale;
        y = body.position.getY() * scale;
        relX = relative.getX() * scale;
        relY = relative.getY() * scale;

        g.setFont(new Font("Impact", 20));

        g.setFill(Color.BLACK);
        g.fillText(body.name, ((x) + screenWidth/2) - relX - 2, ((y) + screenHeight/2) - relY - 2);
        g.setFill(Color.WHITE);
        g.fillText(body.name, ((x) + screenWidth/2) - relX, ((y) + screenHeight/2) - relY);
    }

    public void drawRing(GraphicsContext g, double scale, double screenWidth, double screenHeight, Vec2 relative) {
        double ir, or;

        x = ring.getParent().getScreenPosition(scale, screenWidth, screenHeight, relative).getX();
        y = ring.getParent().getScreenPosition(scale, screenWidth, screenHeight, relative).getY();
        ir = ring.getInnerRadius() * scale;
        or = ring.getOuterRadius() * scale;

        g.setFill(ring.getColor());
        g.setGlobalAlpha(ring.getOpacity());
        g.fillOval(
                x - or,
                y - or,
                2*or,
                2*or
        );
        g.setGlobalAlpha(1);
        g.setFill(Color.BLACK);
        g.fillOval(
                x - ir,
                y - ir,
                2*ir,
                2*ir
        );
    }
}
