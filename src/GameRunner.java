import java.util.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;



public class GameRunner extends Application {

    // Public variables
    public double scalingConstant;
    public double height;
    public double width;
    public int n;
    public int m;

    // Private variables
    private Pane root;
    private Food food;
    private Snake snake;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Welcome to the snake game");

        n = 20;
        m = 20;

        root = new Pane();
        root.setPrefSize(n, m);

        drawGrid(n, m);
        food = new Food(2, 2, scalingConstant);
        drawFood(food);
        snake = new Snake(n, m, scalingConstant, Direction.Stop, 0, 2);
        drawSnake(snake);

        width = scalingConstant * n;
        height = scalingConstant * m;
        Scene scene = new Scene(root, width, height);

        Runnable snakeStepper = () -> {
            try {
                while (true) {
                    stepHandler(snake);
                    Collections.rotate(snake, 1);
                    Thread.sleep(80);
                }

            } catch (InterruptedException ie) {
            }
        };

        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            KeyCode code = event.getCode();
            KeyCode last = code;
            KeyCode last2 = last;
            switch (code) {
                case UP:
                    if (snake.getDirr() != Direction.Down) {
                        snake.setCurrentDirection(Direction.Up);
                    }
                    break;
                case DOWN:
                    if (snake.getDirr() != Direction.Up) {
                        snake.setCurrentDirection(Direction.Down);
                    }
                    break;
                case LEFT:
                    if (snake.getDirr() != Direction.Right) {
                        snake.setCurrentDirection(Direction.Left);
                    }
                    break;
                case RIGHT:
                    if (snake.getDirr() != Direction.Left) {
                        snake.setCurrentDirection(Direction.Right);
                    }
                    break;

                case SPACE:
                    snake.Grow();
                    root.getChildren().add(snake.get(snake.getLength() - 1));
                    break;

                case G:
                    snake.setCurrentDirection(Direction.Stop);
                default:
                    break;
            }
        });

        primaryStage.setTitle("Snake");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        Thread gameThread = new Thread(snakeStepper);
        gameThread.setDaemon(true);
        gameThread.start();
    }

    public void drawGrid(int x, int y) { // Colours background
        if (x > y) {
            scalingConstant = 500 / (x);
        } else {
            scalingConstant = 500 / (y);
        }
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                Rectangle back = new Rectangle(i * scalingConstant, j * scalingConstant, scalingConstant,
                        scalingConstant);
                if (((i % 2 == 0) && (j % 2 == 0)) || ((i % 2 != 0) && (j % 2 != 0))) {
                    back.setFill(Color.rgb(136, 91, 242));
                } else {
                    back.setFill(Color.rgb(109, 74, 191));
                }
                root.getChildren().add(back);
            }
        }
    }

    public void drawSnake(Snake snake) {
        root.getChildren().addAll(snake);
    }

    public void drawFood(Food food) {
        root.getChildren().add(food);
    }

    public void stepHandler(Snake snake) {
        Random rand = new Random();
        Platform.runLater(() -> {
            if (snake.selfCollide()) {
                snake.setCurrentDirection(Direction.Stop);
                // try {
                //     gameOver();
                // } catch (FileNotFoundException e) {
                //     e.printStackTrace();
                // }
            }
            if(snake.foodCollision(food)){

                int randX = rand.nextInt(n)+1;
                int randY = rand.nextInt(m)+1;
                food.setXY(randX, randY);
                eat();
            }
            snake.moveSnake(snake.getDirr());
        });
    }
    public void gameOver() throws FileNotFoundException{
        Image gameover = new Image(new FileInputStream("GameOverScreen.jpg"));
        ImageView imageView = new ImageView(gameover);
        imageView.relocate(0,0);
        Button button = new Button("RESTART");
        button.relocate(width / 2, height / 2);
        root.getChildren().addAll(imageView,button);
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent a) {
                root.getChildren().clear();
                drawGrid(n, m);
                snake = new Snake(n, m, scalingConstant, Direction.Stop, 0, 2);
                drawSnake(snake);
            }
        };
        button.setOnAction(event);
    }

    public void eat(){
        snake.Grow();
        root.getChildren().add(snake.get(snake.getLength() - 1));
    }
}