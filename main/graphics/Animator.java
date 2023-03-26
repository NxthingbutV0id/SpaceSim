package main.graphics;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import main.simulation.CelestialBody;
import main.simulation.SimulationSolver;

public class Animator extends AnimationTimer {
    private long lastTime = System.nanoTime();
    private double timer = 0;
    private final SimulationSolver simulator;
    private int lastSimTime, simUPS;
    private final Canvas canvas;
    private double scale;
    private double timeScale;
    private final Scene scene;

    public Animator(Canvas canvas, Scene scene, double scale, SimulationSolver simulator, double timeScale) {
        this.simulator = simulator;
        lastSimTime = simulator.getSimulationTime();
        this.canvas = canvas;
        this.scale = scale;
        this.timeScale = timeScale;
        this.scene = scene;
    }

    @Override
    public void handle(long currentTime) {
        double deltaT = (currentTime - lastTime) / 1e9;
        timer += deltaT;

        simulator.update(deltaT/timeScale, 10000);

        update();

        draw();

        lastTime = currentTime;
    }

    private void update() {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.UP) {
                scale *= 1.1;
            } else if (event.getCode() == KeyCode.DOWN) {
                scale *= 0.9;
            }
            if (event.getCode() == KeyCode.LEFT) {
                timeScale *= 0.9;
            } else if (event.getCode() == KeyCode.RIGHT) {
                timeScale *= 1.1;
            }
        });
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFont(new Font("FiraCode NF", 20));

        drawBodies(gc);
        drawText(gc);
    }

    private void drawBodies(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (CelestialBody body : simulator.getBodies()) {
            body.drawBody(gc, scale, canvas.getWidth(), canvas.getHeight(), simulator.getCenterOfMass());
        }
    }

    private void drawText(GraphicsContext gc) {
        String str;

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
        gc.fillText("Zoom level: " + scale, 50.0, 70.0);
        gc.fillText("Time scale: " + 1/timeScale + "x real time", 50.0, 90.0);
    }
}