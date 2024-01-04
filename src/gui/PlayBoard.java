package src.gui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
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
import javafx.util.Duration;

public class PlayBoard extends Application {

    int width = 800;
    int height = 800;
    public static int tileSize = 28;
    public static int boardHeight = 17;
    public static int boardWidth = 17;
    public static Pane root = new Pane();
    public static Snake snake1 = new Snake(Integer.valueOf(boardWidth / 2), Integer.valueOf(boardHeight / 2), boardWidth, boardHeight);

    public static void main(String[] args) {
        Application.launch(args);
    }

    public static void DrawSnake(Snake snake, Pane root, int tileSize){
        for (int i = 0; i < snake.getLength(); i++) {
                Rectangle snakePart = new Rectangle(10 + tileSize * snake.getPoint(i).getX(), 10 + tileSize * snake.getPoint(i).getY(), tileSize, tileSize);
                snakePart.setFill(Color.rgb(255, 255, 255));
                snakePart.setStroke(Color.rgb(0, 0, 0));
                root.getChildren().add(snakePart);
        }
    }

    public static void DrawBoard(Pane root, int boardWidth, int boardHeight, int tileSize) {
        for (int i = 0; i < boardWidth; i++) {
            for (int j = 0; j < boardHeight; j++) {
                Rectangle tile = new Rectangle(10 + tileSize * i, 10 + tileSize * j, tileSize, tileSize);
                tile.setFill(Color.rgb(200, 100, 50));
                tile.setStroke(Color.rgb(0, 0, 0));
                root.getChildren().add(tile);
            }
        }
    }

    public static void run() {
        Platform.runLater(() -> {
            DrawBoard(root, boardWidth, boardHeight, tileSize);
            snake1.update(Direction.DOWN);
            DrawSnake(snake1, root, tileSize);
        });
    }

    @Override
    public void start(Stage primaryStage) {
        
        Canvas canvas = new Canvas(width, height);

        root.getChildren().add(canvas);

        Scene scene = new Scene(root, 500, 500);

        //snake1.update(Direction.LEFT);

        //DrawBoard(root, boardWidth, boardHeight, tileSize);
        //DrawSnake(snake1, root, tileSize);

        
        Runnable game = () -> {
            try {
                while (true) {
                    run();
                    Thread.sleep(100);
                }
            } catch (InterruptedException ie) {}
        };


        primaryStage.setTitle("Snake");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        Thread gameThread = new Thread(game);
        gameThread.setDaemon(true);
        gameThread.start();
        
    }
}