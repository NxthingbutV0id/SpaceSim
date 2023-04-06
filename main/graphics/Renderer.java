/* Class: Renderer
 * Author: Christian Torres
 * Date: 4/6/2023
 *
 * Purpose: draws graphics to the screen
 *
 * Attributes: TBD
 *
 * Methods: TBD
 */
package main.graphics;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import main.customUtils.Vec2;
import main.simulation.bodies.CelestialBody;

public class Renderer {
    private Canvas canvas;

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
        gc.setFill(Color.WHITE);
        gc.fillText("Zoom level: " + scale, 50.0, 120.0);
        gc.fillText("Time scale: " + 1/timeScale + "x real time", 50.0, 140.0);

        for (CelestialBody body : simulationHandler.getBodies()) {
            body.drawBodyText(gc, scale, canvas.getWidth(), canvas.getHeight(), camera);
        }
    }
}

