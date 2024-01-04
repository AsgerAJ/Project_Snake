package src.backend;

import java.util.*;
public class Snake {
    
    private int score;
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
        Point oldHead = this.body.get(0);
        switch (direction) {
            case LEFT:
                Point newPoint = (oldHead.getX() - 1 < 0) ? new Point(this.gridSizeX - 1, oldHead.getY()) : new Point(oldHead.getX() - 1, oldHead.getY());
                this.body.add(0, newPoint);
                break;

            case DOWN:
                newPoint = (oldHead.getY() + 1 > this.gridSizeY - 1) ? new Point(oldHead.getX(), 0) : new Point(oldHead.getX(), oldHead.getY() + 1);
                this.body.add(0, newPoint);
                break;

            case RIGHT:
                newPoint = (oldHead.getX() + 1 > this.gridSizeX - 1) ? new Point(0, oldHead.getY()) : new Point(oldHead.getX() + 1, oldHead.getY());
                this.body.add(0, newPoint);
                break;

            case UP:
                newPoint = (oldHead.getY() - 1 < 0) ? new Point(oldHead.getX(), this.gridSizeY - 1) : new Point(oldHead.getX(), oldHead.getY() - 1);
                this.body.add(0, newPoint);
                break;

            default:
                break;
        }
        if(!grow()) {
            this.body.remove(getLength() - 1);
        }
    }

    public boolean selfCollision() {
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

     public boolean eat(Point p){
        boolean output = (p == body.get(0)) ? true : false;
        return output;
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

    public void setDirection(Direction direction){
        this.direction = direction;
    }
}