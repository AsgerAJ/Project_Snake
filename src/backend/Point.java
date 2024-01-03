package src.backend;

import java.util.*;

public class Point {

    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void updatePoint(int dx, int dy) {
        this.x = x+dx;
        this.y = y+dy;
    }

    public boolean equals(Point other) {
        if (this.getX() == other.getX() && this.getY() == other.getY()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return x + "," + y;
    }

}
