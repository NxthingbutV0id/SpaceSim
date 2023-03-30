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
package main.simulation.bodies;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;
import main.customUtils.*;

import java.util.LinkedList;
import java.util.Queue;

public class CelestialBody {
    protected String name;
    protected double mass;
    protected double radius;
    protected Vec2 position;
    protected Vec2 velocity;
    protected Paint planetColor;
    protected double surfaceTemp;
    private Queue<Vec2> path = new LinkedList<>();

    public CelestialBody(String name, double mass, double radius, Vec2 position, Vec2 velocity) {
        this.name = name;
        this.mass = mass;
        this.radius = radius;
        this.position = position;
        this.velocity = velocity;
        path.add(position);
    }

    public double getMass() {return mass;}
    public Vec2 getPosition() {return position;}
    public Vec2 getVelocity() {return velocity;}
    public String getName() {return name;}
    public double getRadius() {return radius;}
    public Paint getPlanetColor() {return planetColor;}
    public double getSurfaceTemp() {return surfaceTemp;}
    public void setName(String name) {this.name = name;}
    public void setMass(double mass) {this.mass = mass;}
    public void setRadius(double radius) {this.radius = radius;}
    public void setPosition(Vec2 position) {this.position = position;}
    public void setVelocity(Vec2 velocity) {this.velocity = velocity;}
    public void setPlanetColor(Paint planetColor) {this.planetColor = planetColor;}
    public void setSurfaceTemp(double surfaceTemp) {this.surfaceTemp = surfaceTemp;}

    public void addToPath() {
        path.add(position);
        if (path.size() > 1000) {
            path.poll();
        }
    }

    public Vec2 getScreenPosition(double scale, double screenWidth, double screenHeight, Vec2 relative) {
        double x, y, r, relX, relY;

        x = position.getX() * scale;
        y = position.getY() * scale;
        r = radius*scale;
        relX = relative.getX() * scale;
        relY = relative.getY() * scale;

        return new Vec2((((x - r) + screenWidth/2) - relX), (((y - r) + screenHeight/2) - relY));
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

    public void drawBodyPath(GraphicsContext gc, double scale, double screenWidth, double screenHeight, Vec2 relative) {
        double xPrev, yPrev, xCurr, yCurr, r, relX, relY;
        r = radius * scale;
        relX = relative.getX() * scale;
        relY = relative.getY() * scale;

        gc.setStroke(planetColor);
        gc.setLineWidth(100);

        Vec2 previous = path.poll();
        //TODO: fix this shit
        for (Vec2 current : path) {
            xPrev = ((previous.getX() - r) + screenWidth/2) - relX;
            yPrev = ((previous.getY() - r) + screenHeight/2) - relY;
            xCurr = ((current.getX() - r) + screenWidth/2) - relX;
            yCurr = ((current.getY() - r) + screenHeight/2) - relY;
            gc.strokeLine(xPrev, yPrev, xCurr, yCurr);
            previous = current;
        }
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
