/* Class: CelestialBody
 * Author: Christian Torres
 * Created: 2023/3/13
 * Modified:
 *
 * Purpose: The bodies that generate gravity
 *
 * Attributes: -name: String
 *             -mass: double
 *             -radius: double
 *             -position: Vec2
 *             -velocity: Vec2
 *             -planetColor: Paint
 *
 * Methods: +CelestialBody(String, double, double, Vec2, Vec2, Paint): this
 *          +getMass(): double
 *          +getPosition(): Vec2
 *          +getVelocity(): Vec2
 *          +drawBody(GraphicsContext, double, double, double, Vec2): void
 *          +drawBodyText(GraphicsContext, double, double, double, Vec2): void
 *          +getName(): String
 *          +setName(String): void
 *          +printStats(): void
 */
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
        this.radius = 100 * radius;
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

    public void drawBodyText(GraphicsContext g, double scale, double screenWidth, double screenHeight, Vec2 relative) {
        double x, y, r, relX, relY;

        x = position.getX() * scale;
        y = position.getY() * scale;
        r = radius*scale;
        relX = relative.getX() * scale;
        relY = relative.getY() * scale;

        g.setFill(Color.WHITE);
        g.fillText(name, ((x - r) + screenWidth/2) - relX, ((y - r) + screenHeight/2) - relY);
    }

    public String getName() {
        return name;
    }

    public void printStats() {
        System.out.println("Name: " + name);
        System.out.println("Mass: " + mass);
        System.out.println("Radius: " + radius);
        System.out.println("Position: " + position);
        System.out.println("Velocity: " + velocity);
        System.out.println();
    }
}
