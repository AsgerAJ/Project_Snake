package src.backend;

import java.util.Random;

import src.backend.*;

public class Grid {
    private int n;
    private int m;
    private Snake snake;

    public Grid(int n, int m) {
        this.n = n;
        this.m = m;

        this.snake = new Snake(n / 2, m / 2);

        boolean goodrand = false;
        Random rand = new Random();
        while (!goodrand) {
            Point rewardLocation = new Point(rand.nextInt(n), rand.nextInt(m));
            if (!(rewardLocation.equals(snake.getPoint(0)) && rewardLocation.equals(snake.getPoint(1)))) {
                goodrand = true;
            }
        }
    }
}
