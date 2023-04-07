/* Class: SimulationHandler
 * Author: Christian Torres
 * Date: 4/6/2023
 *
 * Purpose: handles the simulation
 *
 * Attributes: *TO BE FINALIZED*
 *
 * Methods: *TO BE FINALIZED*
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

    public void update(double deltaT, int subDivisions) {
        simulator.update(deltaT, subDivisions);
    }

    public List<CelestialBody> getBodies() {
        return simulator.getBodies();
    }

    public int getSimulationTime() {
        return simulator.getSimulationTime();
    }
}

