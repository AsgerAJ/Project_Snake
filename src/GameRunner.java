import java.util.*;
import java.io.*;
import java.nio.file.*;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class GameRunner extends Application {

    // Public variables
    public double scalingConstant;
    public double height;
    public double width;
    public int n;
    public int m;
    public boolean multiplayer = false;
    public boolean startGame = false;
    public boolean scoreSet = false;

    // Private variables
    private Pane root;
    private Food food;
    private Snake snake1;
    private Snake snake2;
    private Label score;
    private TextField initials;
    private Font headFont = Font.loadFont("file:assets/fonts/Modak-Regular.ttf", 70);
    private Font detailFont = Font.loadFont("file:assets/fonts/Modak-Regular.ttf", 30);
    private Font checkFont = Font.loadFont("file:assets/fonts/Modak-Regular.ttf", 24);
    private Font scoreFont = Font.loadFont("file:assets/fonts/Modak-Regular.ttf", 20);
    private Font miniFont = Font.loadFont("file:assets/fonts/Modak-Regular.ttf", 12);
    private String scoreboard = "assets/scoreboard.txt";
    private String initialsString = "";
    // private boolean directionWasChanged = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Welcome to the snake game");
        try {
            sortFile(scoreboard);
            System.out.println("File sorted successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        n = 20;
        m = 20;

        if (n > m) {
            scalingConstant = 600 / (n);
        } else {
            scalingConstant = 600 / (m);
        }

        root = new Pane();
        root.setPrefSize(n, m);
        width = 600;
        height = 600;

        drawGrid(n, m);
        startScreen();

        width = scalingConstant * n;
        height = scalingConstant * m;
        Scene scene = new Scene(root, width, height);
        Runnable snakeStepper = () -> {
            try {
                while (true) {
                    stepHandler(snake1);
                    if (multiplayer) {
                        stepHandler(snake2);
                        if(snake1.enemyCollide(snake2)) {
                            snake1.setCurrentDirection(Direction.Stop);
                        }
                        if(snake2.enemyCollide(snake1)) {
                            snake2.setCurrentDirection(Direction.Stop);
                        }
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
                }
            });
        }
    }

    public void gameOver() throws FileNotFoundException {

        Rectangle blackscreen = new Rectangle(0, 0, width, height);
        Label gameOver = new Label("GAME OVER");
        gameOver.setFont(headFont);
        gameOver.setTextFill(Color.rgb(255, 200, 87));
        gameOver.relocate((width / 24 * 5 - 10), (height / 10));
        Button restart = new Button("RESTART");
        restart.setFont(detailFont);
        restart.relocate(width / 3 + 15, height * 7 / 10);
        blackscreen.setOpacity(0.25);

        Button addScore = new Button("Add score to scoreboard");
        addScore.setFont(scoreFont);
        addScore.relocate((width / 4) + 20, (height * 7 / 10) - 50);

        root.getChildren().addAll(blackscreen, restart, gameOver);
        if (!scoreSet) {
            root.getChildren().add(addScore);
        }
        EventHandler<ActionEvent> addScoreEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                if (!multiplayer && !scoreSet) {
                    String filestring = "" + snake1.getScore();
                    for (int resultString = filestring.length(); resultString < 5; resultString++) {
                        filestring = "0" + filestring;
                    }
                    filestring += " " + initialsString;
                    try {
                        writeSingleLine(scoreboard, filestring);
                        System.out.println("Line written successfully!");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                root.getChildren().remove(addScore);
                scoreSet = true;
            }
        };
        addScore.setOnAction(addScoreEvent);

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

    public void startScreen() throws FileNotFoundException{
        // Title
        Label title = new Label("SNAKE");
        title.setFont(headFont);
        title.setTextFill(Color.rgb(255, 200, 87));
        title.relocate((195), (height / 6));

        // multiplayer choice
        CheckBox multi = new CheckBox("Multiplayer");
        multi.setFont(checkFont);
        multi.relocate((scalingConstant * 3), (scalingConstant * 8 - (scalingConstant * 0.2)));
        multi.setTextFill(Color.rgb(255, 200, 87));

        // boardsize buttons:
        Button small = new Button("Small");
        small.setFont(detailFont);
        small.relocate((92), (500));

        Button medium = new Button("Medium");
        medium.setFont(detailFont);
        medium.relocate((227), (500));

        Button large = new Button("Large");
        large.setFont(detailFont);
        large.relocate((391), (500));

        // initials textfield
        initials = new TextField();
        initials.relocate(227, height / 3);
        initials.setTextFormatter(new TextFormatter<>(c -> c.getControlNewText().matches(".{0,3}") ? c : null));
        initials.setPromptText("Enter Initials here");

        // clear initials button
        Button clearInitials = new Button("Clear");
        clearInitials.setFont(miniFont);
        clearInitials.relocate(400, height / 3);

        // scoreboard
        Rectangle scoreBackground = new Rectangle(width / 2 + 40, height / 3 + 35, 200, 250);
        scoreBackground.setFill(Color.rgb(109, 74, 191));
        scoreBackground.setFill(Color.rgb(136, 91, 242));
        //scoreBackground.setOpacity(0.75);
        //scoreBackground.setFill(Color.WHITESMOKE);
        Label scoreBoard = new Label("Scoreboard");
        scoreBoard.setFont(checkFont);
        scoreBoard.relocate(width / 2 + 70, height / 3 + 35);
        Label first = new Label("1.");
        first.setFont(checkFont);
        first.relocate(width / 2 + 50, height / 3 + 80);
        Label second = new Label("2.");
        second.setFont(checkFont);
        second.relocate(width / 2 + 50, height / 3 + 120);
        Label third = new Label("3.");
        third.setFont(checkFont);
        third.relocate(width / 2 + 50, height / 3 + 160);
        Label fourth = new Label("4.");
        fourth.setFont(checkFont);
        fourth.relocate(width / 2 + 50, height / 3 + 200);
        Label fifth = new Label("5.");
        fifth.setFont(checkFont);
        fifth.relocate(width / 2 + 50, height / 3 + 240);

        File scoreboardfile = new File("assets/scoreboard.txt");
        Scanner scoreScanner = new Scanner(scoreboardfile);
        for (int i = 0; i < 5; i++) {
            String insertstring = scoreScanner.nextLine();
            switch (i) {
                case 0:
                    first.setText("1. " + insertstring);
                    break;
                case 1:
                    second.setText("2. " + insertstring);
                    break;
                case 2:
                    third.setText("3. " + insertstring);
                    break;
                case 3:
                    fourth.setText("4. " + insertstring);
                    break;
                case 4:
                    fifth.setText("5, " + insertstring);
                    break;

                default:
                    break;
            }
        }
        scoreScanner.close();

        root.getChildren().addAll(title, multi, small, medium, large, initials, clearInitials, scoreBackground,
                scoreBoard, first, second, third, fourth, fifth);

        EventHandler<ActionEvent> clearInitialHandler = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent size) {
                initials.clear();
            }

        };
        clearInitials.setOnAction(clearInitialHandler);

        EventHandler<ActionEvent> sizeSelectSmall = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent size) {
                root.getChildren().clear();
                Random foodCord = new Random();
                n = 8;
                m = 8;
                scalingConstant = 75;
                multiplayer = multi.selectedProperty().get();
                initialsString = initials.getText();
                startGame = true;
                drawGrid(n, m);
                food = new Food(foodCord.nextInt(n) + 1, foodCord.nextInt(m) + 1, scalingConstant);
                drawFood(food);
                snake1 = new Snake(n, m, scalingConstant, Direction.Stop, 0, 2, 0);
                drawSnake(snake1);
                if (!multiplayer) {
                    displayScore(snake1);
                }
                if (multiplayer) {
                    snake2 = new Snake(n, m, scalingConstant, Direction.Stop, 0, 2, 2);
                    drawSnake(snake2);
                }
                System.out.println(initialsString);

            }
        };
        small.setOnAction(sizeSelectSmall);

        EventHandler<ActionEvent> sizeSelectMedium = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent size) {
                root.getChildren().clear();
                Random foodCord = new Random();
                n = 15;
                m = 15;
                scalingConstant = 40;
                multiplayer = multi.selectedProperty().get();
                initialsString = initials.getText();
                startGame = true;
                drawGrid(n, m);
                food = new Food(foodCord.nextInt(n) + 1, foodCord.nextInt(m) + 1, scalingConstant);
                drawFood(food);
                snake1 = new Snake(n, m, scalingConstant, Direction.Stop, 0, 2, 0);
                drawSnake(snake1);
                if (!multiplayer) {
                    displayScore(snake1);
                }
                if (multiplayer) {
                    snake2 = new Snake(n, m, scalingConstant, Direction.Stop, 0, 2, 2);
                    drawSnake(snake2);
                }

            }
        };
        medium.setOnAction(sizeSelectMedium);
        EventHandler<ActionEvent> sizeSelectLarge = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent size) {
                root.getChildren().clear();
                Random foodCord = new Random();
                n = 30;
                m = 30;
                scalingConstant = 20;
                multiplayer = multi.selectedProperty().get();
                initialsString = initials.getText();
                drawGrid(n, m);
                food = new Food(foodCord.nextInt(n) + 1, foodCord.nextInt(m) + 1, scalingConstant);
                drawFood(food);
                snake1 = new Snake(n, m, scalingConstant, Direction.Stop, 0, 2, 0);
                drawSnake(snake1);
                if (!multiplayer) {
                    displayScore(snake1);
                }
                if (multiplayer) {
                    snake2 = new Snake(n, m, scalingConstant, Direction.Stop, 0, 2, 2);
                    drawSnake(snake2);
                }
                startGame = true;
            }
        };
        large.setOnAction(sizeSelectLarge);
        scoreScanner.close();
    }

    public void displayScore(Snake snake) {
        score = new Label();
        score.setText("" + snake.getScore());
        score.setFont(new Font("Arial", 30));
        score.setTextFill(Color.RED);
        score.relocate(10, 0);
        root.getChildren().add(score);
    }

    public void updateScore(Snake snake) {
        root.getChildren().remove(score);
        score.setText("" + snake.getScore());
        root.getChildren().add(score);
    }

    public void eat(Snake snake) {
        snake.Grow();
        if (!multiplayer) {
            updateScore(snake);
        }
        root.getChildren().add(snake.get(snake.getLength() - 1));
    }

    public static void sortFile(String inputFile) throws IOException {
        List<String> lines = new ArrayList<>();

        // Read lines from the input file
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }

        // Custom comparator for sorting based on the 5-digit number
        Comparator<String> comparator = Comparator.comparingInt(s -> Integer.parseInt(s.substring(0, 5)));
        comparator = comparator.reversed();

        // Sort the lines
        lines.sort(comparator);

        // Write the sorted lines back to the input file, overwriting its content
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    public static void writeSingleLine(String outputFile, String lineToWrite) throws IOException {
        Path outputPath = Paths.get(outputFile);
        Files.write(outputPath, (lineToWrite + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE,
                StandardOpenOption.WRITE, StandardOpenOption.APPEND);
    }

}