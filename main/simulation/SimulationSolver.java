/* Class: SimulationSolver
 * Author: Christian Torres
 * Created: 2023/3/13
 * Modified:
 *
 * Purpose: grabs the data from the file and runs the calculations
 *
 * Attributes:
 *
 * Methods: +_main(String[]): void - Start of program
 */
package main.simulation;

import java.util.LinkedList;

import main.customUtils.*;
import main.graphics.Animator;
import main.simulation.bodies.CelestialBody;
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

    public void createBodies() {
        try {
            DataManager dm = new DataManager("main/files/system.csv");
            bodies = dm.getData();
        } catch (Exception e) {
            logger.error(".csv file not found/failed to read file, make sure system.csv file is in the proper format");
            e.printStackTrace();
        }
    }

    public void update(double deltaT, int timeStep) {
        simulationTime++;
        for (int i = 0; i < timeStep; i++) {
            for (CelestialBody body : bodies) {
                updateVelocity(body, getAcceleration(body), deltaT / timeStep);
            }
            for (CelestialBody body : bodies) {
                updatePosition(body, body.getVelocity(), deltaT / timeStep);
            }
            for (CelestialBody body : bodies) {
                body.addToPath();
            }
        }
    }

    public LinkedList<CelestialBody> getBodies() {
        return bodies;
    }

    private Vec2 getAcceleration(CelestialBody body){
        double dx, dy, r, f, ax = 0, ay = 0;
        for (CelestialBody otherBody : bodies) {
            if (otherBody != body) {
                dx = otherBody.getPosition().getX() - body.getPosition().getX();
                dy = otherBody.getPosition().getY() - body.getPosition().getY();

                r = sqrt(dx * dx + dy * dy);
                f = Constants.GRAVITATIONAL_CONSTANT.getValue() * otherBody.getMass() / (r * r);

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