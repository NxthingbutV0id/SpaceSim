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

public class SimulationManager extends Application {
    private double width = 1280;
    private double height = 720;
    private Animator animator;
    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        initialize();
    }

    private void initialize() {
        double timeScale = 1.0;
        double scale = 1.0;
        int subDivisions = 1 << 10;

        BorderPane root = new BorderPane();
        Scene mainScene = new Scene(root);
        Canvas canvas = new Canvas(width, height);

        root.getChildren().add(canvas);
        animator = new Animator(stage, canvas, mainScene, scale, timeScale, subDivisions);

        setStage(mainScene);
    }

    private void setStage(Scene mainScene) {
        String version = "0.9.5";
        stage.setTitle("System Simulation V" + version);

        stage.setMinHeight(height);
        stage.setMinWidth(width);
        stage.setResizable(false);
        stage.setScene(mainScene);

        animator.start();
        stage.show();
    }
}
