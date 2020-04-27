import java.util.ArrayList;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class SnakeJavaFx extends Application {

    static int speed = 1;
    // static int foodColor = 0;
    static int width = 20;
    static int height = 20;
    static int foodX = 0;
    static int foodY = 0;
    static int cornerSize = 25;
    static ArrayList<Corner> snake = new ArrayList<Corner>();
    static Dir direction = Dir.left;
    static boolean gameOver = false;
    static Random rand = new Random();

    public enum Dir {
        left, right, up, down
    }

    public static class Corner {
        int x;
        int y;

        public Corner(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public void start(Stage stage) {
        VBox root = new VBox();
        Canvas canvas = new Canvas(width * cornerSize, height * cornerSize);
        Scene scene = new Scene(root, width * cornerSize, height * cornerSize);
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Font theFont = Font.font("Helvetica", FontWeight.BOLD, 24);
        gc.setFont(theFont);
        // gc.setStroke(Color.BLACK);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            if (key.getCode() == KeyCode.W) {
                direction = Dir.up;
            }
            if (key.getCode() == KeyCode.A) {
                direction = Dir.left;
            }
            if (key.getCode() == KeyCode.S) {
                direction = Dir.down;
            }
            if (key.getCode() == KeyCode.D) {
                direction = Dir.right;
            }
        });
        snake.add(new Corner(width / 2, height / 2));
        snake.add(new Corner(width / 2, height / 2));
        snake.add(new Corner(width / 2, height / 2));
        stage.setScene(scene);
        stage.setTitle("SNAKE GAME");
        addFood();
        // animations
        new AnimationTimer() {
            long lastNanoTime = 0;

            @Override
            public void handle(long currentNanoTime) {
                // if (lastNanoTime == 0) {
                //     lastNanoTime = currentNanoTime;
                //     return;
                // }
                if ((currentNanoTime - lastNanoTime) > 1000000000 / speed) {
                    lastNanoTime = currentNanoTime;
                    snakeFrames(gc);
                }
            }
        }.start();

        stage.show();
    }

    public static void snakeFrames(GraphicsContext gc) {
        if (gameOver) {
            gc.setFill(Color.RED);
            gc.setFont(new Font("Helvetica", 50));
            gc.fillText("GAME OVER", 100, 250);
            // gc.strokeText("GAME OVER", 100, 250);
            return;
        }
        for (int i = snake.size() - 1; i > 0; i--) {
            snake.get(i).x = snake.get(i - 1).x;
            snake.get(i).y = snake.get(i - 1).y;
        }
        switch (direction) {
            case up:
                snake.get(0).y--;
                if (snake.get(0).y < 0) {
                    gameOver = true;
                }
                break;
            case left:
                snake.get(0).x--;
                if (snake.get(0).x < 0) {
                    gameOver = true;
                }
                break;
            case down:
                snake.get(0).y++;
                if (snake.get(0).y > height) {
                    gameOver = true;
                }
                break;
            case right:
                snake.get(0).x++;
                if (snake.get(0).x > width) {
                    gameOver = true;
                }
                break;

            default:
                break;
        }
        // eat food case
        if (foodX == snake.get(0).x && foodY == snake.get(0).y) {
            snake.add(new Corner(-1, -1));
            addFood();
        }
        // self destroy case
        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y) {
                gameOver = true;
                break;
            }
        }
        // fill background
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width * cornerSize, height * cornerSize);
        // score
        gc.setFill(Color.WHITE);
        String scoreText = "Score : " + (speed - 2);
        gc.fillText(scoreText, 10, 30);
        gc.setFill(new Color(Math.random(), Math.random(), Math.random(), 1));
        gc.fillOval(foodX * cornerSize, foodY * cornerSize, cornerSize, cornerSize);
        for (Corner corner : snake) {
            gc.setFill(Color.LIGHTGREEN);
            gc.fillRect(corner.x *cornerSize, corner.y*cornerSize, cornerSize - 1, cornerSize - 1);
            gc.setFill(Color.GREEN);
            gc.fillRect(corner.x *cornerSize, corner.y*cornerSize, cornerSize - 2, cornerSize - 2);
            
        }
    }

    public static void addFood() {
        foodX = rand.nextInt(width);
        foodY = rand.nextInt(height);
        for (Corner corner : snake) {
            if (corner.x == foodX && corner.y == foodY) {
                addFood();
                break;
            }
        }
        speed++;
    }

    public static void main(String[] args) {
        launch(args);
    }
}