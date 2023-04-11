/* Class: Renderer
 * Author: Christian Torres
 * Date: 4/6/2023
 *
 * Purpose: draws graphics to the screen
 *
 * Attributes: *TO BE FINALIZED*
 *
 * Methods: *TO BE FINALIZED*
 */
package main.graphics;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import main.customUtils.Vec2;
import main.simulation.bodies.CelestialBody;

public class Renderer {
    private Canvas canvas;
    private Animator animator;
    private GraphicsContext gc;

    public Renderer(Canvas canvas, Animator animator) {
        this.canvas = canvas;
        this.animator = animator;
    }

    public void draw(SimulationHandler simulationHandler, double scale, double timeScale, Vec2 camera) {
        gc = canvas.getGraphicsContext2D();
        drawBodies(simulationHandler, scale, camera);
        drawBodyText(simulationHandler, scale, camera);
        if (animator.escapePressed()) {
            drawPauseMenu();
        } else {
            drawText(scale, timeScale);
        }
    }

    private void drawBodies(SimulationHandler simulationHandler, double scale, Vec2 camera) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (CelestialBody body : simulationHandler.getBodies()) {
            body.drawBody(gc, scale, canvas.getWidth(), canvas.getHeight(), camera);
            body.drawBodyPath(gc, scale, canvas.getWidth(), canvas.getHeight(), camera, animator.getTarget().getPosition());
        }
    }

    private void drawText(double scale, double timeScale) {
        double initTextPos = 100.0;
        CelestialBody target = animator.getTarget();
        gc.setFill(Color.WHITE);
        gc.fillText("Zoom level: " + scale, 50.0, initTextPos); initTextPos += 20;
        gc.fillText("Time scale: " + 1/timeScale + "x real time", 50.0, initTextPos); initTextPos += 20;
        if (animator.isLockOn()) {
            gc.fillText("Current target: " + target.getName(), 50.0, initTextPos); initTextPos += 20;
            gc.fillText("Mass: " + target.getMass() + " kg", 50.0, initTextPos); initTextPos += 20;
            gc.fillText("Radius: " + target.getRadius() + " m", 50.0, initTextPos); initTextPos += 20;
            gc.fillText("Temperature: " + target.getTemperature() + " K (" +(target.getTemperature() - 273.15)+ " C)"
                    , 50.0, initTextPos);
        } else {
            gc.fillText("Current target: None", 50.0, initTextPos);
        }

    }

    private void drawBodyText(SimulationHandler simulationHandler, double scale, Vec2 camera) {
        for (CelestialBody body : simulationHandler.getBodies()) {
            body.drawBodyText(gc, scale, canvas.getWidth(), canvas.getHeight(), camera);
        }
    }

    private void drawPauseMenu() {
        gc.setFill(Color.GRAY);
        gc.fillRect(0, 0, canvas.getWidth()/4, canvas.getHeight());

        gc.setFill(Color.BLUE);
        gc.fillRect(50, 250, 150, 50);
        gc.setFill(Color.WHITE);
        gc.fillText("Load File", 80, 280);

        gc.setFill(Color.BLUE);
        gc.fillRect(50, 350, 150, 50);
        gc.setFill(Color.WHITE);
        gc.fillText("Reload File", 75, 380);
    }
}

