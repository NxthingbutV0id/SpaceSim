/* Class: InputHandler
 * Author: Christian Torres
 * Date: 2023/4/6
 *
 * Purpose: The InputHandler class is responsible for handling user input from the keyboard and mouse in the simulation.
 *
 * Attributes:
 * -scene: Scene
 * -keyPressed: HashMap<String, BooleanProperty>
 * -cameraVel: Vec2
 * -animator: Animator
 *
 * Methods:
 * +setControls(): void
 * +moveCamera(double): void
 * +handleInput(double): void
 * +update(double): void
 * +keyPressed(KeyEvent): void
 * +keyReleased(KeyEvent): void
 * +pauseMenuClick(MouseEvent): void
 * +scrollEvent(ScrollEvent): void
 * +mouseEvent(MouseEvent): void
 * +escapePressed(): boolean
 */
package main.graphics;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.FileChooser;
import main.utils.Vec2;

import java.io.File;
import java.util.HashMap;

public class InputHandler {
    private Scene scene;
    private HashMap<String, BooleanProperty> keyPressed = new HashMap<>();
    private Vec2 cameraVel = new Vec2();
    private Animator animator;

    public InputHandler(Scene scene, Animator animator) {
        this.scene = scene;
        this.animator = animator;
        setControls();
    }

    private void setControls() {
        keyPressed.put("time speed up", new SimpleBooleanProperty(false));
        keyPressed.put("time speed down", new SimpleBooleanProperty(false));
        keyPressed.put("move up", new SimpleBooleanProperty(false));
        keyPressed.put("move left", new SimpleBooleanProperty(false));
        keyPressed.put("move down", new SimpleBooleanProperty(false));
        keyPressed.put("move right", new SimpleBooleanProperty(false));
        keyPressed.put("pause", new SimpleBooleanProperty(false));
        keyPressed.put("escape", new SimpleBooleanProperty(false));
    }

    private void moveCamera(double deltaT) {
        Vec2 cameraAcc = new Vec2();
        if (keyPressed.get("move up").get()) {
            cameraAcc.incrementBy(new Vec2(0, -1));
        } else if (keyPressed.get("move down").get()) {
            cameraAcc.incrementBy(new Vec2(0, 1));
        } else {
            cameraVel.setY(cameraVel.getY() * 0.9);
        }
        if (keyPressed.get("move left").get() && !keyPressed.get("move right").get()) {
            cameraAcc.incrementBy(new Vec2(-1, 0));
        } else if (keyPressed.get("move right").get() && !keyPressed.get("move left").get()) {
            cameraAcc.incrementBy(new Vec2(1, 0));
        } else {
            cameraVel.setX(cameraVel.getX() * 0.9);
        }
        cameraAcc.selfNormalize();
        double maxSpeed = 200 * (1/animator.getScale());

        cameraVel.incrementBy(cameraAcc.multiply(1000 / animator.getScale()).multiply(deltaT));

        if (cameraVel.getMagnitude() > maxSpeed) {
            cameraVel.selfNormalize();
            cameraVel.selfMultiply(maxSpeed);
        }

        animator.getCamera().incrementBy(cameraVel.multiply(deltaT));
    }

    public void handleInput(double deltaT) {
        scene.setOnKeyPressed(this::keyPressed);
        scene.setOnKeyReleased(this::keyReleased);
        scene.setOnMousePressed(this::mouseEvent);
        scene.setOnScroll(this::scrollEvent);
        update(deltaT);
    }

    private void update(double deltaT) {
        double timeScale = animator.getTimeScale();
        boolean paused = animator.isPaused();
        if (keyPressed.get("time speed up").get()) {
            timeScale *= 1.1;
            animator.setTimeScale(timeScale);
        } else if (keyPressed.get("time speed down").get()) {
            timeScale *= 0.9;
            animator.setTimeScale(timeScale);
        }
        if (!animator.isLockOn()) {
            moveCamera(deltaT);
        }
        animator.setPaused(!keyPressed.get("pause").get());
    }

    private void keyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case DOWN -> keyPressed.get("time speed up").set(true);
            case UP -> keyPressed.get("time speed down").set(true);
            case W -> keyPressed.get("move up").set(true);
            case A -> keyPressed.get("move left").set(true);
            case S -> keyPressed.get("move down").set(true);
            case D -> keyPressed.get("move right").set(true);
            case SPACE -> keyPressed.get("pause").set(!keyPressed.get("pause").get());
            case ESCAPE -> keyPressed.get("escape").set(!keyPressed.get("escape").get());
        }
    }

    private void keyReleased(KeyEvent event) {
        switch (event.getCode()) {
            case DOWN -> keyPressed.get("time speed up").set(false);
            case UP -> keyPressed.get("time speed down").set(false);
            case W -> keyPressed.get("move up").set(false);
            case A -> keyPressed.get("move left").set(false);
            case S -> keyPressed.get("move down").set(false);
            case D -> keyPressed.get("move right").set(false);
        }
    }

    private void pauseMenuClick(MouseEvent event) {
        if (event.getX() >= 50 && event.getX() <= 200 && event.getY() >= 250 && event.getY() <= 300) {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"));
            File file = fc.showOpenDialog(animator.getStage());
            if (file != null) {
                animator.setPath(file.getPath());
            }
        } else if (event.getX() >= 50 && event.getX() <= 200 && event.getY() >= 350 && event.getY() <= 400) {
            animator.restart();
        }
    }

    private void scrollEvent(ScrollEvent event) {
        double scale = animator.getScale();
        if (event.getDeltaY() > 1) {
            scale *= 1.1;
        } else if (event.getDeltaY() < 1) {
            scale *= 0.9;
        }
        animator.setScale(scale);
    }

    private void mouseEvent(MouseEvent event) {
        if (escapePressed()) {
            pauseMenuClick(event);
        } else {
            animator.setTarget(event);
        }
    }

    public boolean escapePressed() {
        return keyPressed.get("escape").get();
    }
}

