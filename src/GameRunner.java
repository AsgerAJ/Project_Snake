import java.util.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class GameRunner extends Application {

    // Public variables
    public double scalingConstant;
    public double height;
    public double width;
    public int n;
    public int m;
    public boolean multiplayer = true;
    public boolean startGame = false;

    // Private variables
    private Pane root;
    private Food food;
    private Snake snake1;
    private Snake snake2;
    private Label score;
    private Font headFont = Font.loadFont("file:assets/fonts/Modak-Regular.ttf", 70);
    private Font detailFont = Font.loadFont("file:assets/fonts/Modak-Regular.ttf", 20);
    private Font checkFont = Font.loadFont("file:assets/fonts/Modak-Regular.ttf", 19);

    // private boolean directionWasChanged = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Welcome to the snake game");

        n = 20;
        m = 20;

        if (n > m) {
            scalingConstant = 600 / (n);
        } else {
            scalingConstant = 600 / (m);
        }

        root = new Pane();
        root.setPrefSize(n, m);
        Random foodCord = new Random();

        width = 600;
        height = 600;

        drawGrid(n, m);
        startScreen();

        if (startGame) {
            food = new Food(foodCord.nextInt(n) + 1, foodCord.nextInt(m) + 1, scalingConstant);
            drawFood(food);
            snake1 = new Snake(n, m, scalingConstant, Direction.Stop, 0, 2, 0);
            drawSnake(snake1);
            if (multiplayer) {
                snake2 = new Snake(n, m, scalingConstant, Direction.Stop, 0, 2, 2);
                drawSnake(snake2);
            }

        }

        width = scalingConstant * n;
        height = scalingConstant * m;
        Scene scene = new Scene(root, width, height);

        Runnable snakeStepper = () -> {
            try {
                while (true) {
                    stepHandler(snake1);
                    if (multiplayer) {
                        stepHandler(snake2);
                    }
                    Thread.sleep(100);
                }

            } catch (InterruptedException ie) {
            }
        };

        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            KeyCode code = event.getCode();
            snake1.setTailCoords();
            if (multiplayer) {
                snake2.setTailCoords();
            }

            switch (code) {
                case UP:
                    if (snake1.getAlive() && snake1.getDirr() != Direction.Down
                            && !snake1.getDirectionWasChanged()) {
                        snake1.setCurrentDirection(Direction.Up);
                        snake1.setDirectionWasChanged(true);
                    }
                    break;
                case DOWN:
                    if (snake1.getAlive() && snake1.getDirr() != Direction.Up
                            && !snake1.getDirectionWasChanged()) {
                        snake1.setCurrentDirection(Direction.Down);
                        snake1.setDirectionWasChanged(true);
                    }
                    break;
                case LEFT:
                    if (snake1.getAlive() && snake1.getDirr() != Direction.Right
                            && !snake1.getDirectionWasChanged()) {
                        snake1.setCurrentDirection(Direction.Left);
                        snake1.setDirectionWasChanged(true);
                    }
                    break;
                case RIGHT:
                    if (snake1.getAlive() && snake1.getDirr() != Direction.Left
                            && !snake1.getDirectionWasChanged()) {
                        snake1.setCurrentDirection(Direction.Right);
                        snake1.setDirectionWasChanged(true);
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
                    snake1.Grow();
                    root.getChildren().add(snake1.get(snake1.getLength() - 1));
                    break;

                case G:
                    snake1.setCurrentDirection(Direction.Stop);
                    if (multiplayer) {
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
        if (startGame) {
            Platform.runLater(() -> {
                snake.setDirectionWasChanged(false);
                snake.moveSnake(snake.getDirr());
                Collections.rotate(snake, 1);
                if (snake.selfCollide()) {
                    snake.setCurrentDirection(Direction.Stop);
                    snake.murder();
                    try {
                        gameOver();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if (snake.foodCollision(food)) {
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
    }

    public void gameOver() throws FileNotFoundException {
        Rectangle blackscreen = new Rectangle(0, 0, width, height);
        Label gameOver = new Label("GAME OVER");
        gameOver.setFont(headFont);
        gameOver.setTextFill(Color.rgb(255, 200, 87));
        gameOver.relocate((width/2), (scalingConstant*1));
        Button restart = new Button("RESTART");
        restart.setFont(detailFont);
        restart.relocate(scalingConstant*5, scalingConstant*10);
        root.getChildren().addAll(blackscreen, restart, gameOver);
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent a) {
                Random foodCord = new Random();
                root.getChildren().clear();
                drawGrid(n, m);
                snake1 = new Snake(n, m, scalingConstant, Direction.Stop, 0, 2, 0);
                drawSnake(snake1);
                if (multiplayer) {
                    snake2 = new Snake(n, m, scalingConstant, Direction.Stop, 0, 2, 2);
                    drawSnake(snake2);
                }
                food = new Food(foodCord.nextInt(n) + 1, foodCord.nextInt(m) + 1, scalingConstant);
                drawFood(food);
            }
        };
        restart.setOnAction(event);
    }

    public void startScreen() {
        // Title
        Label title = new Label("SNAKE");
        title.setFont(headFont);
        title.setTextFill(Color.rgb(255, 200, 87));
        title.relocate((140), (height / 6));

        // multiplayer choice
        CheckBox multi = new CheckBox("Multiplayer");
        multi.setFont(checkFont);
        multi.relocate((scalingConstant * 3), (scalingConstant * 8 - (scalingConstant * 0.2)));
        multi.setTextFill(Color.rgb(255, 200, 87));

        // boardsize buttons:
        Button small = new Button("Small");
        small.setFont(detailFont);
        small.relocate((110), (400));

        Button medium = new Button("Medium");
        medium.setFont(detailFont);
        medium.relocate((200), (400));

        Button large = new Button("Large");
        large.setFont(detailFont);
        large.relocate((310), (400));

        root.getChildren().addAll(title, multi, small, medium, large);

        EventHandler<ActionEvent> sizeSelectSmall = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent size) {
                root.getChildren().clear();
                Random foodCord = new Random();
                n = 15;
                m = 15;
                scalingConstant = 40;
                drawGrid(n, m);
                food = new Food(foodCord.nextInt(n) + 1, foodCord.nextInt(m) + 1, scalingConstant);
                drawFood(food);
                snake = new Snake(n, m, scalingConstant, Direction.Stop, 0, 2, 0);
                drawSnake(snake);
                displayScore(snake);
                if (multiplayer) {
                    snake2 = new Snake(n, m, scalingConstant, Direction.Stop, 0, 2, 2);
                    drawSnake(snake2);
                }
                startGame = true;
                // multiplayer = multi.BooleanProperty().isSelected();
            }
        };
        small.setOnAction(sizeSelectSmall);
        EventHandler<ActionEvent> sizeSelectMedium = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent size) {
                root.getChildren().clear();
                Random foodCord = new Random();
                n = 30;
                m = 30;
                scalingConstant = 20;
                drawGrid(n, m);
                food = new Food(foodCord.nextInt(n) + 1, foodCord.nextInt(m) + 1, scalingConstant);
                drawFood(food);
                snake = new Snake(n, m, scalingConstant, Direction.Stop, 0, 2, 0);
                drawSnake(snake);
                if (multiplayer) {
                    snake2 = new Snake(n, m, scalingConstant, Direction.Stop, 0, 2, 2);
                    drawSnake(snake2);
                }
                startGame = true;
                // multiplayer = multi.BooleanProperty().isSelected();
            }
        };
        medium.setOnAction(sizeSelectMedium);
        EventHandler<ActionEvent> sizeSelectLarge = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent size) {
                root.getChildren().clear();
                Random foodCord = new Random();
                n = 60;
                m = 60;
                scalingConstant = 10;
                drawGrid(n, m);
                food = new Food(foodCord.nextInt(n) + 1, foodCord.nextInt(m) + 1, scalingConstant);
                drawFood(food);
                snake = new Snake(n, m, scalingConstant, Direction.Stop, 0, 2, 0);
                drawSnake(snake);
                if (multiplayer) {
                    snake2 = new Snake(n, m, scalingConstant, Direction.Stop, 0, 2, 2);
                    drawSnake(snake2);
                }
                startGame = true;
                // multiplayer = multi.BooleanProperty().isSelected();
            }
        };
        large.setOnAction(sizeSelectLarge);
    }

    public void displayScore(Snake snake) {
        score = new Label();
        score.setText("" + snake.getScore());
        score.setFont(new Font("Arial", 30));
        score.setTextFill(Color.RED);
        score.relocate(10, 0);
        root.getChildren().add(score);
    }

    public void updateScore(){
        root.getChildren().remove(score);
        score.setText("" + snake.getScore());
        root.getChildren().add(score);
    }

    public void eat(Snake snake) {
        snake.Grow();
        updateScore();
    }

}