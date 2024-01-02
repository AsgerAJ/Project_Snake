
package src.gui;
public class Main {
    public static void main(String[] args) {
        int gridSizeX = 5;
        int gridSizeY = 5;
        boolean[][] visualGrid = new boolean[gridSizeX][gridSizeY];
        primitivGrafix(visualGrid);
        Snake test = new Snake(gridSizeX/2, gridSizeY/2);
    }

    public static void primitivGrafix(boolean[][] visualGrid) {
        for (int i = 0; i < visualGrid.length; i++) {
            for (int j = 0; j < visualGrid[i].length; j++) {
                System.out.print(visualGrid[i][j] + ", ");
            }
            System.out.println();        
        }
    }
}
