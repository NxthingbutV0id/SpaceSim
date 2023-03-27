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

import javafx.scene.paint.*;
import main.customUtils.*;
import main.constants.*;

public class SimulationSolver {
    private LinkedList<CelestialBody> bodies = new LinkedList<>();
    private int simulationTime;

    public SimulationSolver() {
        simulationTime = 0;
    }

    public void createBodies() {
        try {
            DataManager dm = new DataManager();
            bodies = dm.getData();
            for (CelestialBody body : bodies) {
                body.printStats();
            }
        } catch (Exception e) {
            System.out.println("Error creating bodies, make sure \"system.csv\" is in the proper format");
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
        }
    }

    public LinkedList<CelestialBody> getBodies() {
        return bodies;
    }

    //TODO: get this shit to work dammit! a = GM/r^2
    private Vec2 getAcceleration(CelestialBody body){
        Vec2 accel;
        Vec2 totalAccel = new Vec2(0, 0);
        for (CelestialBody otherBody : bodies) {
            if (otherBody != body) {
                double dist = body.getPosition().distance(otherBody.getPosition());
                double dist2 = dist * dist;
                Vec2 direction = body.getPosition().sub(otherBody.getPosition()).normalize();
                double magnitude = (Constants.GRAVITATIONAL_CONSTANT * body.getMass())/dist2;
                accel = new Vec2(magnitude, direction);
                totalAccel.incrementBy(accel);
            }
        }
        if (Double.isNaN(totalAccel.getX()) || Double.isNaN(totalAccel.getY())) {
            System.out.println("AN ERROR OCCURRED, POSSIBLE COLLISION");
        }
        System.out.println(body.getName() + " acceleration calculated: " + totalAccel);
        return totalAccel;
    }

    private void updatePosition(CelestialBody body, Vec2 velocity, double deltaT) {
        body.getPosition().incrementBy(velocity.multiply(deltaT));
    }

    private void updateVelocity(CelestialBody body, Vec2 acceleration, double deltaT) {
        System.out.println(body.getName() + " updateVelocity deltaT: " + deltaT);
        System.out.println(body.getName() + " velocity before: " + body.getVelocity());
        body.getVelocity().incrementBy(acceleration.multiply(deltaT));
        System.out.println(body.getName() + " velocity after: " + body.getVelocity());
        System.out.println();
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

    public void printBodiesStats() {
        for (CelestialBody body : bodies) {
            body.printStats();
        }
    }
}