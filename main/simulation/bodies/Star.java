/* Class: Star
 * Author: Christian Torres
 * Date: 4/6/2023
 *
 * Purpose: "Its a star!" - Bill Wurtz
 *
 * Attributes: *TO BE FINALIZED*
 *
 * Methods: *TO BE FINALIZED*
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