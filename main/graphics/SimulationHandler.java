/* Class: SimulationHandler
 * Author: Christian Torres
 * Date: 4/6/2023
 *
 * Purpose: handles the simulation
 *
 * Attributes:
 * -simulator: SimulationSolver
 *
 * Methods:
 * +SimulationHandler(Animator, String): Constructor
 * +update(double, int): void
 * +getBodies(): List<CelestialBody>
 */
package main.graphics;

import main.simulation.*;
import main.simulation.bodies.*;
import java.util.List;

public class SimulationHandler {
    private SimulationSolver simulator;

    public SimulationHandler(Animator animator, String path) {
        simulator = new SimulationSolver(animator);
        simulator.createBodies(path);
    }

    public void update(double deltaT, int subDivisions) {simulator.update(deltaT, subDivisions);}
    public List<CelestialBody> getBodies() {return simulator.getBodies();}
    public boolean isError() {return simulator.isError();}
}

