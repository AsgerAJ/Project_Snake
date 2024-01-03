package src.gui;
import src.backend.*;
public class Main {

    public static void main(String[] args) {
        int n = 11;
        int m = 11;
        boolean[][] visualGrid = new boolean[n][m];
        Snake test = new Snake(n/2, m/2, n, m);
        primitivGrafix(visualGrid, test);
    }

    public static void primitivGrafix(boolean[][] visualGrid, Snake test) throws IndexOutOfBoundsException {
        int counter = 1;
        for (int i = 0; i < visualGrid.length; i++) {
            for (int j = 0; j < visualGrid[i].length; j++) {
                if((test.getHead().getX()==i)&&(test.getHead().getY()==j)){
                    System.out.print("Head" + " , ");
                    if(counter != 1 && counter != test.getLength()){
                        counter++;
                    }
                }else if(new Point(i, j).equals(test.getPoint(counter))){
                    System.out.print("Body" + " , ");
                    if(counter != 1 && counter != test.getLength()){
                        counter++;
                    }
                }else{
                    System.out.print(visualGrid[i][j] + ", ");
                }

            }
            System.out.println();
            System.out.println();
        }
    }
}
