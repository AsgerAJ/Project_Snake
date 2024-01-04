package src.backend;

import java.util.Random;

public class Rewards {
    private Point location;
    private String type;
    private Random random = new Random();
    private Point fruit;



    public  Rewards(int x, int y, Snake snake) {
        boolean badSpawn = true;
        while (badSpawn) {
            Point fruit = new Point(random.nextInt(x), random.nextInt(y));
            for(int j = 0; j < snake.getLength()-1; j++){
                if ((fruit.equals(snake.getPoint(j)))){
                    badSpawn = false;
                }
            }
        }
        return fruit;
    }

    public Point getFruitPoint(){

    }
}
