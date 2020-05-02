import java.util.ArrayList;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FlappyBirdJavaFx extends Application {
    private static final int width = 800;
    private static final int height = 600;
    private boolean gameStarted;
    private boolean gameDefaults;
    private Bird bird = new Bird(50, 25);
    private ArrayList<Tower> towers = new ArrayList<Tower>();
    private int towerWidth = 50;
    private int towerHeight = 500;
    private int spawnPos = width * 5 / 8;
    private int score = 0;

    // tower to obstruct path of bird
    static class Tower {
        double xPos;
        double yPos;
        double width;
        double height;

        Tower() {
        }

        Tower(double width, double height) {
            xPos = 0;
            yPos = 0;
            this.width = width;
            this.height = height;
        }

        void setXPos(double x) {
            xPos = x;
        }

        void setYPos(double y) {
            yPos = y;
        }

        void setXYPos(double x, double y) {
            xPos = x;
            yPos = y;
        }

        void moveLeftUpdate(double y) {
            xPos -= y;
        }

        Rectangle2D getBoundry() {
            return new Rectangle2D(xPos, yPos, width, height);
        }

        void render(GraphicsContext gc) {
            gc.fillRect(xPos, yPos, width, height);
        }

        public String toString() {
            return " Position: [" + xPos + "," + yPos + "]";
        }
    }
    // bird to follow specific gaps in tower to continue journey
    // bird extends tower because to inherits all basic methods and properties of tower
    static class Bird extends Tower {
        double xVelocity;
        double yVelocity;

        Bird(double width, double height) {
            super(width, height);
        }

        void setVelocity(double x, double y) {
            xVelocity = x;
            yVelocity = y;
        }

        void update() {
            xPos += xVelocity;
            yPos += yVelocity;
        }

        void render(GraphicsContext gc) {
            gc.fillOval(xPos, yPos, width, height);
        }

        public String toString() {
            return " Position: [" + xPos + "," + yPos + "]" + " Velocity: [" + xVelocity + "," + yVelocity + "]";
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("FLAPPY BIRD");
        // creating play area canvas
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Timeline t1 = new Timeline(new KeyFrame(Duration.millis(10), e -> run(gc)));
        t1.setCycleCount(Timeline.INDEFINITE);
        // setting bird and towers

        // canvas.setOnMouseMoved(e -> p1.setPositionY(e.getY() - p1.height / 2));
        EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!gameStarted) {
                    gameStarted = true;
                    return;
                }
                bird.setVelocity(0, bird.yVelocity - 9);
            }
        };
        canvas.setOnMouseClicked(mouseHandler);
        stage.setScene(new Scene(new StackPane(canvas)));
        stage.show();
        t1.play();
    }
    /**
     * 
     * @param gc Graphics context to handle all graphics work
     */
    private void run(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(25));
        // gameplay
        if (gameStarted) {
            bird.update();
            bird.setVelocity(0, bird.yVelocity + 0.1);
            spawnPos -= 1;
            if (spawnPos < (width * 5 / 8)) {
                spawnPos = width;
                int randYPos = new Random().nextInt(500);
                randYPos += 75;
                int randGap = new Random().nextInt(height / 10);
                randGap += 75;
                Tower up = new Tower(towerWidth, towerHeight);
                up.setXYPos(width, randYPos - randGap - up.height);
                Tower down = new Tower(towerWidth, towerHeight);
                down.setXYPos(width, randYPos + randGap);
                towers.add(up);
                towers.add(down);
            }
            // tower moves towards bird if towers passes untouched it is removed
            for (int i = 0; i < towers.size(); i++) {
                towers.get(i).moveLeftUpdate(1);
                if (towers.get(i).xPos < 0) {
                    towers.remove(i);
                    score++;
                }
            }
            if(bird.yPos > height - bird.height || bird.yPos < 0){
                gameStarted = false;
                gameDefaults = false;
            }
            for (Tower tower : towers) {
                // collision between tower and bird
                if(intersects(tower, bird)){
                    gameStarted = false;
                    gameDefaults = false;
                }
            }
        } else {
            // start game on mouse click
            gc.setStroke(Color.WHITE);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.strokeText("on Click", width / 2, height / 2);
            if (!gameDefaults) {
                gameDefaults = true;
                setDefaults();
            }
        }
        for (Tower tower : towers) {
            tower.render(gc);
        }
        gc.fillText((int) (score / 2) + "", 50, 50);
        bird.render(gc);
    }
    /**
     * 
     * @param tower object parameter of Tower class
     * @param bird  object parameter of Bird class
     * @return
     */
    private boolean intersects(Tower tower, Bird bird) {
        return tower.getBoundry().intersects(bird.getBoundry());
    }
    /**
     * set default for tower, bird and scores
     */
    private void setDefaults() {
        bird.setVelocity(0, 0);
        bird.setXPos(100);
        bird.setYPos(height / 2);
        towers = new ArrayList<Tower>();
        Tower up = new Tower(towerWidth, towerHeight);
        up.setXYPos(500, -300);
        Tower down = new Tower(towerWidth, towerHeight);
        down.setXYPos(500, 400);
        towers.add(up);
        towers.add(down);
        score = 0;
        spawnPos = width * 5 / 8;
    }
}