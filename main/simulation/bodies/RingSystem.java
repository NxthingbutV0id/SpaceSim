package main.simulation.bodies;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import main.customUtils.Vec2;

public class RingSystem{
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

    public void drawRing(GraphicsContext g, double scale, double screenWidth, double screenHeight, Vec2 relative) {
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
