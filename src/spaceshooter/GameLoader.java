package spaceshooter;

import java.util.*;
import java.util.Map;
import javafx.stage.Stage;
import javafx.scene.Scene; 
import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.input.KeyCode;
import javafx.application.Application;
import javafx.animation.AnimationTimer;

public class GameLoader extends Application {
    int old_points=0;
    int new_points=0;
    int death_count=0;
    static List<Bullet> bullet_list=new ArrayList<>();
    static List<Asteroid> asteroid_list=new ArrayList<>();
    boolean end=false,pause=false;
    public final static int WIDTH=800;
    public final static int HEIGHT=600;
    String[] args;
    
    GameLoader(String[] args){
        this.args = args;
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        StackPane root = new StackPane();
        
        Image image = new Image("file:D:\\Projects\\SpaceShooter\\src\\spaceshooter\\images\\background\\0.png");
        BackgroundImage bg_img=new BackgroundImage(image,BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.DEFAULT,BackgroundSize.DEFAULT);
        Background bg=new Background(bg_img);
        
        old_points=Integer.parseInt(args[1]);
        death_count=Integer.parseInt(args[3]);
        Text old_score=new Text(10,20,"Old Score : "+old_points);
        Text new_score=new Text(10,40,"New Score : "+new_points);
        old_score.setFont(new Font("Arial",15));
        old_score.setFill(Color.WHITE);
        new_score.setFont(new Font("Arial",15));
        new_score.setFill(Color.WHITE);
        
        PlayerShip ps=new PlayerShip((WIDTH/2)-25,(HEIGHT/2)-50);
        
        Pane pane = new Pane();
        pane.setBackground(bg);
        pane.getChildren().add(old_score);
        pane.getChildren().add(new_score);
        pane.getChildren().add(ps.getObject());
        
        for(int i=0;i<12;i++)
            addAsteroid(pane);
        
        root.getChildren().add(pane);
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setTitle("Space Shooter");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        Map<KeyCode,Boolean> pressedKeys=new HashMap<>();
        scene.setOnKeyPressed(e->{pressedKeys.put(e.getCode(),true);});
        scene.setOnKeyReleased(e->{pressedKeys.put(e.getCode(),false);});
        AnimationTimer timer=new AnimationTimer(){
            public void handle(long now){
                try{
                    if(pressedKeys.getOrDefault(KeyCode.A,false))
                        ps.turnLeft();
                    if(pressedKeys.getOrDefault(KeyCode.D,false))
                        ps.turnRight();
                    if(pressedKeys.getOrDefault(KeyCode.W,false))
                        ps.accelerate(0.05);
                    if(pressedKeys.getOrDefault(KeyCode.S,false))
                        ps.decelerate();
                    if(pressedKeys.getOrDefault(KeyCode.R,false))
                        ps.resetPosition((WIDTH/2)-25,(HEIGHT/2)-50);
                    scene.setOnKeyPressed(e->{
                        pressedKeys.put(e.getCode(),true);
                        if(pressedKeys.getOrDefault(KeyCode.SPACE,false) && !end && !pause){
                            try{
                                Bullet b=new Bullet (ps.getObject().getTranslateX(),ps.getObject().getTranslateY());
                                b.getObject().setRotate(ps.getObject().getRotate());
                                bullet_list.add(b);
                                pane.getChildren().add(b.getObject());
                            }
                            catch(Exception ex){}
                        }
                        if(pressedKeys.getOrDefault(KeyCode.M,false)){
                            pause=true;
                            stop();
                        }
                        if(pressedKeys.getOrDefault(KeyCode.N,false) && !end){
                            pause=false;
                            start();
                        }
                    });
                    ps.move();
                    asteroid_list.forEach(a->a.move());
                    bullet_list.forEach(b->b.move());
                    
                    asteroid_list.forEach(a->{
                        if(a.collide(ps.getObject())){
                            try{
                                ShipModel blasted=new ShipModel (
                                                                    ps.getObject().getTranslateX()-30,
                                                                    ps.getObject().getTranslateY()-30,
                                                                    "file:D:\\Projects\\SpaceShooter\\src\\spaceshooter\\images\\player\\blast.png",
                                                                    120
                                                                );
                                pane.getChildren().add(blasted.getObject());
                                ps.remove(pane);
                                end=true;
                                if(!args[0].equals("Guest"))
                                {
                                    DatabaseConnector dbc = new DatabaseConnector();
                                    dbc.createConnection();
                                    int asteroids=new_points/500;
                                    if(new_points<old_points)
                                        new_points=old_points;
                                    dbc.updatePoints(args[0],new_points,asteroids,death_count+1);
                                    dbc.closeConnection();
                                }
                                stop();
                            }
                            catch(Exception ex){}
                        }
                        if(a.isOutOfBounds())
                            a.remove(pane);

                        bullet_list.forEach(b->{
                            if(a.collide(b.getObject())){
                                a.remove(pane);
                                b.remove(pane);
                                new_score.setText("New Score : "+addPoints(new_points));
                            }
                            if(b.isOutOfBounds())
                                b.remove(pane);
                        });
                    });
                }
                catch(Exception ex){}
                if(Math.random()<0.039)
                    addAsteroid(pane);
            }
        };
        timer.start();

    }

     void addAsteroid(Pane pane){
        Asteroid a=new Asteroid();
        asteroid_list.add(a);
        pane.getChildren().add(a.getObject());
    }
     
     int addPoints(int pnts){
        new_points=pnts+500;
        return new_points;
    }
}