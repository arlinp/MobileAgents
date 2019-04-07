package Project4;
/**
 * Class to create the graph for the graph / sensor network
 *
 * @authors A. Pedregon, J. Lusby
 * @date 03/24/19
 * @version 1.0
 */
import javafx.geometry.Point2D;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Hashtable;

public class GraphDisplay {
    public static Pane display;
    public static FlowPane stationLog;
    public static ArrayList<Node> nodes;
    public static int displayWidth = 800;
    public static int displayHeight = 600;
    public Hashtable<Point2D, Node> nodeHashtable = new Hashtable();

    /**
     * Constructor for the graph graph.
     * @return display
     */
    public Pane createGraph(){
        display = new Pane();
        display.setPrefSize(displayWidth, 400);
        display.setStyle("-fx-background-color:  #003300;");
        display.getChildren().add(createStationLog());
        return display;
    }

    /**
     * Creates a FlowPane for the base station log
     * @return stationLog
     */
    public FlowPane createStationLog(){
        stationLog = new FlowPane();
        Text text = new Text("Base Station Log: ");
        text.setStroke(Color.RED);
        stationLog.setPrefWidth(displayWidth);
        stationLog.setPrefHeight(200);
        stationLog.setStyle("-fx-background-color:  #001a00;");
        stationLog.getChildren().add(text);
        stationLog.relocate(0, 400);
        return stationLog;
    }

    /**
     * Creates a graph node to represent a sensor in the network
     * @param x ; x coordinate of the new node
     * @param y : y coordinate of the nwe node
     * @param color : color of the node
     */
    public void drawNode(int x, int y, Color color){

        Circle circle = new Circle(15, color);
        circle.relocate((x*80)+100, (y*80)+20);
        display.getChildren().add(circle);

    }

    /**
     * Creates a graph node to represent a sensor in the network
     * @param x ; x coordinate of the new node
     * @param y : y coordinate of the nwe node
     */
    public void drawAgent(int x, int y){

        Circle circle = new Circle(15, null );
        circle.setStroke(Color.GOLD);
        circle.setStrokeWidth(2);
        circle.relocate((x*80)+100, (y*80)+20);

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
        Line line = new Line(startX+100, startY+20, endX+100, endY+20);
        line.setStroke(Color.WHITE);
        display.getChildren().add(line);
    }

    /**
     * Creates the message to be displayed on the base station log
     * @param name : unique Agent Id name
     * @param node : node the agent was created on
     */
    public static void createMessage(String name, Node node){
        Text newLog = new Text("\tAgent: " + name +
                " Created at (" + node.getX() + "," + node.getY() + ")\t");
        newLog.setStroke(Color.WHITE);
        stationLog.getChildren().add(newLog);
    }

    /**
     * Updates the graph
     */
    public void setUp(){

        //Loop through the edge list and add create lines between nodes
        for(int x = 0; x < Controller.edges.size(); x++){
            createLine(Controller.edges.get(x));
        }

        //Loop through node list and add nodes to display

        nodes = new ArrayList<Node>(Controller.nodes.values());

        for(int x  = 0; x < nodes.size(); x++){
            Node node = nodes.get(x);

            if(node.isStation()){
                drawNode(node.getX(), node.getY(), Color.GREEN);
            }
            if(node.getAgent() != null){
                drawAgent(node.getX(), node.getY());
            }

            else if(node.getState().equals("blue") && !node.isStation()){
                drawNode(node.getX(), node.getY(), Color.BLUE);
            }

            else if (node.getState().equals("fire")){
                drawNode(node.getX(), node.getY(), Color.RED);
            }

            else if (node.getState().equals("yellow")){
                drawNode(node.getX(), node.getY(), Color.YELLOW);
            }
        }


    }

    public void update(){
    }
}