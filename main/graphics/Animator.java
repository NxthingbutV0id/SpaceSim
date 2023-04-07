/* Class: Animator
 * Author: Christian Torres
 * Created: 2023/3/23
 * Modified:
 *
 * Purpose: Runs the animation logic and draws the pictures
 *
 * Attributes: TBD
 *
 * Methods: TBD
 */
package main.graphics;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import main.customUtils.Vec2;
import main.simulation.bodies.CelestialBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Animator extends AnimationTimer {
    private long lastTime = System.nanoTime();
    private double timer = 0;
    private Canvas canvas;
    private double scale;
    private double timeScale;
    private boolean paused;
    private Vec2 camera;
    private boolean lockOn;
    private CelestialBody target = new CelestialBody(null, Double.NaN, Double.NaN, new Vec2(0,0), null);
    private Logger logger = LoggerFactory.getLogger(Animator.class);
    private SimulationHandler simulationHandler;
    private InputHandler inputHandler;
    private Renderer renderer;
    private int subDivisions;

    public Animator(Canvas canvas, Scene scene, double scale, double timeScale, int subDivisions) {
        this.canvas = canvas;
        this.scale = scale;
        this.timeScale = timeScale;
        this.subDivisions = subDivisions;
        paused = true;
        camera = new Vec2(0, 0);
        target.setPosition(camera);
        simulationHandler = new SimulationHandler(this);
        inputHandler = new InputHandler(scene);
        renderer = new Renderer(canvas, this);
    }

    @Override
    public void handle(long currentTime) {
        double deltaT = (currentTime - lastTime) / 1e9;
        timer += deltaT;
        if (!paused) {
            simulationHandler.update(deltaT/timeScale, subDivisions);
        }
        update(deltaT);
        draw();
        lastTime = currentTime;
    }

    private void update(double deltaT) {
        inputHandler.handleInput(this);

        if (timer >= (deltaT/subDivisions)) {
            for (CelestialBody body : simulationHandler.getBodies()) {
                body.addToPath();
            }
            timer -= (deltaT/subDivisions);
        }
    }

    public void setTarget(MouseEvent mouse) {
        boolean closeOnX, closeOnY, x1, x2, y1, y2;
        if (!lockOn) {
            for (CelestialBody body : simulationHandler.getBodies()) {
                Vec2 bodyScreenPos = body.getScreenPosition(scale, canvas.getWidth(), canvas.getHeight(), camera);
                double bodyScreenRadius = body.getRadius() * scale;

                x1 = mouse.getX() <= bodyScreenPos.getX() + bodyScreenRadius + 10;
                x2 = mouse.getX() >= bodyScreenPos.getX() - bodyScreenRadius - 10;
                y1 = mouse.getY() <= bodyScreenPos.getY() + bodyScreenRadius + 10;
                y2 = mouse.getY() >= bodyScreenPos.getY() - bodyScreenRadius - 10;

                closeOnX = x1 && x2;
                closeOnY = y1 && y2;

                if (closeOnX && closeOnY) {
                    logger.info("Target body: {}", body.getName());
                    target = body;
                    lockOn = true;
                }
            }
        } else {
            x1 = mouse.getX() > canvas.getWidth()/2 + 10;
            x2 = mouse.getX() < canvas.getWidth()/2 - 10;
            y1 = mouse.getY() > canvas.getWidth()/2 + 10;
            y2 = mouse.getY() < canvas.getWidth()/2 - 10;

            closeOnX = x1 && x2;
            closeOnY = y1 && y2;

            if (closeOnX && closeOnY) {
                logger.info("Target body: None");
                double newX, newY;
                newX = target.getPosition().getX();
                newY = target.getPosition().getY();
                target.setPosition(new Vec2(newX, newY));
                lockOn = false;
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

    public CelestialBody getTarget() {
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