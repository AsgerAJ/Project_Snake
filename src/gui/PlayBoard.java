package src.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import src.backend.Snake;
import src.gui.Main;

public class PlayBoard extends Application {

    int width = 700;
    int height = 700;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        StackPane root = new StackPane();
        Canvas canvas = new Canvas(width, height);

        root.getChildren().add(canvas);

        Scene scene = new Scene(root);

        primaryStage.setTitle("Snake");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

}