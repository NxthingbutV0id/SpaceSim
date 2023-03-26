package main.graphics;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import main.customUtils.Vec2;
import main.simulation.CelestialBody;
import main.simulation.SimulationSolver;

public class Animator extends AnimationTimer {
    private long lastTime = System.nanoTime();
    private double timer = 0;
    private final SimulationSolver simulator;
    private int lastSimTime, simUPS;
    private final Canvas canvas;
    private final double scale;

    public Animator(Canvas canvas, double scale, SimulationSolver simulator) {
        this.simulator = simulator;
        lastSimTime = simulator.getSimulationTime();
        this.canvas = canvas;
        this.scale = scale;
    }

    @Override
    public void handle(long currentTime) {
        double deltaT = (currentTime - lastTime) / 1e9;
        double timeScale = 10000; //larger means slower
        String str;
        timer += deltaT;

        simulator.update(deltaT/5000, 10000);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFont(new Font("FiraCode NF", 20));

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (CelestialBody body : simulator.getBodies()) {
            body.drawBody(gc, scale, canvas.getWidth(), canvas.getHeight(), simulator.getCenterOfMass());
            //System.out.println("Body " + body.getName() + " position: " + body.getPosition().toString());
        }
        gc.setFill(Color.WHITE);
        if (timer >= 1) {
            simUPS = simulator.getSimulationTime() - lastSimTime;
            str = "Simulation Rate: " + simUPS + " UPS";
            gc.fillText(str, 50.0, 50.0);
            lastSimTime = simulator.getSimulationTime();
            --timer;
        } else {
            str = "Simulation Rate: " + simUPS + " UPS";
            gc.fillText(str, 50.0, 50.0);
        }

        lastTime = currentTime;
    }
}