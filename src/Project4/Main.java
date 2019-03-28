package Project4;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.nio.file.Paths;
import java.util.Hashtable;


public class Main extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Text text = new Text("Hello World!");
        Controller controller = new Controller();
        controller.processGraph(Paths.get("src/Project4/MediumGraph.txt"));

        Hashtable myHashtable = controller.getNodes();

//        GraphDisplay graphic = new GraphDisplay();
//        graphic.setSize(400, 300);
//        graphic.setVisible(true);
//
//        Group root = new Group();
//        //root.getChildren().addAll(graphic);
//        Scene scene = new Scene(root, 700, 700);
//                // Add root to stage
//        primaryStage.setScene(scene);
//        primaryStage.show();

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
