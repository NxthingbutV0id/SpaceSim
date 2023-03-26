package main.simulation;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;
import javafx.stage.*;
import main.graphics.Animator;

public class SimulationManager extends Application {
    private final double width = 1280, height = 720;
    private final SimulationSolver simulator = new SimulationSolver();;
    private Canvas canvas;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("System Simulation V0.0.4");
        stage.setWidth(width);
        stage.setHeight(height);
        stage.setResizable(false);

        StackPane root = new StackPane();
        Scene mainScene = new Scene(root, Color.BLACK);
        stage.setScene(mainScene);

        canvas = new Canvas(width, height);
        root.getChildren().add(canvas);
        simulator.createBodies();

        Animator animator = new Animator(canvas, 0.2, simulator);
        animator.start();

        stage.show();
    }
}
