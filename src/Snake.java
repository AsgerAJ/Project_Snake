import javafx.scene.shape.*;
import java.util.*;

public class Snake extends ArrayList<Rectangle> {

    private Direction direction;
    public int playerNumber = 0;
    private int x;
    private int y;
    private int score;
    private double sC;

    public Snake(int x, int y, double sC, Direction direction, int score) {
        this.x = x;
        this.y = y;
        this.sC = sC;
        this.score = score;
        super.add(new Rectangle((x / 2) * sC, (y / 2) * sC, sC, sC));
        this.direction = direction;
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
                if (get(0).getY() + getSC() > getYlim() * getSC()) {
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
                if (get(0).getX() + getSC() > getXlim() * getSC()) {
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
        this.score++;
        super.add(new Rectangle(super.get(getLength() - 1).getX(), super.get(getLength() - 1).getY(), getSC(),getSC()));
    }

    public boolean eat() {
        for (int i = 1; i < getLength() - 1; i++) {
            if (get(0).intersects(get(i).getBoundsInLocal())) {
                return false;
            }
        }
        return true;
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


    public Direction getDirr() {
        return this.direction;
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

}