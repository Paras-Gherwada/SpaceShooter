package spaceshooter;

import java.io.*;
import javafx.geometry.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

class ShipModel
{
    private ImageView object;
    private Point2D movement;

    ShipModel(double x,double y,String path,int size) throws Exception
    {
        this.object=new ImageView();
        this.object.setFitWidth(size);
        this.object.setFitHeight(size);
        this.object.setImage(new Image(path));
        this.object.setTranslateX(x);
        this.object.setTranslateY(y);
        this.movement=new Point2D(0,0);
    }

    ImageView getObject()
    {
        return this.object;
    }

    void turnLeft()
    {
        this.object.setRotate(this.object.getRotate()-5);
    }
    void turnRight()
    {
        this.object.setRotate(this.object.getRotate()+5);
    }
    void accelerate(double constant)
    {
        double changeX=Math.cos(Math.toRadians(this.object.getRotate()))*constant;
        double changeY=Math.sin(Math.toRadians(this.object.getRotate()))*constant;
        this.movement=this.movement.add(changeX,changeY);
    }
    void decelerate()
    {
        double changeX=Math.cos(Math.toRadians(this.object.getRotate()))/25; 
        double changeY=Math.sin(Math.toRadians(this.object.getRotate()))/25;
        this.movement=this.movement.subtract(changeX,changeY);
    }
    void move()
    {
        this.object.setTranslateX(this.object.getTranslateX()+this.movement.getX());
        this.object.setTranslateY(this.object.getTranslateY()+this.movement.getY());
    }
    void resetPosition(double x,double y)
    {
        this.object.setTranslateX(x);
        this.object.setTranslateY(y);
        this.movement=new Point2D(0,0);
    }
    void remove(Pane pane)
    {
        GameLoader.bullet_list.remove(this);
        pane.getChildren().remove(this.getObject());
    }
}
