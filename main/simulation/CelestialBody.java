package main.simulation;

import customUtils.*;
import java.awt.*;

public class CelestialBody {
    private String name;
    private double mass;
    private double radius;
    private Vec2 position;
    private Vec2 velocity;
    private Color planetColor;

    public CelestialBody(String name, double mass, double radius, Vec2 position, Vec2 velocity, Color color) {
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

    public void drawBody(Graphics2D g, double scale, int screenWidth, int screenHeight, Vec2 relative) {
        g.setColor(planetColor);
        g.fillOval(
                (int)(((position.getX() * scale) + screenWidth/2) - relative.getX()),
                (int)(((position.getY() * scale) + screenHeight/2) - relative.getY()),
                (int)(radius*scale),
                (int)(radius*scale)
        );
    }

    public String getName() {
        return name;
    }
}
