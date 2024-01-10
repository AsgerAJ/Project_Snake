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
import javafx.scene.text.FontWeight;
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
    public boolean gameIsStarted = false;

    // Private variables
    private Pane root;
    private Food food;
    private Snake snake1;
    private Snake snake2;
    private Label score;
    private TextField initials;
    private Font headFont = Font.loadFont("file:assets/fonts/Modak-Regular.ttf", 140);
    private Font gameOverFont = Font.loadFont("file:assets/fonts/Modak-Regular.ttf", 100);
    private Font winnerFont = Font.loadFont("file:assets/fonts/Modak-Regular.ttf", 70);
    private Font detailFont = Font.loadFont("file:assets/fonts/Modak-Regular.ttf", 30);
    private Font checkFont = Font.loadFont("file:assets/fonts/Modak-Regular.ttf", 24);
    private Font scoreFont = Font.loadFont("file:assets/fonts/Modak-Regular.ttf", 20);
    private Font miniFont = Font.loadFont("file:assets/fonts/Modak-Regular.ttf", 12);
    private Font scoreBoardFont = Font.loadFont("file:assets/fonts/LcdSolid-VpzB.ttf", 25);
 
    private String scoreboard = "assets/scoreboard.txt";
    private String initialsString = "";
    private String winner;
    private Button addScore;
    private boolean gameOverEvent = false;
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
        scalingConstant = (n > m) ? 600 / (n) : 600 / (m);
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
                        if (snake1.enemyCollide(snake2)) {
                            snake1.setCurrentDirection(Direction.Stop);
                            snake2.setCurrentDirection(Direction.Stop);
                            snake1.murder();
                            winner = "Player 2";
                        }
                        if (snake2.enemyCollide(snake1)) {
                            snake1.setCurrentDirection(Direction.Stop);
                            snake2.setCurrentDirection(Direction.Stop);
                            snake2.murder();
                            winner = "Player 1";
                        }
                    }
                    Thread.sleep(100);
                }
            } catch (InterruptedException ie) {
            }
        };

        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if(gameIsStarted) {
                KeyCode code = event.getCode();
                snake1.setTailCoords();
                if (multiplayer) {
                    snake2.setTailCoords();
                }
                switch (code) {
                    case UP:
                        if (snake1.getAlive() 
                        && snake1.getDirr() != Direction.Down
                        && !snake1.getDirectionWasChanged()) {
                            snake1.setCurrentDirection(Direction.Up);
                            snake1.setDirectionWasChanged(true);
                        }
                        break;
                    case DOWN:
                        if (snake1.getAlive() 
                        && snake1.getDirr() != Direction.Up
                        && !snake1.getDirectionWasChanged()) {
                            snake1.setCurrentDirection(Direction.Down);
                            snake1.setDirectionWasChanged(true);
                        }
                        break;
                    case LEFT:
                        if (snake1.getAlive() 
                        && snake1.getDirr() != Direction.Right
                        && !snake1.getDirectionWasChanged()) {
                            snake1.setCurrentDirection(Direction.Left);
                            snake1.setDirectionWasChanged(true);
                        }
                        break;
                    case RIGHT:
                        if (snake1.getAlive() 
                        && snake1.getDirr() != Direction.Left
                        && !snake1.getDirectionWasChanged()) {
                            snake1.setCurrentDirection(Direction.Right);
                            snake1.setDirectionWasChanged(true);
                        }
                        break;
                    case W:
                        if (multiplayer
                        && snake2.getAlive() 
                        && !snake2.getDirectionWasChanged()
                        && snake2.getDirr() != Direction.Down) {
                            snake2.setCurrentDirection(Direction.Up);
                            snake2.setDirectionWasChanged(true);
                        }
                        break;
                    case S:
                        if (multiplayer
                        && snake2.getAlive() 
                        && !snake2.getDirectionWasChanged()
                        && snake2.getDirr() != Direction.Up) {
                            snake2.setCurrentDirection(Direction.Down);
                            snake2.setDirectionWasChanged(true);
                        }
                        break;
                    case A:
                        if (multiplayer
                        && snake2.getAlive() 
                        && !snake2.getDirectionWasChanged()
                        && snake2.getDirr() != Direction.Right) {
                            snake2.setCurrentDirection(Direction.Left);
                            snake2.setDirectionWasChanged(true);
                        }
                        break;
                    case D:
                        if (multiplayer
                        && snake2.getAlive() 
                        && !snake2.getDirectionWasChanged()
                        && snake2.getDirr() != Direction.Left) {
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
                Rectangle back = new Rectangle(i * scalingConstant, j * scalingConstant, scalingConstant, scalingConstant);
                back.setFill((((i % 2 == 0) && (j % 2 == 0)) || ((i % 2 != 0) && (j % 2 != 0))) ? Color.rgb(61, 66, 65) : Color.rgb(37, 42, 39));
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
                snake.selfCollide();
                if (!snake.getAlive()) {
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
        if (!gameOverEvent) {
            gameOverEvent = true;
            Rectangle blackscreen = new Rectangle(0, 0, width, height);
            blackscreen.setOpacity(0.6);
            Label gameOver = new Label("GAME OVER");
            gameOver.setFont(gameOverFont);
            gameOver.setTextFill(Color.rgb(115, 147, 126));
            gameOver.relocate((width / 15), (height / 20));
            Button restart = new Button("RESTART");
            restart.setFont(detailFont);
            restart.relocate((width / 15) * 5.5, (height / 15) * 12.12);
            root.getChildren().add(blackscreen);
            if (multiplayer) {
                Label winnerLabel = new Label(winner + " wins!");
                winnerLabel.setFont(winnerFont);
                winnerLabel.setTextFill((winner == "Player 1") ? Color.rgb(241, 196, 15) : Color.rgb(0, 204, 102));
                winnerLabel.relocate((width / 15 * 2), (height / 15 * 5));
                root.getChildren().add(winnerLabel);
            }
            root.getChildren().addAll(restart, gameOver);

            if (!multiplayer) {
                Label playerScore = new Label();
                playerScore.setText("" + snake1.getScore());
                playerScore.setFont(Font.font("Futura", FontWeight.BOLD, width/3));
                playerScore.setTextFill(Color.rgb(115, 147, 126));
                if (snake1.getScore()<10){
                playerScore.relocate(width/15*6, height/15*4);
                } else {
                playerScore.relocate(width/15*4, height/15*4); 
                }
                root.getChildren().add(playerScore);
                addScore = new Button("Add score to scoreboard");
                addScore.setFont(scoreFont);
                addScore.relocate((width / 15) * 4.35, (height / 15) * 10.12);
                if (!scoreSet) {
                    root.getChildren().add(addScore);
                }

                EventHandler<ActionEvent> addScoreEvent = new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        if (!multiplayer && !scoreSet) {
                            String filestring = "" + snake1.getScore();
                            for (int resultString = filestring.length(); resultString < 3; resultString++) {
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
            }

            EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
                public void handle(ActionEvent a) {
                    root.getChildren().clear();
                    Random foodCord = new Random();
                    drawGrid(n, m);
                    snake1 = new Snake(n, m, scalingConstant, Direction.Stop, 0, 2, 0);
                    drawSnake(snake1);
                    if (multiplayer) {
                        snake2 = new Snake(n, m, scalingConstant, Direction.Stop, 0, 2, 2);
                        drawSnake(snake2);
                    }
                    food = new Food(foodCord.nextInt(n) + 1, foodCord.nextInt(m) + 1, scalingConstant);
                    drawFood(food);
                    gameOverEvent = false;
                }
            };
            restart.setOnAction(event);
        }

    }

    public void startScreen() throws FileNotFoundException {
        // Title
        Label title = new Label("SNAKE");
        title.setFont(headFont);
        title.setTextFill(Color.rgb(115, 147, 126));
        title.relocate((88), (height / 200 - 19));

        // multiplayer choice
        CheckBox multi = new CheckBox("Multiplayer");
        multi.setFont(checkFont);
        multi.relocate((scalingConstant * 3), (scalingConstant * 9 - (scalingConstant * 0.2)));
        multi.setTextFill(Color.rgb(206, 185, 146));

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
        initials.relocate(scalingConstant * 3, scalingConstant * 7 + 2);
        initials.setTextFormatter(new TextFormatter<>(c -> c.getControlNewText().matches(".{0,3}") ? c : null));
        initials.setPromptText("Enter Initials here");

        // clear initials button
        Button clearInitials = new Button("Clear");
        clearInitials.setFont(miniFont);
        clearInitials.relocate(scalingConstant * 8.75, scalingConstant * 7);

        // scoreboard
        Rectangle scoreBackground = new Rectangle(scalingConstant * 12, scalingConstant * 7, scalingConstant * 6, scalingConstant * 8);
        scoreBackground.setFill(Color.rgb(115, 147, 126));
        Label scoreBoard = new Label("Scoreboard");
        scoreBoard.setFont(scoreBoardFont);
        scoreBoard.relocate(scalingConstant * 12.5, scalingConstant * 7 + 5);
        scoreBoard.setTextFill(Color.rgb(206, 185, 146));
        scoreBoard.setUnderline(true);
        Label first = new Label("1.");
        first.setFont(scoreBoardFont);
        first.relocate(scalingConstant * 12.5, scalingConstant * 8.5);
        Label second = new Label("2.");
        second.setFont(scoreBoardFont);
        second.relocate(scalingConstant * 12.5, scalingConstant * 9.8125);
        Label third = new Label("3.");
        third.setFont(scoreBoardFont);
        third.relocate(scalingConstant * 12.5, scalingConstant * 11.125);
        Label fourth = new Label("4.");
        fourth.setFont(scoreBoardFont);
        fourth.relocate(scalingConstant * 12.5, scalingConstant * 12.4375);
        Label fifth = new Label("5.");
        fifth.setFont(scoreBoardFont);
        fifth.relocate(scalingConstant * 12.5, scalingConstant * 13.75);

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
                    fifth.setText("5. " + insertstring);
                    break;
                default:
                    break;
            }
        }
        root.getChildren().addAll(title, multi, small, medium, large, initials, clearInitials, scoreBackground, scoreBoard, first, second, third, fourth, fifth);
        scoreScanner.close();

        EventHandler<ActionEvent> clearInitialHandler = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent size) {
                initials.clear();
            }
        };
        clearInitials.setOnAction(clearInitialHandler);

        EventHandler<ActionEvent> sizeSelectSmall = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent size) {
                gameIsStarted = true;
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
                if (multiplayer) {
                    snake2 = new Snake(n, m, scalingConstant, Direction.Stop, 0, 2, 2);
                    drawSnake(snake2);
                } else {
                    displayScore(snake1);
                }
            }
        };
        small.setOnAction(sizeSelectSmall);

        EventHandler<ActionEvent> sizeSelectMedium = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent size) {
                gameIsStarted = true;
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
                if (multiplayer) {
                    snake2 = new Snake(n, m, scalingConstant, Direction.Stop, 0, 2, 2);
                    drawSnake(snake2);
                } else {
                    displayScore(snake1);
                }
            }
        };
        medium.setOnAction(sizeSelectMedium);
        
        EventHandler<ActionEvent> sizeSelectLarge = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent size) {
                gameIsStarted = true;
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
                if (multiplayer) {
                    snake2 = new Snake(n, m, scalingConstant, Direction.Stop, 0, 2, 2);
                    drawSnake(snake2);
                } else {
                    displayScore(snake1);
                }
            }
        };
        large.setOnAction(sizeSelectLarge);
    }

    public void displayScore(Snake snake) {
        score = new Label();
        score.setText("" + snake.getScore());
        score.setFont(Font.font("Futura", FontWeight.BOLD, scalingConstant));
        score.setTextFill(Color.rgb(115, 147, 126));
        score.relocate(scalingConstant / 5, -scalingConstant / 7);
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
        Comparator<String> comparator = Comparator.comparingInt(s -> Integer.parseInt(s.substring(0, 3)));
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
        Files.write(outputPath, (lineToWrite + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
    }
}