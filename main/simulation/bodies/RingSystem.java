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
    private BodyGraphics graphics;

    public RingSystem(CelestialBody parent, double innerRadius, double outerRadius, Paint color, double opacity) {
        this.parent = parent;
        this.innerRadius = innerRadius * parent.radius;
        this.outerRadius = outerRadius * parent.radius;
        this.color = color;
        this.opacity = opacity;
        graphics = new BodyGraphics(this);
    }

    public BodyGraphics getGraphics() {
        return graphics;
    }

    public CelestialBody getParent() {
        return parent;
    }

    public double getInnerRadius() {
        return innerRadius;
    }

    public double getOuterRadius() {
        return outerRadius;
    }

    public Paint getColor() {
        return color;
    }

    public double getOpacity() {
        return opacity;
    }
}
