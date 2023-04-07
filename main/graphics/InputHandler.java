/* Class: InputHandler
 * Author: Christian Torres
 * Date: 4/6/2023
 *
 * Purpose: handles keyboard input
 *
 * Attributes: *TO BE FINALIZED*
 *
 * Methods: *TO BE FINALIZED*
 */
package main.graphics;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import main.customUtils.Vec2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;

public class InputHandler {
    private Scene scene;
    private HashMap<String, KeyCode> controls = new HashMap<>();
    private Logger logger = LoggerFactory.getLogger(InputHandler.class);

    public InputHandler(Scene scene) {
        this.scene = scene;
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

    public void handleInput(Animator animator) {
        double cameraMoveSpeed = 4/animator.getScale();
        scene.setOnKeyPressed(event -> {
            double timeScale = animator.getTimeScale();
            Vec2 camera = animator.getCamera();
            boolean lockOn = animator.isLockOn();
            boolean paused = animator.isPaused();
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
            animator.setTimeScale(timeScale);
            animator.setPaused(paused);
            animator.setLockOn(lockOn);
        });
        scene.setOnMousePressed(event -> {
            animator.setTarget(event);
            if (animator.isLockOn()) {
                animator.setCamera(animator.getTarget().getPosition());
            }
        });
        scene.setOnScroll(event -> {
            double scale = animator.getScale();
            if (event.getDeltaY() > 1) {
                scale *= 1.1;
            } else if (event.getDeltaY() < 1) {
                scale *= 0.9;
            }
            animator.setScale(scale);
        });
    }
}

