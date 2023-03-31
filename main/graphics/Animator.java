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
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import main.customUtils.Vec2;
import main.simulation.bodies.CelestialBody;
import main.simulation.SimulationSolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class Animator extends AnimationTimer {
    private long lastTime = System.nanoTime();
    private double timer = 0;
    private SimulationSolver simulator;
    private int lastSimTime;
    private double simUPS;
    private Canvas canvas;
    private double scale;
    private double timeScale;
    private Scene scene;
    private boolean paused;
    private Vec2 camera;
    private boolean lockOn;
    private Vec2 target;
    private HashMap<String, KeyCode> controls = new HashMap<>();
    private Logger logger = LoggerFactory.getLogger(Animator.class);

    public Animator(Canvas canvas, Scene scene, double scale, double timeScale) {
        simulator = new SimulationSolver(this);
        simulator.createBodies();
        lastSimTime = simulator.getSimulationTime();
        this.canvas = canvas;
        this.scale = scale;
        this.timeScale = timeScale;
        this.scene = scene;
        paused = true;
        camera = new Vec2(0, 0);
        target = camera;
        setControls();
    }

    private void setControls() {
        controls.put("zoom in", KeyCode.UP);
        controls.put("zoom out", KeyCode.DOWN);
        controls.put("speed up time", KeyCode.RIGHT);
        controls.put("slow down time", KeyCode.LEFT);
        controls.put("move up", KeyCode.W);
        controls.put("move down", KeyCode.S);
        controls.put("move left", KeyCode.A);
        controls.put("move right", KeyCode.D);
        controls.put("pause", KeyCode.SPACE);
        controls.put("deselect target", KeyCode.ESCAPE);
    }

    @Override
    public void handle(long currentTime) {
        double deltaT = (currentTime - lastTime) / 1e9;
        timer += deltaT;

        if (!paused) {
            simulator.update(deltaT / timeScale, 100);
        }

        update();

        draw();

        lastTime = currentTime;
    }

    private void update() {
        double cameraMoveSpeed = 4/scale;
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == controls.get("speed up time")) {
                timeScale *= 1.1;
            } else if (event.getCode() == controls.get("slow down time")) {
                timeScale *= 0.9;
            }
            if (event.getCode() == controls.get("move up")) {
                camera.incrementBy(new Vec2(0, -cameraMoveSpeed));
            } else if (event.getCode() == controls.get("move down")) {
                camera.incrementBy(new Vec2(0, cameraMoveSpeed));
            }
            if (event.getCode() == controls.get("move left")) {
                camera.incrementBy(new Vec2(-cameraMoveSpeed, 0));
            } else if (event.getCode() == controls.get("move right")) {
                camera.incrementBy(new Vec2(cameraMoveSpeed, 0));
            }
            if (event.getCode() == controls.get("pause")) {
                paused = !paused;
            }
            if (event.getCode() == controls.get("deselect target")) {
                logger.info("Escape pressed");
                lockOn = false;
            }

        });
        scene.setOnMousePressed(event -> {
            setTarget(event);
            if (lockOn) {
                camera = target;
            }
        });
        scene.setOnScroll(event -> {
            if (event.getDeltaY() > 1) {
                scale *= 1.1;
            } else if (event.getDeltaY() < 1) {
                scale *= 0.9;
            }
        });
    }

    private void setTarget(MouseEvent mouse) {
        for (CelestialBody body : simulator.getBodies()) {
            Vec2 bodyScreenPos = body.getScreenPosition(scale, canvas.getWidth(), canvas.getHeight(), camera);
            double bodyScreenRadius = body.getRadius() * scale;
            if (mouse.getX() <= bodyScreenPos.getX() + bodyScreenRadius + 10 && mouse.getX() >= bodyScreenPos.getX() + bodyScreenRadius - 10) {
                if (mouse.getY() <= bodyScreenPos.getY() + 10 && mouse.getY() >= bodyScreenPos.getY() - 10) {
                    logger.info("Target body: {}");
                    target = body.getPosition();
                    lockOn = true;
                }
            }
        }
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
            body.drawBodyPath(gc, scale, canvas.getWidth(), canvas.getHeight(), camera);
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