/*
Ismat Syah Imran
Mr. Tully
Period 4
 */

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.Random;

public class ShapeSmasher extends Application
{
    // sets the number of updates / frames per second
    private int updatesPerSecond = 35;

    // number of updates
    private long updateCount;

    private Rectangle square;
    private Ellipse circle;
    private Polygon triangle;

    //keeps track of time until next update for each shape
    private long squareUpdate;
    private long circleUpdate;
    private long triangleUpdate;

    private double width = 50;
    private double height = 50;

    private double x;
    private double y;

    private int skulls;
    private int score;

    private Canvas canvas;

    private Random random = new Random();

    private Image skull;

    long timeBetweenUpdates = 1000/updatesPerSecond;
    long startTime = System.nanoTime();

    @Override
    public void start(Stage primaryStage) throws Exception {
        skull = new Image("File:Images/Skull.png");

        Group group = new Group();

        canvas = new Canvas(500,500);
        group.getChildren().add(canvas);



        canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                controls(event);
            }
        });
        Scene scene = new Scene(group);
        primaryStage.setScene(scene);
        resetGame();
        primaryStage.show();

        new AnimationTimer() {
            public void handle (long currentTime) {
                if(skulls < 3) {
                    long updatesNeeded = ((currentTime - startTime) / 1000000) / timeBetweenUpdates;
                    for (; updateCount < updatesNeeded; updateCount++) {
                        update();
                    }
                    draw(canvas.getGraphicsContext2D());
                }
                else
                    startTime = System.nanoTime();

            }
        }.start();
    }

    public void controls(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();
        if(event.getButton().name().equals("PRIMARY")) {
            //moves square if clicked
            if (square.contains(x, y)) {
                resetSquare();
                score += 1;
            }
            //moves circle if clicked
            else if (circle.contains(x, y)) {
                resetCircle();
                score += 3;
            }
            //moves triangle if clicked
            else if (triangle.contains(x, y)) {
                resetTriangle();
                score += 5;
            }
        }
        //resets game if game is over and right button is pressed
        else if(event.getButton().name().equals("SECONDARY") && skulls >= 3) {
            skulls = 0;
            resetGame();
        }
    }

    public void resetGame() {
        updateCount = 0;
        x = 0;
        y = 0;
        square = new Rectangle(x,y,width,height);
        circle = new Ellipse(x-width/2,y-height/2,width/2,height/2);
        triangle = new Polygon(x,y,x+width/2,y+height,x-width/2,y+height);
        resetSquare();
        resetCircle();
        resetTriangle();
        skulls = 0;
        score = 0;


        System.out.println("RESET");
    }

    public void resetSquare() {
        //moves square until it finds an empty spot
        do {
            square.setX(random.nextInt(450));
            square.setY(random.nextInt(410) + 40);
        } while(square.intersects(circle.getBoundsInLocal()) || square.intersects(triangle.getBoundsInLocal()));
        //resets the timer
        squareUpdate = 0;
    }

    public void resetCircle() {
        //moves circle until it finds an empty spot
        do {
            circle.setCenterX(random.nextInt(450) + width/2);
            circle.setCenterY(random.nextInt(410) + height/2 + 40);
        } while(circle.intersects(square.getBoundsInLocal()) || circle.intersects(triangle.getBoundsInLocal()));
        //resets the timer
        circleUpdate = 0;
    }

    public void resetTriangle() {
        //moves triangle until it finds an empty spot
        do {
            x = random.nextInt(450) + width/2;
            y = random.nextInt(410) + 40;
            triangle = new Polygon(x,y,x+24,y+50,x-24,y+50);
        } while(triangle.intersects(square.getBoundsInLocal()) || triangle.intersects(circle.getBoundsInLocal()));
        //resets the timer
        triangleUpdate = 0;
    }

    public void update()
    {

        //moves square or increases timer
        if(squareUpdate >= updatesPerSecond*3) {
            resetSquare();
            skulls++;
        }
        else
            squareUpdate++;

        //moves circle or increases timer
        if(circleUpdate >= updatesPerSecond*2.5) {
            resetCircle();
            skulls++;
        }
        else
            circleUpdate++;

        //moves triangle or increases timer
        if(triangleUpdate >= updatesPerSecond*2) {
            resetTriangle();
            skulls++;
        }
        else
            triangleUpdate++;
    }

    public void draw(GraphicsContext g)
    {
        g.setFill(Color.BLACK);
        g.fillRect(0,0,500,500);

        //draws square
        g.setFill(Color.GREEN);
        g.fillRect(square.getX(),square.getY(),square.getWidth(),square.getHeight());


        //draws circle
        g.setFill(Color.BLUE);
        g.fillOval(circle.getCenterX()-circle.getRadiusX(),circle.getCenterY()-circle.getRadiusY(),2*circle.getRadiusX(),2*circle.getRadiusY());


        //draws triangle
        g.setFill(Color.RED);
        double[] xPoints = new double[triangle.getPoints().size()/2];
        double[] yPoints = new double[triangle.getPoints().size()/2];
        for(int i = 0; i < triangle.getPoints().size(); i += 2) {
            xPoints[i / 2] = triangle.getPoints().get(i);
            yPoints[i / 2] = triangle.getPoints().get(i + 1);
        }
        g.fillPolygon(xPoints,yPoints,triangle.getPoints().size()/2);

        g.setFill(Color.WHITE);
        g.setFont(new Font("Times New Roman", 25));

        //displays skulls
        g.fillText("Skulls: ",10,30);
        for(int i = 0; i < skulls; i++) {
            g.drawImage(skull,i * 35 + 90,5,35,35);
        }

        //displays score
        g.fillText("Score: " + score, 360,30);

        //displays game over
        if(skulls >= 3) {
            g.setFont(new Font("Times New Roman", 50));
            g.fillText("Game Over",135,250);
        }
    }
}