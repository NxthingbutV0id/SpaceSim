/* Class: CelestialBody
 * Author: Christian Torres
 * Created: 2023/3/13
 * Modified:
 *
 * Purpose: The bodies that generate gravity
 *
 * Attributes: *TO BE FINALIZED*
 *
 * Methods: *TO BE FINALIZED*
 */
package main.simulation.bodies;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;
import main.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.LinkedList;
import static java.lang.Math.*;

public abstract class CelestialBody {
    protected String name;
    protected double mass;
    protected double radius;
    protected Vec2 position;
    protected Vec2 velocity;
    protected Paint planetColor;
    protected double surfaceTemp;
    protected CelestialBody parent;
    private LinkedList<Vec2> path = new LinkedList<>();
    private Logger logger = LoggerFactory.getLogger(CelestialBody.class);
    private BodyGraphics bodyGraphics;
    private OrbitGraphics orbitGraphics;

    public CelestialBody(String name, double mass, double radius, Vec2 position, Vec2 velocity) {
        this.name = name;
        this.mass = mass;
        this.radius = radius;
        this.position = position;
        this.velocity = velocity;
        bodyGraphics = new BodyGraphics(this);
        orbitGraphics = new OrbitGraphics(this);
    }

    public double getMass() {return mass;}
    public Vec2 getPosition() {return position;}
    public Vec2 getVelocity() {return velocity;}
    public String getName() {return name;}
    public double getRadius() {return radius;}
    public double getTemperature() {return surfaceTemp;}
    public void setPlanetColor(Paint planetColor) {this.planetColor = planetColor;}
    public void setPosition(Vec2 position) {this.position = position;}
    public void setParentObject(CelestialBody parent) {this.parent = parent;}

    public void addToPath() {
        orbitGraphics.addToPath();
    }

    public Vec2 getScreenPosition(double scale, double screenWidth, double screenHeight, Vec2 relative) {
        double x, y, relX, relY;

        x = position.getX() * scale;
        y = position.getY() * scale;
        relX = relative.getX() * scale;
        relY = relative.getY() * scale;

        return new Vec2(((x + screenWidth/2) - relX), ((y + screenHeight/2) - relY));
    }

    public void drawBody(GraphicsContext g, double scale, double screenWidth, double screenHeight, Vec2 relative) {
        bodyGraphics.drawBody(g, scale, screenWidth, screenHeight, relative);
    }

    public void drawBodyText(GraphicsContext g, double scale, double screenWidth, double screenHeight, Vec2 relative) {
        bodyGraphics.drawBodyText(g, scale, screenWidth, screenHeight, relative);
    }

    public void drawOrbit(GraphicsContext gc, double scale, double screenWidth, double screenHeight, Vec2 camera) {
        orbitGraphics.drawOrbit(gc, scale, screenWidth, screenHeight, camera);
    }

    public void printStats() {
        logger.debug("Body stats debug\nName: {}\nMass: {}\nRadius: {}\nPosition: {}\nVelocity: {}\n",
                name, mass, radius, position, velocity
        );
    }

    public abstract void setTemp(Star star);
}
