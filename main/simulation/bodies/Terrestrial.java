/* Class: Terrestrial
 * Author: Christian Torres
 * Date: 4/6/2023
 *
 * Purpose: its like earth, or the moon, or whatever you want it to be
 *
 * Attributes: *TO BE FINALIZED*
 *
 * Methods: *TO BE FINALIZED*
 */
package main.simulation.bodies;

import main.customUtils.*;

public class Terrestrial extends CelestialBody {
    private double albedo; //value from 0 to 1
    private double greenhouseFactor; // earth ~= 1.132, venus ~= 3.166, vacuum = 1
    private boolean hasAtmosphere;

    public Terrestrial(String name, double mass, double radius, Vec2 position, Vec2 velocity) {
        super(name, mass, radius, position, velocity);
    }

    public void setInitTemp(double temp) {
        surfaceTemp = temp;
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
