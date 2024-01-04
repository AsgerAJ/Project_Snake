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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    public static int boardHeight = 17;
    public static int boardWidth = 17;
    public static int tileSize = 28;
    public static Pane root = new Pane();
    public static Snake snake1 = new Snake(Integer.valueOf(boardWidth / 2), Integer.valueOf(boardHeight / 2),
            boardWidth, boardHeight);

    public static void main(String[] args) {
        Application.launch(args);
    }

    public static void DrawSnake(Snake snake, Pane root, int tileSize) {
        root.getChildren().remove(boardHeight * boardWidth);
        for (int i = 0; i < snake.getLength() - 1; i++) {
            Rectangle snakePart = new Rectangle(10 + tileSize * snake.getPoint(i).getX(),
                    10 + tileSize * snake.getPoint(i).getY(), tileSize, tileSize);
            snakePart.setFill(Color.rgb(255, 255, 255));
            snakePart.setStroke(Color.rgb(0, 0, 0));
            root.getChildren().add(snakePart);
        }
    }

    public static void DrawBoard(Pane root, int boardWidth, int boardHeight, int tileSize) {
        for (int i = 0; i < boardWidth; i++) {
            for (int j = 0; j < boardHeight; j++) {
                Rectangle tile = new Rectangle(10 + tileSize * i, 10 + tileSize * j, tileSize, tileSize);
                if (i % 2 == 0 && j % 2 == 0 || ((i % 2 != 0) && (j % 2 != 0))) {
                    tile.setFill(Color.rgb(136, 91, 242));
                } else {
                    tile.setFill(Color.rgb(109, 74, 191));
                }

                root.getChildren().add(tile);
            }
        }
        Rectangle tile = new Rectangle(10 + tileSize * boardWidth, 10 + tileSize * boardHeight, tileSize, tileSize);
        tile.setFill(Color.rgb(200, 100, 50));
        tile.setStroke(Color.rgb(0, 0, 0));
        root.getChildren().add(tile);
    }

    public static void run() {
        Platform.runLater(() -> {
            snake1.update(snake1.gDirection());
            DrawSnake(snake1, root, tileSize);
        });
    }

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(width, height);
        DrawBoard(root, boardWidth, boardHeight, tileSize);
        root.getChildren().add(canvas);
        Runnable game = () -> {
            try {
                while (true) {
                    run();
                    Thread.sleep(100);
                }

            } catch (InterruptedException ie) {
            }
        };
        Scene scene = new Scene(root, 500, 500);

        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            KeyCode code = event.getCode();
            if (code == KeyCode.UP) {
                if (snake1.gDirection() != Direction.DOWN) {
                    snake1.setDirection(Direction.UP);
                }
            } else if (code == KeyCode.DOWN) {
                if (snake1.gDirection() != Direction.UP) {
                    snake1.setDirection(Direction.DOWN);
                }
            } else if (code == KeyCode.LEFT) {
                if (snake1.gDirection() != Direction.RIGHT) {
                    snake1.setDirection(Direction.LEFT);
                }
            } else if (code == KeyCode.RIGHT) {
                if (snake1.gDirection() != Direction.LEFT) {
                    snake1.setDirection(Direction.RIGHT);
                }
            }

        });

        primaryStage.setTitle("Snake");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();

        Thread gameThread = new Thread(game);
        gameThread.setDaemon(true);
        gameThread.start();

    }
}