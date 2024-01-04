package src.backend;

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
        this.x = this.x+dx;
        this.y = this.y+dy;
    }

    public boolean equals(Point other) {
        boolean output = (this.getX() == other.getX() && this.getY() == other.getY()) ? true : false;
        return output;
    }

    @Override
    public String toString() {
        return x + "," + y;
    }

}
