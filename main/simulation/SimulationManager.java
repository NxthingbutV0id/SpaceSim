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
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;
import javafx.stage.*;
import main.graphics.Animator;

public class SimulationManager extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        double width = 1280, height = 720, timeScale = 1.0, scale = 1.0;
        int subDivisions = 10000;

        stage.setTitle("System Simulation V0.2.1");
        stage.setMinHeight(height);
        stage.setMinWidth(width);
        stage.setResizable(false);

        StackPane root = new StackPane();
        Scene mainScene = new Scene(root, Color.BLACK);
        stage.setScene(mainScene);

        Screen mainScreen = Screen.getScreens().get(0);

        Canvas canvas = new Canvas(width, height);
        root.getChildren().add(canvas);

        Animator animator = new Animator(canvas, mainScene, scale, timeScale, subDivisions);
        animator.start();

        stage.show();
    }
}
