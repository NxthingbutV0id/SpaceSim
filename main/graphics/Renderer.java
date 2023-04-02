package main.graphics;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import main.customUtils.Vec2;
import main.simulation.bodies.CelestialBody;

public class Renderer {
    private Canvas canvas;
    private int lastSimTime;
    private double timer = 0;
    private double simUPS;

    public Renderer(Canvas canvas) {
        this.canvas = canvas;
    }

    public void draw(SimulationHandler simulationHandler, double scale, double timeScale, Vec2 camera) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawBodies(gc, simulationHandler, scale, camera);
        drawText(gc, simulationHandler, scale, timeScale, camera);
    }

    private void drawBodies(GraphicsContext gc, SimulationHandler simulationHandler, double scale, Vec2 camera) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (CelestialBody body : simulationHandler.getBodies()) {
            body.drawBody(gc, scale, canvas.getWidth(), canvas.getHeight(), camera);
            body.drawBodyPath(gc, scale, canvas.getWidth(), canvas.getHeight(), camera);
        }
    }

    private void drawText(GraphicsContext gc, SimulationHandler simulationHandler,
                          double scale, double timeScale, Vec2 camera) {
        String str;

        gc.setFill(Color.WHITE);
        if (timer >= 1) {
            simUPS = (simulationHandler.getSimulationTime() - lastSimTime)/timer;
            str = "Simulation Rate: " + simUPS + " UPS";
            gc.fillText(str, 50.0, 100.0);
            lastSimTime = simulationHandler.getSimulationTime();
            --timer;
        } else {
            str = "Simulation Rate: " + simUPS + " UPS";
            gc.fillText(str, 50.0, 100.0);
        }
        gc.fillText("Zoom level: " + scale, 50.0, 120.0);
        gc.fillText("Time scale: " + 1/timeScale + "x real time", 50.0, 140.0);

        for (CelestialBody body : simulationHandler.getBodies()) {
            body.drawBodyText(gc, scale, canvas.getWidth(), canvas.getHeight(), camera);
        }
    }
}

