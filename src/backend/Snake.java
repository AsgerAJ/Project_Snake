package src.backend;
import java.util.*;

public class Snake {
    private int score;
    private int xstart;
    private int ystart;
    private int direction = 0; // 0 <- , 1 ^ , 2 ->, 3 v
    private ArrayList<Point> body = new ArrayList<Point>();
    private boolean alive;
    
    public Snake(int xstart, int ystart){
        this.body.add(new Point(xstart, ystart)); // head
        this.body.add(new Point(xstart, ystart+1)); // tail
        this.alive = true;
        this.score = 0;
    }

    public void update(int direction, int oldDirection) { // google har 8 steps i sekundet
        // if målfelt is clear
        // if not on egde

        // new head

        Point oldHead = this.body.get(0);
        Point newHead = oldHead;
        switch (direction) { // handles movement of snake.
            case 0: // left movement
                if(newHead.getX() - 1 < 0) {
                    newHead.setX(this.gridSizeX-1);
                } else {
                    newHead.updatePoint(-1,0);
                }
                break;

            case 1: // up movement
                if(newHead.getY() + 1 > this.gridSizeY) {
                    newHead.setY(0);
                } else {
                    newHead.updatePoint(0, 1);
                }
                break;

            case 2: // right movement
                if(newHead.getX() + 1 > this.gridSizeX) {
                    newHead.setX(0);
                } else {
                    newHead.updatePoint(1, 0);
                }
                break;

            case 3: // down movement
                if(newHead.getY() - 1 < 0) {
                    newHead.setY(this.gridSizeY-1);
                } else {
                    newHead.updatePoint(0, -1);
                }
                break;
        
            default:
                break;
        }


        // remove tail
        this.body.remove(this.body.size() - 1); 
    }

    public boolean collision() {
        boolean collision = false;
        for (int i = 0; i < this.body.size(); i++){
            if(this.body.get(0).equals(this.body.get(i))){
                collision = true;
                break;
            }else{
                collision = false;
            }
        }
        return collision;
    }

    public void grow() {
        
    }
    
    public Point getHead() {
        return this.body.get(0);
    }

    public Point getPoint(int i){
        return this.body.get(i);
    }

    public int getLength(){
        return this.body.size();
    }
}