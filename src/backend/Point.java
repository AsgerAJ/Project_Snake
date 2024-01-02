package src.backend;

public class Point {
    
    private int x;
    private int y; 

    public Point(int x, int y){
        this.x = x; 
        this.y = y; 
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean equals(Point other) {
        if(this.getX()==other.getX() && this.getY() == other.getY()){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public String toString() {
        return x + "," + y;
    }

}
