package main.simulation;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;
import main.customUtils.*;

public class CelestialBody {
    private final String name;
    private final double mass;
    private final double radius;
    private Vec2 position;
    private Vec2 velocity;
    private final Paint planetColor;

    public CelestialBody(String name, double mass, double radius, Vec2 position, Vec2 velocity, Paint color) {
        this.name = name;
        this.mass = mass;
        this.radius = radius;
        this.position = position;
        this.velocity = velocity;
        planetColor = color;
    }

    public double getMass() {
        return mass;
    }

    public Vec2 getPosition() {
        return position;
    }

    public Vec2 getVelocity() {
        return velocity;
    }

    public void setPosition(Vec2 position) {
        this.position = position;
    }

    public void setVelocity(Vec2 velocity) {
        this.velocity = velocity;
    }

    public void drawBody(GraphicsContext g, double scale, double screenWidth, double screenHeight, Vec2 relative) {
        double x, y, r, relX, relY;

        x = position.getX() * scale;
        y = position.getY() * scale;
        r = radius*scale;
        relX = relative.getX() * scale;
        relY = relative.getY() * scale;

        g.setFill(planetColor);
        g.fillOval(
                ((x - r) + screenWidth/2) - relX,
                ((y - r) + screenHeight/2) - relY,
                2*r,
                2*r
        );
    }

    public String getName() {
        return name;
    }
}
