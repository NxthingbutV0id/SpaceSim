/* Class: SimulationManager
 * Author: Christian Torres
 * Created: 2023/3/13
 * Modified:
 *
 * Purpose: The sets up the window and the application
 *
 * Attributes: -simulator: SimulationSolver
 *             -scale: double
 *             -timeScale: double
 *
 * Methods: +start(Stage): void
 */
package main.simulation;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;
import javafx.stage.*;
import main.graphics.Animator;

public class SimulationManager extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        double width = 1280, height = 720, timeScale = 1.0 / 86400.0, scale = 5e-9;

        stage.setTitle("System Simulation V0.0.8");
        stage.setMinHeight(height);
        stage.setMinWidth(width);
        stage.setResizable(false);

        StackPane root = new StackPane();
        Scene mainScene = new Scene(root, Color.BLACK);
        stage.setScene(mainScene);

        Screen mainScreen = Screen.getScreens().get(0);

        Canvas canvas = new Canvas(width, height);
        root.getChildren().add(canvas);

        Animator animator = new Animator(canvas, mainScene, scale, timeScale);
        animator.start();

        stage.show();
    }
}
