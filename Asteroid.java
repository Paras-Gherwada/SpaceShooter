import java.util.Random;
import javafx.geometry.*;
import javafx.scene.paint.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Polygon;

class Asteroid {
    private Polygon asteroid = new Polygon();
    private Point2D move;
    Random rnd = new Random();
    int[] speed = {
		-3, -2, -1, 0, 1, 2, 3
	};
    double[] XCoords = {
		0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 390, 391, 392, 393, 394, 395, 396, 397, 398, 399, 400
	};
    double[] YCoords = {
		0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 290, 291, 292, 293, 294, 295, 296, 297, 298, 299, 300
	};
    int size = (XCoords.length & YCoords.length);
    double x = 0, y = 0;

    Asteroid() {
        this.x = XCoords[rnd.nextInt(size)];
        this.y = YCoords[rnd.nextInt(size)];
        this.asteroid.getPoints().addAll(
			new Double[] {
				x + 00.0, y + 00.0,
				x + 10.0, y + 00.0,
				x + 20.0, y + 10.0,
				x + 20.0, y + 20.0,
				x + 10.0, y + 30.0,
				x + 00.0, y + 30.0,
				x - 10.0, y + 20.0,
				x - 10.0, y + 10.0
			}
		);
        this.asteroid = asteroid;
        this.asteroid.setTranslateX(x);
        this.asteroid.setTranslateY(y);
        this.asteroid.setFill(Color.RED);
        this.move = new Point2D(
            speed[rnd.nextInt(speed.length)],
            speed[rnd.nextInt(speed.length)]
        );
    }

    Polygon getObject() {
        return this.asteroid;
    }

    void move() {
        this.asteroid.setTranslateX(this.asteroid.getTranslateX() + this.move.getX());
        this.asteroid.setTranslateY(this.asteroid.getTranslateY() + this.move.getY());
        this.asteroid.setRotate(this.asteroid.getRotate() - rnd.nextInt(120));
    }

    boolean collide(ImageView iv) {
        return iv.getBoundsInParent().intersects((this.getObject()).getBoundsInParent());
        /*
        if(iv.getBoundsInParent().intersects((this.getObject()).getBoundsInParent()))
            return true;
        return false;
        */
    }

    boolean isOutOfBounds() {
        double Xpos = this.asteroid.getBoundsInParent().getMaxX();
        double Ypos = this.asteroid.getBoundsInParent().getMaxY();
        return (Xpos < 0 || Xpos > (GameLoader.WIDTH + 30) || Ypos < 0 || Ypos > (GameLoader.HEIGHT + 30));
        /*
        if(Xpos < 0 || Xpos > (GameLoader.WIDTH + 30) || Ypos < 0 || Ypos > (GameLoader.HEIGHT + 30))
            return true;
        return false;
        */
    }

    void remove(Pane pane) {
        GameLoader.asteroid_list.remove(this);
        pane.getChildren().remove(this.getObject());
    }
}