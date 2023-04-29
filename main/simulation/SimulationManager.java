/* Class: SimulationManager
 * Author: Christian Torres
 * Date: 2023/3/13
 *
 * Purpose: This class is responsible for setting up and managing
 *          the JavaFX application for the celestial system simulation.
 *          It initializes the stage, scene, and canvas, and starts the Animator.
 *
 * Attributes:
 * -width: double
 * -height: double
 * -animator: Animator
 * -stage: Stage
 *
 * Methods:
 * +start(Stage): void
 * -initialize(): void
 * -setStage(Scene): void
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
        int subDivisions = 2000;

        BorderPane root = new BorderPane();
        Scene mainScene = new Scene(root);
        Canvas canvas = new Canvas(width, height);

        root.getChildren().add(canvas);
        animator = new Animator(stage, canvas, mainScene, scale, timeScale, subDivisions);

        setStage(mainScene);
    }

    private void setStage(Scene mainScene) {
        String version = "1.0.0";
        stage.setTitle("WorldSim V" + version);

        stage.setHeight(height);
        stage.setWidth(width);
        stage.setResizable(false);
        stage.setScene(mainScene);

        animator.start();
        stage.show();
    }
}
