import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Sprite {
    private Image image;
    private double positionX;
    private double positionY;
    private double velocityX;
    private double velocityY;
    private double width;
    private double height;

    public Sprite(String imageString) {
        Image i = new Image(imageString);
        image = i;
        positionX = 0;
        positionY = 0;
        velocityX = 0;
        velocityY = 0;
        width = i.getWidth();
        height = i.getHeight();
    }

    public void setPosition(double x, double y) {
        positionX = x;
        positionY = y;
    }

    public void setVelocity(double x, double y) {
        velocityX = x;
        velocityY = y;
    }

    public void update(double time) {
        positionX += velocityX * time;
        positionY += velocityY * time;
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(image, positionX, positionY);
    }

    public Rectangle2D getBoundry() {
        return new Rectangle2D(positionX, positionY, width, height);
    }

    public boolean intersects(Sprite s){
        return s.getBoundry().intersects(this.getBoundry());
    }
    public String toString() {
        return " Position: [" + positionX + "," + positionY + "]" + " Velocity: [" + velocityX + "," + velocityY + "]";
    }
}