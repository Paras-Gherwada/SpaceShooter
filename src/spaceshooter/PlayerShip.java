package spaceshooter;

class PlayerShip extends ShipModel
{
    PlayerShip(double x,double y) throws Exception{
        super(x,y,"file:D:\\Projects\\SpaceShooter\\src\\spaceshooter\\images\\player\\0.png",39);
    }
    
    void move()
    {
        super.move();
        double Xpos=this.getObject().getTranslateX();
        double Ypos=this.getObject().getTranslateY();
        if(Xpos<0)
            resetPosition(0,Ypos);
        if(Xpos>760)
            resetPosition(760,Ypos);
        if(Ypos<0)
            resetPosition(Xpos,0);
        if(Ypos>560)
            resetPosition(Xpos,560);
    }
}
