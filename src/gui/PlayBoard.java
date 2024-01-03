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
import src.backend.Direction;
import src.backend.Snake;
import src.gui.Main;

public class PlayBoard extends Application {

    int width = 800;
    int height = 800;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        int tileSize = 40;
        int boardHeight = 17;
        int boardWidth = 17;

        Snake snake1 = new Snake(Integer.valueOf(boardWidth / 2), Integer.valueOf(boardHeight / 2), boardWidth, boardHeight);

        Pane root = new Pane();
        Canvas canvas = new Canvas(width, height);

        root.getChildren().add(canvas);

        Scene scene = new Scene(root, width, height);

        primaryStage.setTitle("Snake");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);

        for (int gen = 0; gen < 20; gen++) {
            snake1.update(Direction.LEFT);

            for (int i = 0; i < boardWidth; i++) {
                for (int j = 0; j < boardHeight; j++) {
                    Rectangle tile = new Rectangle(10 + tileSize * i, 10 + tileSize * j, tileSize, tileSize);
                    tile.setFill(Color.rgb(200, 100, 50));
                    tile.setStroke(Color.rgb(0, 0, 0));
                    root.getChildren().add(tile);
                }
            }

            for (int i = 0; i < snake1.getLength(); i++) {
                Rectangle snakePart = new Rectangle(10 + tileSize * snake1.getPoint(i).getX(), 10 + tileSize * snake1.getPoint(i).getY(), tileSize, tileSize);
                snakePart.setFill(Color.rgb(255, 255, 255));
                snakePart.setStroke(Color.rgb(0, 0, 0));
                root.getChildren().add(snakePart);
            }

            primaryStage.show();
        }
    }
}