package main.simulation;

import main.engine.*;

public class SimulationManager implements AbstractScene {
    private final Container container;
    private final SimulationSolver simulator = new SimulationSolver();

    public SimulationManager() {
        container = new Container(1280, 720, 1f, "Gravity Simulation", this);
    }

    public void start() {
        container.start();
        simulator.createBodies();
    }

    @Override
    public void update(Container container, double deltaT) {
        simulator.update(deltaT, 1);
    }

    @Override
    public void render(Container container, Renderer renderer) {

    }
}
