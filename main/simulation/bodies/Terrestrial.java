/* Class: Terrestrial
 * Author: Christian Torres
 * Date: 2023/4/6
 *
 * Purpose: The Terrestrial class represents a terrestrial celestial body, inheriting from the CelestialBody class.
 *
 * Attributes:
 * -albedo: double
 * -greenhouseFactor: double
 * -hasAtmosphere: boolean
 *
 * Methods:
 * +setGreenhouseFactor(double greenhouseFactor): void
 * +setAlbedo(double albedo): void
 * +setHasAtmosphere(boolean hasAtmosphere): void
 * -setTemp(Star star): void
 */

package main.simulation.bodies;

import main.utils.*;

public class Terrestrial extends CelestialBody {
    private double albedo; //value from 0 to 1
    private double greenhouseFactor; // earth ~= 1.132, venus ~= 3.166, vacuum = 1
    private boolean hasAtmosphere;

    public Terrestrial(String name, double mass, double radius, Vec2 position, Vec2 velocity) {
        super(name, mass, radius, position, velocity);
    }

    public void setGreenhouseFactor(double greenhouseFactor) {
        this.greenhouseFactor = greenhouseFactor;
    }

    public void setAlbedo(double albedo) {
        this.albedo = albedo;
    }

    public void setHasAtmosphere(boolean hasAtmosphere) {
        this.hasAtmosphere = hasAtmosphere;
    }

    public void setTemp(Star star) {
        if (star != null) {
            double dist = position.distance(star.getPosition());
            if (hasAtmosphere) {
                double effectiveTemp = Math.sqrt(Math.sqrt((star.getLuminosity() * (1 - albedo))
                        / (16 * Math.PI * (dist * dist) * Constants.STEFAN_BOLTZMANN)));
                surfaceTemp = effectiveTemp * greenhouseFactor;
            } else {
                surfaceTemp = Math.sqrt(Math.sqrt((star.getLuminosity() * (1 - albedo))
                        / (16 * Math.PI * (dist * dist) * Constants.STEFAN_BOLTZMANN)));
            }
        } else {
            surfaceTemp = 0;
        }
    }
}
