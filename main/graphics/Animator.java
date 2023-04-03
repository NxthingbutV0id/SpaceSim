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
    private Canvas canvas;
    private double scale;
    private double timeScale;
    private boolean paused;
    private Vec2 camera;
    private boolean lockOn;
    private Vec2 target;
    private Logger logger = LoggerFactory.getLogger(Animator.class);
    private SimulationHandler simulationHandler;
    private InputHandler inputHandler;
    private Renderer renderer;

    public Animator(Canvas canvas, Scene scene, double scale, double timeScale) {
        this.canvas = canvas;
        this.scale = scale;
        this.timeScale = timeScale;
        paused = true;
        camera = new Vec2(0, 0);
        target = camera;
        simulationHandler = new SimulationHandler(this);
        inputHandler = new InputHandler(scene);
        renderer = new Renderer(canvas);
    }

    @Override
    public void handle(long currentTime) {
        double deltaT = (currentTime - lastTime) / 1e9;
        timer += deltaT;
        if (!paused) {
            simulationHandler.update(deltaT / timeScale, 100);
        }
        update();
        draw();
        lastTime = currentTime;
    }

    private void update() {
        inputHandler.handleInput(this);

        if (timer >= 0.01) {
            for (CelestialBody body : simulationHandler.getBodies()) {
                body.addToPath();
            }
            timer -= 0.01;
        }
    }

    public void setTarget(MouseEvent mouse) {
        for (CelestialBody body : simulationHandler.getBodies()) {
            Vec2 bodyScreenPos = body.getScreenPosition(scale, canvas.getWidth(), canvas.getHeight(), camera);
            double bodyScreenRadius = body.getRadius() * scale;
            if (mouse.getX() <= bodyScreenPos.getX() + bodyScreenRadius + 10 && mouse.getX() >= bodyScreenPos.getX() + bodyScreenRadius - 10) {
                if (mouse.getY() <= bodyScreenPos.getY() + 10 && mouse.getY() >= bodyScreenPos.getY() - 10) {
                    logger.info("Target body: {}", body.getName());
                    target = body.getPosition();
                    lockOn = true;
                }
            }
        }
    }

    private void draw() {
        renderer.draw(simulationHandler, scale, timeScale, camera);
    }

    public double getScale() {
        return scale;
    }

    public Vec2 getCamera() {
        return camera;
    }

    public double getTimeScale() {
        return timeScale;
    }

    public boolean isLockOn() {
        return lockOn;
    }

    public boolean isPaused() {
        return paused;
    }

    public Vec2 getTarget() {
        return target;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public void setCamera(Vec2 camera) {
        this.camera = camera;
    }

    public void setTimeScale(double timeScale) {
        this.timeScale = timeScale;
    }

    public void setLockOn(boolean lockOn) {
        this.lockOn = lockOn;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
}