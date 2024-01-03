package src.backend;

import java.util.*;

public class Snake {
    private int score;
    private int xstart;
    private int ystart;
    private int gridSizeX;
    private int gridSizeY;
    private Direction direction;
    private ArrayList<Point> body = new ArrayList<Point>();
    private boolean alive;

    public Snake(int xstart, int ystart, int gridSizeX, int gridSizeY) {
        this.direction = Direction.DOWN;
        this.body.add(new Point(xstart, ystart)); // head
        this.body.add(new Point(xstart, ystart + 1)); // tail
        this.gridSizeX = gridSizeX;
        this.gridSizeY = gridSizeY;
        this.alive = true;
        this.score = 0;
    }

    public void update(Direction direction) { // google har 8 steps i sekundet
        // if målfelt is clear
        // if not on egde

        // new head

        Point oldHead = this.body.get(0);
        Point newHead = oldHead;
        switch (direction) { // handles movement of snake.
            case LEFT: // left movement
                if (newHead.getX() - 1 < 0) {
                    //newHead.setX(this.gridSizeX - 1);
                    this.body.add(0, new Point(this.gridSizeX - 1, oldHead.getY()));
                } else {
                    this.body.add(0, new Point(oldHead.getX() - 1, oldHead.getY()));
                    //newHead.updatePoint(-1, 0);
                }
                break;

            case DOWN:
                if (newHead.getY() + 1 > this.gridSizeY - 1) {
                    /* 
                    newHead.setY(0);
                } else {
                    newHead.updatePoint(0, 1);
                    */
                    this.body.add(0, new Point(oldHead.getX(), 0));
                } else {
                    this.body.add(0, new Point(oldHead.getX(), oldHead.getY() + 1));
                }
                break;

            case RIGHT: // right movement
                if (newHead.getX() + 1 > this.gridSizeX - 1) {
                    /*
                    newHead.setX(0);
                } else {
                    newHead.updatePoint(1, 0);
                    */
                    this.body.add(0, new Point(0, oldHead.getY()));
                } else {
                    this.body.add(0, new Point(oldHead.getX() + 1, oldHead.getY()));
                }
                break;

            case UP:
                if (newHead.getY() - 1 < 0) {
                    /*
                    newHead.setY(this.gridSizeY - 1);
                } else {
                    newHead.updatePoint(0, -1);
                    */
                    this.body.add(0, new Point(oldHead.getX(), this.gridSizeY - 1));
                } else {
                    this.body.add(0, new Point(oldHead.getX(), oldHead.getY() - 1));
                }
                break;

            default:
                break;
        }

        // remove tail
        if(!grow()) {
            this.body.remove(getLength() - 1);
        }
    }

    public boolean collision() {
        boolean collision = false;
        for (int i = 0; i < this.body.size(); i++) {
            if (this.body.get(0).equals(this.body.get(i))) {
                collision = true;
                break;
            } else {
                collision = false;
            }
        }
        return collision;
    }

    public boolean grow() {
        return false;
    }

    public Point getHead() {
        return this.body.get(0);
    }

    public Point getPoint(int i) {
        return this.body.get(i);
    }

    public int getLength() {
        return this.body.size();
    }

    public Direction gDirection(){
        return this.direction;
    }
}