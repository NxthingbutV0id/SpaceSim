package main.simulation.bodies;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import main.utils.Vec2;

public class RingSystem{
    private double innerRadius, outerRadius;
    private Paint color;
    private double opacity;
    private CelestialBody parent;

    public RingSystem(CelestialBody parent, double innerRadius, double outerRadius, Paint color, double opacity) {
        this.parent = parent;
        this.innerRadius = innerRadius * parent.radius;
        this.outerRadius = outerRadius * parent.radius;
        this.color = color;
        this.opacity = opacity;
    }

    public void drawRing(GraphicsContext g, double scale, double screenWidth, double screenHeight, Vec2 relative) {
        double x, y, ir, or, relX, relY;

        x = parent.getScreenPosition(scale, screenWidth, screenHeight, relative).getX();
        y = parent.getScreenPosition(scale, screenWidth, screenHeight, relative).getY();
        ir = innerRadius * scale;
        or = outerRadius * scale;
        relX = relative.getX() * scale;
        relY = relative.getY() * scale;

        g.setFill(color);
        g.setGlobalAlpha(opacity);
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
