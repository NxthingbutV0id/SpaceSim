/* Class: Animator
 * Author: Christian Torres
 * Date: 2023/3/23
 *
 * Purpose: The Animator class is responsible for managing the animation loop and rendering of the
 *          simulation. It also handles input and camera movement.
 *
 * Attributes:
 * -lastTime: long
 * -timer: double
 * -canvas: Canvas
 * -scale: double
 * -timeScale: double
 * -paused: boolean
 * -camera: Vec2
 * -lockOn: boolean
 * -target: CelestialBody
 * -simulationHandler: SimulationHandler
 * -inputHandler: InputHandler
 * -renderer: Renderer
 * -subDivisions: int
 * -path: String
 * -stage: Stage
 * -frameRate: double
 *
 * Methods:
 * +handle(long): void
 * +update(double): void
 * +setTarget(MouseEvent): void
 * +draw(): void
 * +getScale(): double
 * +getCamera(): Vec2
 * +getTimeScale(): double
 * +isLockOn(): boolean
 * +isPaused(): boolean
 * +getTarget(): CelestialBody
 * +setScale(double): void
 * +setTimeScale(double): void
 * +setPaused(boolean): void
 * +setPath(String): void
 * +restart(): void
 * +getStage(): Stage
 * +getPath(): String
 * +escapePressed(): boolean
 */
package main.graphics;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import main.utils.Vec2;
import main.simulation.bodies.CelestialBody;
import main.simulation.bodies.Terrestrial;

public class Animator extends AnimationTimer {
    private long lastTime = System.nanoTime();
    private double timer = 0;
    private Canvas canvas;
    private double scale;
    private double timeScale;
    private boolean paused;
    private Vec2 camera;
    private boolean lockOn;
    private CelestialBody target = new Terrestrial(null, Double.NaN, Double.NaN, new Vec2(0,0), null);
    private SimulationHandler simulationHandler;
    private InputHandler inputHandler;
    private Renderer renderer;
    private int subDivisions;
    private String path;
    private Stage stage;
    private double frameRate;

    public Animator(Stage stage, Canvas canvas, Scene scene, double scale, double timeScale, int subDivisions) {
        this.stage = stage;
        this.canvas = canvas;
        this.scale = scale;
        this.timeScale = timeScale;
        this.subDivisions = subDivisions;
        path = "main/files/ExampleSystems/Default.json";
        paused = true;
        camera = new Vec2(0, 0);
        target.setPosition(camera);
        simulationHandler = new SimulationHandler(this, path);
        inputHandler = new InputHandler(scene, this);
        renderer = new Renderer(canvas, this);
    }

    @Override
    public void handle(long currentTime) {
        double deltaT = (currentTime - lastTime) / 1e9;
        lastTime = currentTime;
        timer += deltaT;
        frameRate = 1/deltaT;
        if (!paused) {
            simulationHandler.update(deltaT/timeScale, subDivisions);
        }
        update(deltaT);
        draw();
    }

    private void update(double deltaT) {
        inputHandler.handleInput(deltaT);
        if (timer >= 1.0/60.0) {
            for (CelestialBody body : simulationHandler.getBodies()) {
                body.addToPath();
            }
            timer -= 1.0/60.0;
        }
    }

    public void setTarget(MouseEvent mouse) {
        if (!lockOn) {
            for (CelestialBody body : simulationHandler.getBodies()) {
                Vec2 bodyScreenPos = body.getScreenPosition(scale, canvas.getWidth(), canvas.getHeight(), camera);
                double bodyScreenRadius = body.getRadius() * scale;

                Vec2 mousePos = new Vec2(mouse.getX(), mouse.getY());

                if (mousePos.distance(bodyScreenPos) <= 2 * bodyScreenRadius) {
                    target = body;
                    lockOn = true;
                    break;
                }
            }
        } else {
            Vec2 mousePos = new Vec2(mouse.getX(), mouse.getY());
            Vec2 center = new Vec2(canvas.getWidth()/2, canvas.getHeight()/2);

            if (mousePos.distance(center) > 100) {
                double newX, newY;
                newX = target.getPosition().getX();
                newY = target.getPosition().getY();
                target.setPosition(new Vec2(newX, newY));
                lockOn = false;
            }
        }
    }

    private void draw() {
        if (lockOn) {
            camera = target.getPosition();
        }
        renderer.draw(simulationHandler, scale, timeScale, camera, frameRate);
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

    public void setTimeScale(double timeScale) {
        this.timeScale = timeScale;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void setPath(String path) {
        paused = true;
        this.path = path;
        simulationHandler = new SimulationHandler(this, path);
    }

    public void restart() {
        simulationHandler = new SimulationHandler(this, path);
        paused = true;
        lockOn = false;
        camera.set(0, 0);
    }

    public Stage getStage() {
        return stage;
    }

    public String getPath() {
        return path;
    }

    public boolean escapePressed() {
        return inputHandler.escapePressed();
    }
}