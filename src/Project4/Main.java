package Project4;
/**
 * Main class / entry point for the program.  Sets up the scene
 * and starts the animation timer
 *
 * @authors A. Pedregon, J. Lusby
 * @date 03/24/19
 * @version 1.0
 */
import javafx.animation.AnimationTimer;
import javafx.application.Application;package Project4;
/**
 * Main class / entry point for the program.  Sets up the scene
 * and starts the animation timer
 *
 * @authors A. Pedregon, J. Lusby
 * @date 03/24/19
 * @version 1.0
 */
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) throws Exception{
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Mobile Agents");
        GraphDisplay graph = new GraphDisplay();
        Group root = new Group(graph.createDisplay());
        Scene scene = new Scene(root, GraphDisplay.displayWidth, GraphDisplay.displayHeight);
        primaryStage.setScene(scene);
        primaryStage.show();

        new Controller().readGraph();

        AnimationTimer a = new AnimationTimer() {
            @Override
            public void handle(long now) {
                graph.setUp();
                System.out.println("main animation is runn" +
                        "ing");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("Interupted: " + e);
                }
            }
        };
        a.start();
    }
}
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) throws Exception{
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Mobile Agents");
        GraphDisplay graph = new GraphDisplay();
        Group root = new Group(graph.createGraph());
        Scene scene = new Scene(root, GraphDisplay.displayWidth, GraphDisplay.displayHeight);
        primaryStage.setScene(scene);
        primaryStage.show();

        new Controller().readGraph();
        graph.setUp();

        AnimationTimer a = new AnimationTimer() {
            @Override
            public void handle(long now) {
                System.out.println("main animation is running");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("Interupted: " + e);
                }
            }
        };
        a.start();
    }
}