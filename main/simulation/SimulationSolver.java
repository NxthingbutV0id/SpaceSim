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
import main.customUtils.*;
import main.files.JsonReader;
import main.graphics.Animator;
import main.simulation.bodies.CelestialBody;
import main.simulation.bodies.GasGiant;
import main.simulation.bodies.Star;
import main.simulation.bodies.Terrestrial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.lang.Math.sqrt;

public class SimulationSolver {
    private LinkedList<CelestialBody> bodies = new LinkedList<>();
    private int simulationTime;
    private Animator animator;
    private Logger logger = LoggerFactory.getLogger(SimulationSolver.class);

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
    }

    public void update(double deltaT, int timeStep) {
        simulationTime++;
        LinkedList<Star> stars = new LinkedList<>();
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
                if (body instanceof Star) {
                    stars.add((Star) body);
                } else {
                    for (Star star : stars) {
                        body.setTemp(star);
                    }
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

    private void updatePosition(CelestialBody body, Vec2 velocity, double deltaT) {
        body.getPosition().incrementBy(velocity.multiply(deltaT));
    }

    private void updateVelocity(CelestialBody body, Vec2 acceleration, double deltaT) {
        body.getVelocity().incrementBy(acceleration.multiply(deltaT));
    }

    public int getSimulationTime() {
        return simulationTime;
    }
    public LinkedList<CelestialBody> getBodies() {
        return bodies;
    }

    public Vec2 getCenterOfMass() {
        double totalMass = 0;
        Vec2 centerOfMass, summationVec = new Vec2(0, 0);
        for (CelestialBody body : bodies) {
            totalMass += body.getMass();
        }
        for (CelestialBody body : bodies) {
            summationVec.incrementBy(body.getPosition().multiply(body.getMass()));
        }
        centerOfMass = summationVec.divide(totalMass);
        return centerOfMass;
    }

    public Vec2 getCenterOfAllBodies() {
        Vec2 summationVec = new Vec2(0, 0);
        for (CelestialBody body : bodies) {
            summationVec.incrementBy(body.getPosition());
        }
        return summationVec;
    }

    public void printBodiesStats() {
        for (CelestialBody body : bodies) {
            body.printStats();
        }
    }
}