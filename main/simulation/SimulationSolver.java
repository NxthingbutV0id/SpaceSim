/* Class: SimulationSolver
 * Author: Christian Torres
 * Created: 2023/3/13
 * Modified:
 *
 * Purpose: grabs the data from the file and runs the calculations
 *
 * Attributes: -bodies: LinkedList<CelestialBody>
 *             -simulationTime: int
 *             -animator: Animator
 *             -logger: Logger
 *
 * Methods: *** for now, its a lot ***
 */
package main.simulation;

import java.util.LinkedList;
import main.utils.*;
import main.files.JsonReader;
import main.graphics.Animator;
import main.simulation.bodies.CelestialBody;
import main.simulation.bodies.Star;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.lang.Math.sqrt;

public class SimulationSolver {
    private LinkedList<CelestialBody> bodies = new LinkedList<>();
    private int simulationTime;
    private Animator animator;
    private Logger logger = LoggerFactory.getLogger(SimulationSolver.class);
    private LinkedList<Star> stars = new LinkedList<>();

    public SimulationSolver(Animator animator) {
        simulationTime = 0;
        this.animator = animator;
    }

    public void createBodies(String path) {
        try {
            JsonReader reader = new JsonReader();
            bodies = reader.loadFile(path);
            animator.setScale(reader.getScale());
            animator.setTimeScale(reader.getTimeScale());
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (CelestialBody body : bodies) {
            if (body instanceof Star) {
                stars.add((Star) body);
            }
        }
    }

    public void update(double deltaT, int timeStep) {
        simulationTime++;
        for (int i = 0; i < timeStep; i++) {
            for (CelestialBody body : bodies) {
                Vec2 originalVelocity = body.getVelocity().copy();
                Vec2 originalPosition = body.getPosition().copy();

                Vec2 k1v = getAcceleration(body).multiply(deltaT / timeStep);
                Vec2 k1x = originalVelocity.multiply(deltaT / timeStep);

                body.getVelocity().incrementBy(k1v.multiply(0.5));
                body.getPosition().incrementBy(k1x.multiply(0.5));

                Vec2 k2v = getAcceleration(body).multiply(deltaT / timeStep);
                Vec2 k2x = body.getVelocity().multiply(deltaT / timeStep);

                body.getVelocity().set(originalVelocity.add(k2v.multiply(0.5)));
                body.getPosition().set(originalPosition.add(k2x.multiply(0.5)));

                Vec2 k3v = getAcceleration(body).multiply(deltaT / timeStep);
                Vec2 k3x = body.getVelocity().multiply(deltaT / timeStep);

                body.getVelocity().set(originalVelocity.add(k3v));
                body.getPosition().set(originalPosition.add(k3x));

                Vec2 k4v = getAcceleration(body).multiply(deltaT / timeStep);
                Vec2 k4x = body.getVelocity().multiply(deltaT / timeStep);

                body.getVelocity().set(originalVelocity);
                body.getPosition().set(originalPosition);

                Vec2 deltaV = k1v.add(k2v.multiply(2)).add(k3v.multiply(2)).add(k4v).multiply(1.0 / 6.0);
                Vec2 deltaX = k1x.add(k2x.multiply(2)).add(k3x.multiply(2)).add(k4x).multiply(1.0 / 6.0);

                body.getVelocity().incrementBy(deltaV);
                body.getPosition().incrementBy(deltaX);

                for (Star star : stars) {
                    body.setTemp(star);
                }
            }
        }
    }

    private Vec2 getAcceleration(CelestialBody body){
        double dx, dy, r, f, ax = 0, ay = 0;
        for (CelestialBody otherBody : bodies) {
            if (otherBody != body) {
                dx = otherBody.getPosition().getX() - body.getPosition().getX();
                dy = otherBody.getPosition().getY() - body.getPosition().getY();

                r = sqrt(dx * dx + dy * dy);
                f = Constants.GRAVITATIONAL_CONSTANT * otherBody.getMass() / (r * r);

                ax += f * dx/r;
                ay += f * dy/r;
            }
        }
        if (Double.isNaN(ax) || Double.isNaN(ay)) {
            logger.error(
                    "Possible collision detected at {}, {} acceleration is NaN", body.getPosition(), body.getName());
        }
        return new Vec2(ax, ay);
    }

    public int getSimulationTime() {
        return simulationTime;
    }
    public LinkedList<CelestialBody> getBodies() {
        return bodies;
    }
}