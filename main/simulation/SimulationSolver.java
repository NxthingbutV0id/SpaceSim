package main.simulation;

import java.util.LinkedList;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.*;
import main.constants.Mass;
import main.customUtils.*;
import main.constants.*;

public class SimulationSolver {
    private final LinkedList<CelestialBody> bodies = new LinkedList<>();
    private int simulationTime;

    public SimulationSolver() {
        simulationTime = 0;
    }

    public void createBodies() {
        CelestialBody body1 = new CelestialBody(
                "A",
                Mass.EARTH,
                100,
                new Vec2(0, 0),
                new Vec2(0, 0),
                Color.RED
        );
        CelestialBody body2 = new CelestialBody(
                "B",
                Mass.EARTH,
                100,
                new Vec2(-500, 0),
                new Vec2(0, -1e6),
                Color.BLUE
        );
        CelestialBody body3 = new CelestialBody(
                "C",
                Mass.EARTH,
                100,
                new Vec2(0, 750),
                new Vec2(1e6, 0),
                Color.GREEN
        );
        bodies.add(body1);
        bodies.add(body2);
        bodies.add(body3);
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

    private Vec2 getAcceleration(CelestialBody body){
        Vec2 accel = new Vec2(0, 0);
        for (CelestialBody otherBody : bodies) {
            if (otherBody != body) {
                Vec2 step1 = body.getPosition().sub(otherBody.getPosition());
                double step2 = step1.getMagnitude() * step1.getMagnitude() * step1.getMagnitude();
                Vec2 step3 = step1.divide(step2);
                Vec2 step4 = step3.multiply(otherBody.getMass());
                Vec2 step5 = step4.multiply(Universe.GRAVITATIONAL_CONSTANT);
                Vec2 step6 = step5.multiply(-1);
                accel.incrementBy(step6);
            }
        }
        if (Double.isNaN(accel.getX()) || Double.isNaN(accel.getY())) {
            System.out.println("AN ERROR OCCURRED, POSSIBLE COLLISION");
        }
        return accel;
    }

    private void updatePosition(CelestialBody body, Vec2 velocity, double deltaT) {
        Vec2 currentPos = body.getPosition();
        body.setPosition(currentPos.add(velocity.multiply(deltaT)));
    }

    private void updateVelocity(CelestialBody body, Vec2 acceleration, double deltaT) {
        Vec2 currentVel = body.getVelocity();
        body.setVelocity(currentVel.add(acceleration.multiply(deltaT)));
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
        System.out.println(centerOfMass);
        return centerOfMass;
    }
}