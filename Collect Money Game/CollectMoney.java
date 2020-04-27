import java.util.ArrayList;
import java.util.Iterator;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

class LongValue {
    long value;

    LongValue(long value) {
        this.value = value;
    }
}
class IntValue {
    int value;

    IntValue(int value) {
        this.value = value;
    }
}

public class CollectMoney extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {
        stage.setTitle("Collect the Money Bags!!");
        Group root = new Group();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        Canvas canvas = new Canvas(512, 512);
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        ArrayList<String> input = new ArrayList<String>();
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                String code = e.getCode().toString();
                if (!input.contains(code)) {
                    input.add(code);
                }
            }
        });
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                String code = e.getCode().toString();
                input.remove(code);
            }
        });
        Font theFont = Font.font("Helvetica", FontWeight.BOLD, 24);
        gc.setFont(theFont);
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        Sprite briefcase = new Sprite("briefcase.png");
        briefcase.setPosition(200, 200);
        // Image left = new Image( "left.png" );
        // Image leftG = new Image( "leftG.png" );
        // Image right = new Image( "right.png" );
        // Image rightG = new Image( "rightG.png" );
        ArrayList<Sprite> moneyBagList = new ArrayList<Sprite>();
        for (int i = 0; i < 5; i++) {
            Sprite moneyBag = new Sprite("moneybag.png");
            double x = 50 + 400 * Math.random();
            double y = 50 + 400 * Math.random();
            moneyBag.setPosition(x, y);
            moneyBagList.add(moneyBag);
        }
        // Image left = new Image( "left.png" );
        // Image leftG = new Image( "leftG.png" );
        // Image right = new Image( "right.png" );
        // Image rightG = new Image( "rightG.png" );
        final int speed = 50;
        IntValue points = new IntValue(0);
        LongValue lastNanoTime = new LongValue(System.nanoTime());
        new AnimationTimer() {

            @Override
            public void handle(long currentNanoTime) {
                double deltaTime = (currentNanoTime - lastNanoTime.value) / 1000000000.0;
                lastNanoTime.value = currentNanoTime;
                briefcase.setVelocity(0, 0);
                if (input.contains("LEFT")) {
                    // gc.drawImage(left, 64, 64);
                    briefcase.setVelocity(-speed, 0);
                }
                if (input.contains("RIGHT")) {
                    // gc.drawImage(right, 256, 64);
                    briefcase.setVelocity(speed, 0);
                }
                if (input.contains("UP")) {
                    // gc.drawImage(right, 256, 64);
                    briefcase.setVelocity(0, -speed);
                }
                if (input.contains("DOWN")) {
                    // gc.drawImage(right, 256, 64);
                    briefcase.setVelocity(0, speed);
                }
                for (String i : input) {
                    System.out.println(i + " " + deltaTime);
                }
                // if(!input.contains("LEFT")){
                //     gc.drawImage(left, 64, 64);
                // }
                // else{
                //     gc.drawImage(leftG, 64, 64);
                // }
                // if(!input.contains("RIGHT")){
                //     gc.drawImage(right, 256, 64);
                // }
                // else{
                //     gc.drawImage(rightG, 256, 64);
                // }
                briefcase.update(deltaTime);
                Iterator<Sprite> moneyBagIter = moneyBagList.iterator();
                while (moneyBagIter.hasNext()) {
                    Sprite moneybag = moneyBagIter.next();
                    if (briefcase.intersects(moneybag)) {
                        moneyBagIter.remove();
                        points.value++;
                    }
                }
                // render
                gc.clearRect(0, 0, 512, 512);
                briefcase.render(gc);
                for (Sprite moneyBag : moneyBagList) {
                    moneyBag.render(gc);
                }
                String pointText = "Cash: $" + (100 * points.value);
                gc.fillText(pointText, 360, 36);
                gc.strokeText(pointText, 360, 36);
            }
        }.start();
        stage.show();
    }
}