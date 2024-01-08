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
    public boolean multiplayer = true;

    // Private variables
    private Pane root;
    private Food food;
    private Snake snake;
    private Snake snake2;
    //private boolean directionWasChanged = false;

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

        Random foodCord = new Random();
        drawGrid(n, m);
        food = new Food(foodCord.nextInt(n) + 1, foodCord.nextInt(m) + 1, scalingConstant);
        drawFood(food);
        snake = new Snake(n, m, scalingConstant, Direction.Stop, 0, 2, 0);
        drawSnake(snake);
        if(multiplayer) {
            snake2 = new Snake(n, m, scalingConstant, Direction.Stop, 0, 2, 2);
            drawSnake(snake2);
        }

        width = scalingConstant * n;
        height = scalingConstant * m;
        Scene scene = new Scene(root, width, height);

        Runnable snakeStepper = () -> {
            try {
                while (true) {
                    stepHandler(snake);
                    if(multiplayer) {
                        stepHandler(snake2);
                    }
                    Thread.sleep(100);
                }

            } catch (InterruptedException ie) {
            }
        };

        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            KeyCode code = event.getCode();
            snake.setTailCoords();
            if(multiplayer) {
                snake2.setTailCoords();
            }

            switch (code) {
                case UP:
                    if (snake.getAlive() && snake.getDirr() != Direction.Down
                    && !snake.getDirectionWasChanged() ) {
                        snake.setCurrentDirection(Direction.Up);
                        snake.setDirectionWasChanged(true);
                    }
                    break;
                case DOWN:
                    if (snake.getAlive() && snake.getDirr() != Direction.Up
                    && !snake.getDirectionWasChanged()) {
                        snake.setCurrentDirection(Direction.Down);
                        snake.setDirectionWasChanged(true);
                    }
                    break;
                case LEFT:
                    if (snake.getAlive() && snake.getDirr() != Direction.Right
                    && !snake.getDirectionWasChanged()) {
                        snake.setCurrentDirection(Direction.Left);
                        snake.setDirectionWasChanged(true);
                    }
                    break;
                case RIGHT:
                    if (snake.getAlive() && snake.getDirr() != Direction.Left
                    && !snake.getDirectionWasChanged()) {
                        snake.setCurrentDirection(Direction.Right);
                        snake.setDirectionWasChanged(true);
                    }
                    break;
                
                case W:
                    if (multiplayer
                    && snake2.getAlive() && !snake2.getDirectionWasChanged()
                    && snake2.getDirr() != Direction.Down) {
                        snake2.setCurrentDirection(Direction.Up);
                        snake2.setDirectionWasChanged(true);
                    }
                    break;
                case S:
                    if (multiplayer
                    && snake2.getAlive() && !snake2.getDirectionWasChanged()
                    && snake2.getDirr() != Direction.Up && snake2.getAlive()) {
                        snake2.setCurrentDirection(Direction.Down);
                        snake2.setDirectionWasChanged(true);
                    }
                    break;
                case A:
                    if (multiplayer
                    && snake2.getAlive() && !snake2.getDirectionWasChanged()
                    && snake2.getDirr() != Direction.Right && snake2.getAlive()) {
                        snake2.setCurrentDirection(Direction.Left);
                        snake2.setDirectionWasChanged(true);
                    }
                    break;
                case D:
                    if (multiplayer
                    && snake2.getAlive() && !snake2.getDirectionWasChanged()
                    && snake2.getDirr() != Direction.Left && snake2.getAlive()) {
                        snake2.setCurrentDirection(Direction.Right);
                        snake2.setDirectionWasChanged(true);
                    }
                    break;

                case SPACE:
                    snake.Grow();
                    root.getChildren().add(snake.get(snake.getLength() - 1));
                    break;

                case G:
                    snake.setCurrentDirection(Direction.Stop);
                    if(multiplayer) {
                        snake2.setCurrentDirection(Direction.Stop);
                    }
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
                Rectangle back = new Rectangle(i * scalingConstant, j * scalingConstant, scalingConstant, scalingConstant);
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
            snake.setDirectionWasChanged(false);
            snake.moveSnake(snake.getDirr());
            Collections.rotate(snake, 1);
            if (snake.selfCollide()) {
                snake.setCurrentDirection(Direction.Stop);
                snake.murder();
                // try {
                // gameOver();
                // } catch (FileNotFoundException e) {
                // e.printStackTrace();
                // }
            }else if (snake.foodCollision(food)) {
                boolean validSpawn = false;
                int randX = rand.nextInt(n);
                int randY = rand.nextInt(m);
                while (!validSpawn) {
                    validSpawn = true;
                    randX = rand.nextInt(n);
                    randY = rand.nextInt(m);
                    for (int i = 0; i < snake.getLength(); i++) {
                        if (snake.get(i).getX() / scalingConstant == randX
                        && snake.get(i).getY() / scalingConstant == randY) {
                            validSpawn = false;
                            continue;
                        }
                    }
                }
                food.setXY(randX + 1, randY + 1);
                eat(snake);
                root.getChildren().add(snake.get(snake.getLength() - 1));
            }
        });
    }

    public void gameOver() throws FileNotFoundException {
        Image gameover = new Image(new FileInputStream("GameOverScreen.jpg"));
        ImageView imageView = new ImageView(gameover);
        imageView.relocate(0, 0);
        Button button = new Button("RESTART");
        button.relocate(width / 2, height / 2);
        root.getChildren().addAll(imageView, button);
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent a) {
                root.getChildren().clear();
                drawGrid(n, m);
                snake = new Snake(n, m, scalingConstant, Direction.Stop, 0, 2, 0);
                drawSnake(snake);
            }
        };
        button.setOnAction(event);
    }

    public void eat(Snake snake) {
        snake.Grow();
    }
}