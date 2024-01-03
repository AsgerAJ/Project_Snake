package src.gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
        root.setPrefSize(500, 500);

        Scene scene = new Scene(root, width, height);

        Rectangle myRectangle = new Rectangle(10, 10, 50, 50);
        //myRectangle.setStroke(Color.rgb(200, 100, 50));
        myRectangle.setFill(Color.rgb(200, 100, 50));

        root.getChildren().add(myRectangle);
        
        primaryStage.setTitle("Snake");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);

        primaryStage.show();
    }
}