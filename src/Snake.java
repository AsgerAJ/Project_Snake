import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import java.util.*;

public class Snake extends ArrayList<Rectangle> {

    private Direction direction;
    public int playerNumber;
    private int x;
    private int y;
    private int score;
    private double sC;
    private boolean directionWasChanged = false;
    private double tailX;
    private double tailY;
    private boolean alive;

    public Snake(int x, int y, double sC, Direction direction, int score, int startLength, int playerNumber) {
        this.x = x;
        this.y = y;
        this.sC = sC;
        this.score = score;
        this.playerNumber = playerNumber;
        for(int i = 0; i < startLength; i++){
            super.add(new Rectangle((x / 2+i) * sC, ((y + 2*playerNumber) / 2) * sC, sC, sC));
            if(this.playerNumber == 0) {
                super.get(i).setFill(Color.rgb(189, 217, 191));
            } else {
                super.get(i).setFill(Color.rgb(255, 101, 66));
            }
        }
        this.direction = direction;
        setTailCoords();
        this.alive = true;
        //playerNumber++;
        // this.playerNumber = playerNumber;
    }

    public void moveSnake(Direction newDirection) {
        switch (newDirection) {

            case Up:
                if (get(0).getY() - getSC() < 0) {
                    get(size() - 1).setY(getYlim() * getSC() - getSC());
                    get(size() - 1).setX(get(0).getX());
                } else {
                    get(size() - 1).setY(get(0).getY() - getSC());
                    get(size() - 1).setX(get(0).getX());
                }
                break;

            case Down:
                if (get(0).getY() + getSC() > getYlim() * getSC()- sC) {
                    get(size() - 1).setY(0);
                    get(size() - 1).setX(get(0).getX());
                } else {
                    get(size() - 1).setY(get(0).getY() + getSC());
                    get(size() - 1).setX(get(0).getX());
                }
                break;

            case Left:
                if (get(0).getX() - getSC() < 0) {
                    get(size() - 1).setX(getXlim() * getSC() - getSC());
                    get(size() - 1).setY(get(0).getY());
                } else {
                    get(size() - 1).setX(get(0).getX() - getSC());
                    get(size() - 1).setY(get(0).getY());
                }
                break;

            case Right:
                if (get(0).getX() + getSC() > getXlim() * getSC()-sC) {
                    get(size() - 1).setX(0);
                    get(size() - 1).setY(get(0).getY());
                } else {
                    get(size() - 1).setX(get(0).getX() + getSC());
                    get(size() - 1).setY(get(0).getY());
                }

                break;

            case Stop:
                break;
            default:
                break;
        }
    }

    public void Grow() {
        scoreIncrease();
        super.add(new Rectangle(super.get(getLength() - 1).getX(), super.get(getLength() - 1).getY(), getSC(),getSC()));
        if(this.playerNumber == 0) {
            super.get(getLength()-1).setFill(Color.rgb(189, 217, 191));
        } else {
            super.get(getLength()-1).setFill(Color.rgb(255, 101, 66));
        }
        
    }

    public boolean foodCollision(Rectangle food) {
        if (food.getX()==get(0).getX() && food.getY() == get(0).getY()) {
            return true;
        }
    return false;
    }

    public boolean selfCollide(){
        boolean dead = false;
        for (int i = 1; i < getLength()-1; i++){
            if((get(i).getX() == get(0).getX()) && (get(i).getY() == get(0).getY()) ){
                return dead = true;
            }
        }
        return dead;
    }

    public boolean enemyCollide(Snake enemy) {
        for (int i = 1; i < enemy.getLength()-1; i++){
            if((enemy.get(i).getX() == get(0).getX()) && (enemy.get(i).getY() == get(0).getY()) ){
                return true;
            }
        }
        return false;
    }

    public Direction getDirr() {
        return this.direction;
    }

    public void scoreIncrease() {
        this.score = score +1;
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

    public double getTailCoordX(){
        return this.tailX;
    }

    public double getTailCoordY(){
        return this.tailY;
    }

    public void setTailCoords(){
        this.tailX = super.get(super.size()-1).getX();
        this.tailY = super.get(super.size()-1).getY();
    }

    public void murder(){
        this.alive = false;
    }

    public boolean getAlive(){
        return this.alive;
    }
}