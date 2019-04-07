package Project4;
/**
 * Main class / entry point for the program.  Sets up the scene
 * and starts the animation timer
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
        Group root = new Group(graph.createGraph());

        Scene scene = new Scene(root, graph.displayWidth, graph.displayHeight);
        primaryStage.setScene(scene);
        primaryStage.show();

        Controller controller = new Controller(graph);
        controller.readGraph();

        graph.setUp();
        graph.start();

        controller.startThreads();

    }
}