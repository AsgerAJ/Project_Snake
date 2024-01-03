package src.gui;

import src.backend.*;


public class Main {
    private Direction LEFT, RIGHT, UP, DOWN;
    public static void main(String[] args) {
        int n = 11;
        int m = 11;
        boolean[][] visualGrid = new boolean[n][m];
        Snake test = new Snake(n / 2, m / 2, n, m);
        primitivGrafix(visualGrid, test);
    }

    public static void primitivGrafix(boolean[][] visualGrid, Snake test) throws IndexOutOfBoundsException {
        for (int k = 0; k < 10; k++) {
            for(int lol = 0; lol < test.getLength(); lol++) {
                System.out.print(test.getPoint(lol).toString() + " ");
            }
            System.out.println();
            //System.out.println(test.getHead().toString());
            int counter = 1;
            for (int i = 0; i < visualGrid.length; i++) {
                for (int j = 0; j < visualGrid[i].length; j++) {
                    if ((test.getHead().getX() == j) && (test.getHead().getY() == i)) {
                        System.out.print("Head" + " , ");
                        if (counter != 1 && counter != test.getLength()) {
                            counter++;
                        }
                    } else if (new Point(j, i).equals(test.getPoint(counter))) {
                        System.out.print("Body" + " , ");
                        if (counter != 1 && counter != test.getLength()) {
                            counter++;
                        }
                    } else {
                        System.out.print(visualGrid[j][i] + ", ");
                    }

                }
                System.out.println();
                System.out.println();
            }
            test.update(test.gDirection());
            System.out.println();
        }

    }
}
