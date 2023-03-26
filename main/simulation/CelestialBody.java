package main.simulation;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;
import main.customUtils.*;

public class CelestialBody {
    private String name;
    private double mass;
    private double radius;
    private Vec2 position;
    private Vec2 velocity;
    private Paint planetColor;

    public CelestialBody(String name, double mass, double radius, Vec2 position, Vec2 velocity, Paint color) {
        this.name = name;
        this.mass = mass;
        this.radius = radius;
        this.position = position;
        this.velocity = velocity;
        planetColor = color;
    }

    public CelestialBody() {}

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

    public void setPosition(Vec2 position, Vec2 relative) {
        this.position = position.add(relative);
    }

    public void setVelocity(Vec2 velocity, Vec2 relative) {
        this.velocity = velocity.add(relative);
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

    public void setName(String name) {
        this.name = name;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public void setPlanetColor(Paint planetColor) {
        this.planetColor = planetColor;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
