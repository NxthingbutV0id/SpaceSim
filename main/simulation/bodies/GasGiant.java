/* Class: GasGiant
 * Author: Christian Torres
 * Date: 2023/4/6
 *
 * Purpose: This class represents gas giant celestial bodies in the simulation,
 *          extending the CelestialBody class. It includes additional attributes and
 *          methods specific to gas giants, such as albedo and the presence of ring systems.
 *
 * Attributes:
 * -albedo: double
 * -rings: RingSystem
 *
 * Methods:
 * +GasGiant(String, double, double, Vec2, Vec2): Constructor
 * +setAlbedo(double): void
 * +setRings(double, double, Paint, double): void
 * +setTemp(Star): void
 * +getRings(): RingSystem
 * +hasRings(): boolean
 */
package main.simulation.bodies;

import javafx.scene.paint.Paint;
import main.utils.Constants;
import main.utils.Vec2;

public class GasGiant extends CelestialBody {
    private double albedo;
    private RingSystem rings;

    public GasGiant(String name, double mass, double radius, Vec2 position, Vec2 velocity) {
        super(name, mass, radius, position, velocity);
    }

    public void setAlbedo(double albedo) {
        this.albedo = albedo;
    }

    public void setRings(double innerRadius, double outerRadius, Paint color, double opacity) {
        rings = new RingSystem(this, innerRadius, outerRadius, color, opacity);
    }

    @Override
    public void setTemp(Star star) {
        if (star != null) {
            double dist = position.distance(star.getPosition());
            surfaceTemp = Math.sqrt(Math.sqrt((star.getLuminosity() * (1 - albedo))
                    / (16 * Math.PI * (dist * dist) * Constants.STEFAN_BOLTZMANN)));
        } else {
            surfaceTemp = 0;
        }
    }

    public RingSystem getRings() {
        return rings;
    }

    public boolean hasRings() {
        return rings != null;
    }
}
