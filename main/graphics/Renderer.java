/* Class: Renderer
 * Author: Christian Torres
 * Date: 2023/4/6
 *
 * Purpose: The Renderer class is responsible for drawing the simulation's celestial bodies and UI elements onto the canvas.
 *
 * Attributes:
 * -canvas: Canvas
 * -animator: Animator
 * -gc: GraphicsContext
 * -screenWidth: double
 *
 * Methods:
 * +draw(SimulationHandler, double, double, Vec2, double): void
 * +drawBodies(SimulationHandler, double, Vec2): void
 * +drawText(double, double, double): void
 * +drawBodyText(SimulationHandler, double, Vec2): void
 * +drawPauseMenu(): void
 * +roundTwo(double): double
 */
package main.graphics;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import main.simulation.bodies.GasGiant;
import main.utils.Vec2;
import main.simulation.bodies.CelestialBody;

import java.nio.file.Path;

public class Renderer {
    private Canvas canvas;
    private Animator animator;
    private GraphicsContext gc;
    private double screenWidth;

    public Renderer(Canvas canvas, Animator animator) {
        this.canvas = canvas;
        this.animator = animator;
        screenWidth = canvas.getWidth();
    }

    public void draw(SimulationHandler simulationHandler, double scale, double timeScale, Vec2 camera, double fps) {
        gc = canvas.getGraphicsContext2D();
        drawBodies(simulationHandler, scale, camera);
        drawBodyText(simulationHandler, scale, camera);
        if (animator.escapePressed()) {
            drawPauseMenu();
        } else {
            drawText(scale, timeScale, fps);
        }
    }

    private void drawBodies(SimulationHandler simulationHandler, double scale, Vec2 camera) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (CelestialBody body : simulationHandler.getBodies()) {
            if (body instanceof GasGiant && ((GasGiant) body).hasRings()) {
                ((GasGiant) body).getRings().getGraphics().drawRing(gc, scale, canvas.getWidth(), canvas.getHeight(), camera);
            }
            body.getOrbitGraphics().drawOrbit(gc, scale, canvas.getWidth(), canvas.getHeight(), camera);
            body.getBodyGraphics().drawBody(gc, scale, canvas.getWidth(), canvas.getHeight(), camera);
        }
    }

    private void drawText(double scale, double timeScale, double fps) {
        double initTextPos = 100.0;
        CelestialBody target = animator.getTarget();
        gc.setFill(Color.WHITE);
        Path pathFromString = Path.of(animator.getPath());
        String file = pathFromString.getFileName().toString();
        gc.fillText("Scenario: " + file, 50, 50);
        gc.fillText("FPS: " + roundTwo(fps), screenWidth - 200, 50);
        gc.fillText("Zoom level: " + scale, 50.0, initTextPos); initTextPos += 20;
        gc.fillText("Time scale: " + 1/timeScale + "x real time", 50.0, initTextPos); initTextPos += 20;
        if (animator.isLockOn()) {
            gc.fillText("Current target: " + target.getName(), 50.0, initTextPos); initTextPos += 20;
            gc.fillText("Mass: " + target.getMass() + " kg", 50.0, initTextPos); initTextPos += 20;
            gc.fillText("Radius: " + target.getRadius() + " m", 50.0, initTextPos); initTextPos += 20;
            gc.fillText("Temperature: " + roundTwo(target.getTemperature()) + " K", 50.0, initTextPos); initTextPos += 20;
            gc.fillText("Temperature: " +roundTwo(target.getTemperature() - 273.15)+ " C)"
                    , 50.0, initTextPos);
        } else {
            gc.fillText("Current target: None", 50.0, initTextPos);
        }

    }

    private void drawBodyText(SimulationHandler simulationHandler, double scale, Vec2 camera) {
        for (CelestialBody body : simulationHandler.getBodies()) {
            body.getBodyGraphics().drawBodyText(gc, scale, canvas.getWidth(), canvas.getHeight(), camera);
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

    private double roundTwo(double value) {
        return Math.round(value * 100.0)/100.0;
    }
}

