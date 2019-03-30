package Project4;
/**
 * Class to create the graph for the graph / sensor network
 *
 * @authors A. Pedregon, J. Lusby
 * @date 03/24/19
 * @version 1.0
 */
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class GraphDisplay {
    private Pane display;
    public FlowPane stationLog;
    public static int displayWidth = 800;
    public static int displayHeight = 600;

    /**
     * Constructor for the graph graph.
     * @return display
     */
    public Pane createGraph(){
        display = new Pane();
        display.setPrefSize(displayWidth, 400);
        display.setStyle("-fx-background-color:  #003300;");
        setUp();
        display.getChildren().add(createStationLog());
        return display;
    }

    /**
     * Creates a FlowPane for the base station log
     * @return stationLog
     */
    public FlowPane createStationLog(){
        stationLog = new FlowPane();
        Label label = new Label("Base Station Log: ");
        label.setTextFill(Color.RED);
        stationLog.setPrefWidth(displayWidth);
        stationLog.setPrefHeight(200);
        stationLog.setStyle("-fx-background-color:  #001a00;");
        stationLog.getChildren().add(label);
        stationLog.relocate(0, 400);
        return stationLog;
    }

    /**
     * Creates a graph node to represent a sensor in the network
     * @param x ; x coordinate of the new node
     * @param y : y coordinate of the nwe node
     * @param color : color of the node
     */
    public void createNode(int x, int y, Color color){
        Circle circle = new Circle(15, color);
        circle.relocate(x*80, y*80);
        display.getChildren().add(circle);
    }

    /**
     * Creates a line object and adds to the display
     * @param edge : line between graph nodes
     */
    public void createLine(Edge edge){
        int startX = 15 + edge.getStartX() * 80;
        int startY = 15 + edge.getStartY() * 80;
        int endX = 15 + edge.getEndX() * 80;
        int endY = 15 + edge.getEndY() * 80;
        Line line = new Line(startX, startY, endX, endY);
        line.setStroke(Color.WHITE);
        display.getChildren().add(line);
    }

    /**
     * Updates the graph
     */
    public void setUp(){
        //Loop through sensor list and add nodes to display
        for(int x  = 0; x < Controller.sensors.size(); x++){
            Node node = Controller.sensors.get(x);
            if(node.isStation()){
                createNode(node.getX(), node.getY(), Color.GREEN);
            }
            if (node.isFire()){
                createNode(node.getX(), node.getY(), Color.RED);
            }
            if(!node.isStation() && !node.isFire()){
                createNode(node.getX(), node.getY(), Color.BLUE);
            }
        }
        //Loop through the edge list and add create lines between nodes
        for(int x = 0; x < Controller.edges.size(); x++){
            createLine(Controller.edges.get(x));
        }
    }
}