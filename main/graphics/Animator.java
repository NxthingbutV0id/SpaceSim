/* Class: Animator
 * Author: Christian Torres
 * Created: 2023/3/23
 * Modified:
 *
 * Purpose: Runs the animation logic and draws the pictures
 *
 * Attributes: -lastTime: long
               -timer: double
               -simulator: SimulationSolver
               -lastSimTime: int
               -simUPS: double
               -canvas: Canvas
               -scale: double
               -timeScale: double
               -scene: Scene
 *
 * Methods: +Animator(Canvas, Scene, double, SimulationSolver, double): this
 *          +handle(long): void
 *          -update(): void
 *          -draw(): void
 *          -drawBodies(GraphicsContext): void
 *          -drawText(GraphicsContext): void
 */
package main.graphics;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import main.customUtils.Vec2;
import main.simulation.CelestialBody;
import main.simulation.SimulationSolver;

public class Animator extends AnimationTimer {
    private long lastTime = System.nanoTime();
    private double timer = 0;
    private final SimulationSolver simulator;
    private int lastSimTime;
    private double simUPS;
    private final Canvas canvas;
    private double scale;
    private double timeScale;
    private final Scene scene;
    private boolean paused;
    private Vec2 camera;

    public Animator(Canvas canvas, Scene scene, double scale, SimulationSolver simulator, double timeScale) {
        this.simulator = simulator;
        lastSimTime = simulator.getSimulationTime();
        this.canvas = canvas;
        this.scale = scale;
        this.timeScale = timeScale;
        this.scene = scene;
        paused = true;
        camera = new Vec2(0, 0);
    }

    @Override
    public void handle(long currentTime) {
        double deltaT = (currentTime - lastTime) / 1e9;
        timer += deltaT;

        if (!paused) {
            simulator.update(deltaT / timeScale, 60);
        }

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
            if (event.getCode() == KeyCode.SPACE) {
                paused = !paused;
            }
            if (event.getCode() == KeyCode.W) {
                camera.incrementBy(new Vec2(0, -4/scale));
            } else if (event.getCode() == KeyCode.A) {
                camera.incrementBy(new Vec2(-4/scale, 0));
            } else if (event.getCode() == KeyCode.S) {
                camera.incrementBy(new Vec2(0, 4/scale));
            } else if (event.getCode() == KeyCode.D) {
                camera.incrementBy(new Vec2(4/scale, 0));
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
            body.drawBody(gc, scale, canvas.getWidth(), canvas.getHeight(), camera);
        }
    }

    private void drawText(GraphicsContext gc) {
        String str;

        gc.setFill(Color.WHITE);
        if (timer >= 1) {
            simUPS = (simulator.getSimulationTime() - lastSimTime)/timer;
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

        for (CelestialBody body : simulator.getBodies()) {
            body.drawBodyText(gc, scale, canvas.getWidth(), canvas.getHeight(), camera);
        }
    }
}