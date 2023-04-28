/* Class: Star
 * Author: Christian Torres
 * Date: 2023/4/6
 *
 * Purpose: The Star class represents a celestial body that is a star, inheriting from the CelestialBody class.
 *
 * Attributes:
 * -luminosity: double
 *
 * Inherited Attributes:
 * -name: String
 * -mass: double
 * -radius: double
 * -position: Vec2
 * -velocity: Vec2
 * -planetColor: Color
 * -surfaceTemp: double
 * -parent: CelestialBody
 * -orbitGraphics: OrbitGraphics
 * -bodyGraphics: BodyGraphics
 *
 * Methods:
 * +getLuminosity(): double
 *
 * Inherited Methods:
 * +getMass(): double
 * +getRadius(): double
 * +getPosition(): Vec2
 * +getVelocity(): Vec2
 * +getOrbitGraphics(): OrbitGraphics
 * +getBodyGraphics(): BodyGraphics
 * +getTemperature(): double
 * +getName(): String
 * +getParent(): CelestialBody
 *
 * Private Methods:
 * -setLuminosity(): double
 * -setTemp(Star star): void
 */

package main.simulation.bodies;

import main.utils.*;

public class Star extends CelestialBody {
    private double luminosity;

    public Star(String name, double mass, double radius, Vec2 position, Vec2 velocity) {
        super(name, mass, radius, position, velocity);

        luminosity = setLuminosity() * Constants.L_SOL;
        setTemp(this);
    }

    private double setLuminosity() {
        double relativeMass = mass/Constants.M_SOL;
        if (relativeMass < 0.43) {
            return 0.23*Math.pow(relativeMass, 2.3);// 0.23 * M^2.3
        } else if (relativeMass < 2) {
            return relativeMass * relativeMass * relativeMass * relativeMass; // M^4
        } else {
            return 1.4*(relativeMass * relativeMass * relativeMass * Math.sqrt(relativeMass));// 1.4 * M^3.5
        }
    }

    public double getLuminosity() {
        return luminosity;
    }

    @Override
    public void setTemp(Star star) {
        surfaceTemp =  Math.sqrt(Math.sqrt(luminosity/(radius * radius)));
    }
}