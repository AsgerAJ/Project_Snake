import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Food extends Rectangle {
    private double scalingConstant;
    
    public Food(int x, int y, double scalingConstant){
        super(x*scalingConstant-scalingConstant, y*scalingConstant-scalingConstant, scalingConstant, scalingConstant);
        super.setArcHeight(scalingConstant);
        super.setArcWidth(scalingConstant);
        super.setFill(Color.rgb(194,1,20));
        this.scalingConstant = scalingConstant;
    }

    public double getcS(){
        return this.scalingConstant;
    }

    public void setXY(int x, int y){
        setX(x*scalingConstant-scalingConstant);
        setY(y*scalingConstant-scalingConstant);
        //System.out.println(x + " : " + y);
    }

}
