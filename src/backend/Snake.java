package src.backend;
import java.util.*;

public class Snake {
    private int score = 0;
    public int direction = 0; // 0 <- , 1 ^ , 2 ->, 3 v
    public int gridSizeX;
    public int gridSizeY;
    public ArrayList<Point> body = new ArrayList<Point>();
    private boolean alive;
    
    public Snake(int xstart, int ystart, int gridSizeX, int gridSizeY){
        this.gridSizeX = gridSizeX;
        this.gridSizeY = gridSizeY;
        this.alive = true;
        for(int i = 0; i < 2; i++){
            int[] section = {xstart, ystart + i};
            this.body.add(section);
        }
    }

    public void update(int direction, int oldDirection) { // google har 8 steps i sekundet
        // if målfelt is clear
        // if not on egde

        // new head
        int[] oldHead = this.body.get(0);
        int[] newHead = oldHead;
        switch (direction) { // handles movement of snake.
            case 0:
                if(newHead[0] - 1 < 0) {
                    newHead[0] = this.gridSizeX - 1;
                } else {
                    newHead[0]--;
                }
                break;

            case 1:
                if(newHead[1] + 1 > this.gridSizeY) {
                    newHead[1] = 0;
                } else {
                    newHead[1]++;
                }
                break;

            case 2:
                if(newHead[0] + 1 > this.gridSizeX) {
                    newHead[0] = 0;
                } else {
                    newHead[0]++;
                }
                break;

            case 3:
                if(newHead[1] - 1 < 0) {
                    newHead[1] = this.gridSizeY - 1;
                } else {
                    newHead[1]--;
                }
                break;
        
            default:
                break;
        }


        // remove tail
        this.body.remove(this.body.size() - 1); 
    }

    public boolean collision() {
        return false;
    }

    public void grow() {
        
    }
    
    public int[] getHead() {
        return this.body.get(0);
    }
}