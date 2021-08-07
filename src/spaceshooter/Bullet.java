package spaceshooter;

class Bullet extends ShipModel
{
    Bullet(double x,double y) throws Exception
    {
        super(x+8,y+8,"file:D:\\Projects\\SpaceShooter\\src\\spaceshooter\\images\\bullet\\2.png",0);
    }
    void move()
    {
        accelerate(0.12);
        super.move();
    }

    boolean isOutOfBounds()
    {
        double Xpos=this.getObject().getBoundsInParent().getMaxX();
        double Ypos=this.getObject().getBoundsInParent().getMaxY();
        if(Xpos<0 || Xpos>GameLoader.WIDTH || Ypos<0 || Ypos>GameLoader.HEIGHT)
            return true;
        return false;
    }
}
