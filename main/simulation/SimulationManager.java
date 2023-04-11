/* Class: SimulationManager
 * Author: Christian Torres
 * Created: 2023/3/13
 * Modified:
 *
 * Purpose: The sets up the window and the application
 *
 * Attributes:
 *
 * Methods: +start(Stage): void
 */
package main.simulation;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.stage.*;
import main.graphics.Animator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimulationManager extends Application {
    private final String version = "0.8.0";
    private double width = 1280, height = 720, timeScale = 1.0, scale = 1.0;
    private int subDivisions = 10000;
    private Animator animator;
    private Stage stage;
    private Logger logger = LoggerFactory.getLogger(SimulationManager.class);

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        initialize();
    }

    private void initialize() {
        BorderPane root = new BorderPane();
        Scene mainScene = new Scene(root);
        Canvas canvas = new Canvas(width, height);
        root.getChildren().add(canvas);
        animator = new Animator(stage, canvas, mainScene, scale, timeScale, subDivisions);

        stage.setTitle("System Simulation V" + version);
        stage.setMinHeight(height);
        stage.setMinWidth(width);
        stage.setResizable(false);
        stage.setScene(mainScene);
        animator.start();
        stage.show();
    }
}
