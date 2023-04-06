/* Class: SimulationHandler
 * Author: Christian Torres
 * Date: 4/6/2023
 *
 * Purpose: handles the simulation
 *
 * Attributes: TBD
 *
 * Methods: TBD
 */
package main.graphics;

import main.simulation.*;
import main.simulation.bodies.*;
import java.util.List;

public class SimulationHandler {
    private SimulationSolver simulator;

    public SimulationHandler(Animator animator) {
        simulator = new SimulationSolver(animator);
        simulator.createBodies();
    }

    public void update(double deltaT, double timeScale) {
        simulator.update(deltaT / timeScale, 100);
    }

    public List<CelestialBody> getBodies() {
        return simulator.getBodies();
    }

    public int getSimulationTime() {
        return simulator.getSimulationTime();
    }
}

