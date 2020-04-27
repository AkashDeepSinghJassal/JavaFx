import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PongJavaFx extends Application {
    private static final int width = 800;
    private static final int height = 600;
    private boolean gameStarted;
    private Bar p1 = new Bar(15, 100);
    private Bar p2 = new Bar(15, 100);
    private Ball ball = new Ball(15);

    static class Bar {
        double positionX;
        double positionY;
        double width;
        double height;
        int score;

        Bar(double width, double height) {
            positionX = 0;
            positionY = 0;
            this.width = width;
            this.height = height;
            score = 0;
        }

        void setPositionX(double x) {
            positionX = x;
        }

        void setPositionY(double y) {
            positionY = y;
        }

        void render(GraphicsContext gc) {
            gc.fillRect(positionX, positionY, width, height);
        }

        public String toString() {
            return " Position: [" + positionX + "," + positionY + "]";
        }
    }

    static class Ball extends Bar {
        double velocityX;
        double velocityY;

        Ball(double radius) {
            super(radius, radius);
        }

        void setVelocity(double x, double y) {
            velocityX = x;
            velocityY = y;
        }

        void update() {
            positionX += velocityX;
            positionY += velocityY;
        }

        void render(GraphicsContext gc) {
            gc.fillOval(positionX, positionY, width, height);
        }

        public String toString() {
            return " Position: [" + positionX + "," + positionY + "]" + " Velocity: [" + velocityX + "," + velocityY
                    + "]";
        }
    }

    public void start(Stage stage) throws Exception {
        stage.setTitle("PONG");
        // creating play area canvas
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Timeline t1 = new Timeline(new KeyFrame(Duration.millis(10), e -> run(gc)));
        t1.setCycleCount(Timeline.INDEFINITE);
        // initialising objects ie player 1, 2 and ball
        p1.setPositionX(0);
        p2.setPositionX(width - p2.width);
        p1.setPositionY(height / 2);
        p2.setPositionY(height / 2);
        // mouse Control
        canvas.setOnMouseMoved(e -> p1.setPositionY(e.getY() - p1.height / 2));
        canvas.setOnMouseClicked(e -> gameStarted = true);
        stage.setScene(new Scene(new StackPane(canvas)));
        stage.show();
        t1.play();
    }

    private void run(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(25));
        // gameplay
        if (gameStarted) {
            ball.update();
            if (ball.positionX < width - width / 4) {
                p2.setPositionY(ball.positionY - p2.height / 2);
            } else {
                // computer controls with bit error
                // p2positionY variable to simplyfy code
                double p2positionY = p2.positionY;
                p2positionY = ball.positionY > p2positionY + p2.height / 2 ? p2positionY + 2 : p2positionY - 2;
                p2.setPositionY(p2positionY);
            }
            // bounds for ball
            if (ball.positionY > height - (ball.height) || ball.positionY < 0) {
                ball.setVelocity(ball.velocityX, ball.velocityY * -1);
            }
            if (ball.positionX > width) {
                p1.score++;
                gameStarted = false;
            }
            if (ball.positionX < 0) {
                p2.score++;
                gameStarted = false;
            }
            // detect collision by bar and ball
            if ((ball.positionX + ball.width > p2.positionX && ball.positionY + ball.height / 2 > p2.positionY
                    && ball.positionY - ball.height / 2 < p2.positionY + p2.height)
                    || (ball.positionX < p1.positionX + p1.width && ball.positionY + ball.height / 2 > p1.positionY
                            && ball.positionY - ball.height / 2 < p1.positionY + p1.height)) {
                // increaase velocity
                double ballVelocityX = ball.velocityX + Math.signum(ball.velocityX) / 4;
                double ballVelocityY = ball.velocityY + Math.signum(ball.velocityY) / 4;
                ball.setVelocity(ballVelocityX * -1, ballVelocityY);
            }
            ball.render(gc);
        } else {
            gc.setStroke(Color.WHITE);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.strokeText("on Click", width / 2, height / 2);
            // random position and direction of ball
            ball.setPositionX(width / 2);
            ball.setPositionY(new Random().nextInt(height - 100) + 50);
            double ballVelocityX = new Random().nextInt(2) == 0 ? 2 : -2;
            double ballVelocityY = new Random().nextInt(2) == 0 ? 2 : -2;
            ball.setVelocity(ballVelocityX, ballVelocityY);
        }
        gc.fillText(p1.score + "", 200, 100);
        gc.fillText(p2.score + "", 600, 100);
        p1.render(gc);
        p2.render(gc);
    }

    public static void main(String[] args) {
        launch(args);
    }
}