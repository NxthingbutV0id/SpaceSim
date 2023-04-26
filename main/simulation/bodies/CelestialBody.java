/* Class: CelestialBody
 * Author: Christian Torres
 * Date: 2023/3/13
 *
 * Purpose: This abstract class represents a celestial body in the simulation,
 *          providing common properties and methods for managing the body's position,
 *          velocity, mass, and appearance.
 *
 * Attributes:
 * #name: String
 * #mass: double
 * #radius: double
 * #position: Vec2
 * #velocity: Vec2
 * #planetColor: Paint
 * #surfaceTemp: double
 * #parent: CelestialBody
 * -bodyGraphics: BodyGraphics
 * -orbitGraphics: OrbitGraphics
 *
 * Methods:
 * +CelestialBody(String, double, double, Vec2, Vec2): Constructor
 * +addToPath(): void
 * +getScreenPosition(double, double, double, Vec2): Vec2
 * +getBodyGraphics(): BodyGraphics
 * +getOrbitGraphics(): OrbitGraphics
 * +getMass(): double
 * +getPosition(): Vec2
 * +getVelocity(): Vec2
 * +getName(): String
 * +getRadius(): double
 * +getTemperature(): double
 * +setPlanetColor(Paint): void
 * +setPosition(Vec2): void
 * +setParentObject(CelestialBody): void
 * +setTemp(Star): void (abstract)
 */
package main.simulation.bodies;

import javafx.scene.paint.*;
import main.utils.*;

public abstract class CelestialBody {
    protected String name;
    protected double mass;
    protected double radius;
    protected Vec2 position;
    protected Vec2 velocity;
    protected Paint planetColor;
    protected double surfaceTemp;
    protected CelestialBody parent;
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

    public BodyGraphics getBodyGraphics() {
        return bodyGraphics;
    }

    public OrbitGraphics getOrbitGraphics() {
        return orbitGraphics;
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
    public abstract void setTemp(Star star);
}
