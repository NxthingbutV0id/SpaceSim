package main.simulation;

import java.awt.*;
import java.util.LinkedList;
import customUtils.*;
import main.constants.Universe;

public class SimulationSolver {
    private final LinkedList<CelestialBody> bodies = new LinkedList<>();
    private int simulationTime;

    public SimulationSolver() {
        simulationTime = 0;
    }

    public void createBodies() {
        CelestialBody body1 = new CelestialBody(
                "A",
                1,
                100,
                new Vec2(0, 0),
                new Vec2(0, 0),
                Color.RED
        );
        CelestialBody body2 = new CelestialBody(
                "B",
                1,
                100,
                new Vec2(-500, 0),
                new Vec2(0, 0),
                Color.BLUE
        );
        CelestialBody body3 = new CelestialBody(
                "C",
                1,
                100,
                new Vec2(0, 600),
                new Vec2(0, 0),
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
            Vec2 step1 = body.getPosition().sub(otherBody.getPosition());
            double step2 = step1.getMagnitude() * step1.getMagnitude() * step1.getMagnitude();
            Vec2 step3 = step1.divide(step2);
            Vec2 step4 = step3.multiply(otherBody.getMass());
            Vec2 step5 = step4.multiply(Universe.GRAVITATIONAL_CONSTANT);
            Vec2 step6 = step5.multiply(-1);
            accel.incrementBy(step6);
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
        double xCM, yCM, xN, yN, xD, yD;
        xN = yN = xD = yD = 0;
        for (CelestialBody body : bodies) {
            xN += body.getPosition().getX() * body.getMass();
            yN += body.getPosition().getY() * body.getMass();
            xD += body.getMass();
            yD += body.getMass();
        }

        xCM = xN/xD;
        yCM = yN/yD;

        return new Vec2(xCM, yCM);
    }
}