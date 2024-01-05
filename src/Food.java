import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.*;

public class Food extends Rectangle {

    private int x;
    private int y;



    public Food(int x, int y, double scalingConstant){
        super(x*scalingConstant-scalingConstant, y*scalingConstant-scalingConstant, scalingConstant, scalingConstant);
        super.setArcHeight(scalingConstant);
        super.setArcWidth(scalingConstant);
        super.setFill(Color.RED);
    }
}
