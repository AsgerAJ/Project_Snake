import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import java.util.*;

public class Snake extends ArrayList<Rectangle> {
    private Direction direction;
    private int playerNumber;
    private int x;
    private int y;
    private int score;
    private double sC;
    private boolean directionWasChanged = false;
    private double tailX;
    private double tailY;
    private boolean alive;

    /*
    * Constructs a snake:
    * Main Responsible: Team effort 
    */
    public Snake(int x, int y, double sC, Direction direction, int score, int startLength, int playerNumber) {
        this.x = x;
        this.y = y;
        this.sC = sC;
        this.score = score;
        this.playerNumber = playerNumber;
        for (int i = 0; i < startLength; i++) {
            super.add(new Rectangle((x / 2 + i) * sC, ((y + 2 * playerNumber) / 2) * sC, sC, sC));
            super.get(i).setFill((this.playerNumber == 0) ? Color.rgb(241, 196, 15) : Color.rgb(0, 204, 102));
        }
        this.direction = direction;
        setTailCoords();
        this.alive = true;
    }
    /*
    * Moves the snake
    * Main Responsible: Lovro & Lizette
    */
    public void moveSnake(Direction newDirection) {
        switch (newDirection) {
            case Up:
                get(size() - 1).setY((get(0).getY() - getSC() < 0) ? getYlim() * getSC() - getSC() : get(0).getY() - getSC());
                get(size() - 1).setX(get(0).getX());
                break;

            case Down:
                get(size() - 1).setY((get(0).getY() + getSC() > getYlim() * getSC()- sC) ? 0 : get(0).getY() + getSC());
                get(size() - 1).setX(get(0).getX());
                break;

            case Left:
                get(size() - 1).setX((get(0).getX() - getSC() < 0) ? getXlim() * getSC() - getSC() : get(0).getX() - getSC());
                get(size() - 1).setY(get(0).getY());
                break;

            case Right:
                get(size() - 1).setX((get(0).getX() + getSC() > getXlim() * getSC()-sC) ? 0 : get(0).getX() + getSC());
                get(size() - 1).setY(get(0).getY());
                break;

            default:
                break;
        }
    }
    /*
    * Grows the snake with one
    * Main Responsible: Asger
    */
    public void Grow() {
        scoreIncrease();
        super.add(new Rectangle(super.get(getLength() - 1).getX(), super.get(getLength() - 1).getY(), getSC(), getSC()));
        super.get(getLength() - 1).setFill((this.playerNumber == 0) ? Color.rgb(241, 196, 14) : Color.rgb(0, 204, 102));
    }
    /*
    * Checks if snake head collides with fruit
    * Main Responsible: Asger & Lizette
    */
    public boolean foodCollision(Food food) {
        if (food.getX() == get(0).getX() && food.getY() == get(0).getY()) {
            return true;
        }
        return false;
    }
    /*
    * Checks if snake head collides with fruit
    * Main Responsible: Asger & Lizette
    */
    public void selfCollide() {
        for (int i = 1; i < getLength(); i++) {
            if ((get(i).getX() == get(0).getX()) && (get(i).getY() == get(0).getY())) {
                this.alive = false;
            }
        }
    }

    /*
     * Checks if a snake collides with another snake
     * Main responsible: Lovro
     */
    public boolean enemyCollide(Snake enemy) {
        for (int i = 0; i < enemy.getLength(); i++) {
            if ((enemy.get(i).getX() == get(0).getX()) && (enemy.get(i).getY() == get(0).getY())) {
                return true;
            }
        }
        return false;
    }

    /*
     * Rest of methods
     * Main responsible: Team effort
     */
    
    public Direction getDirr() {
        return this.direction;
    }

    public void scoreIncrease() {
        this.score = score + 1;
    }

    public int getScore() {
        return this.score;
    }

    public void setCurrentDirection(Direction direction) {
        this.direction = direction;
    }

    public int getPlayerNumber() {
        return this.playerNumber;
    }

    public int getXlim() {
        return this.x;
    }

    public int getYlim() {
        return this.y;
    }

    public double getSC() {
        return this.sC;
    }

    public int getLength() {
        return super.size();
    }

    public void setDirectionWasChanged(boolean input) {
        this.directionWasChanged = input;
    }

    public boolean getDirectionWasChanged() {
        return this.directionWasChanged;
    }

    public double getTailCoordX() {
        return this.tailX;
    }

    public double getTailCoordY() {
        return this.tailY;
    }

    public void setTailCoords() {
        this.tailX = super.get(super.size() - 1).getX();
        this.tailY = super.get(super.size() - 1).getY();
    }

    public void murder() {
        this.alive = false;
    }

    public boolean getAlive() {
        return this.alive;
    }
}