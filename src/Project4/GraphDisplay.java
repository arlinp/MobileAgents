package Project4;
/**
 * Class to create the graph for the graph / sensor network
 *
 * @authors A. Pedregon, J. Lusby
 * @date 03/24/19
 * @version 1.0
 */
import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.awt.*;
import java.util.*;
import java.util.List;

public class GraphDisplay {
    public Pane display;
    public FlowPane stationLog;
    public ArrayList<Node> nodes;
    public int displayWidth = 800;
    public int displayHeight = 600;
    public Hashtable<Point2D, Node> nodeHashtable = new Hashtable();
    private Hashtable<Node, Shape> nodeList = new Hashtable<>();
    private Hashtable<Agent, Circle> agentList = new Hashtable<>();
    private Text newAgent;


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
    public void drawNode(int x, int y, Color color, Node node){

        Circle circle = new Circle((x*80)+100, (y*80)+20,15, color);
        nodeList.put(node, circle);
        display.getChildren().add(circle);
    }

    /**
     * Creates a graph node to represent a sensor in the network
     * @param x ; x coordinate of the new node
     * @param y : y coordinate of the nwe node
     */
    public void drawAgent(int x, int y, Agent agent){

        Circle circle = new Circle(14, null );
        circle.relocate((x*80)+80, (y*80)+0 );
        circle.setStroke(Color.LIMEGREEN);
        circle.setStrokeWidth(4);
        agentList.put(agent, circle);
        display.getChildren().add(circle);


    }

    /**
     * Creates a line object and adds to the display
     * @param edge : line between graph nodes
     */
    public void createLine(Edge edge){
        int startX = edge.getStartX() * 80;
        int startY = edge.getStartY() * 80;
        int endX = edge.getEndX() * 80;
        int endY = edge.getEndY() * 80;
        Line line = new Line(startX+100, startY+20, endX+100, endY+20);
        line.setStroke(Color.WHITE);
        display.getChildren().add(line);
    }

    /**
     * Creates the message to be displayed on the base station log
     * @param name : unique Agent Id name
     * @param node : node the agent was created on
     */
    public void createMessage(String name, Node node){
        Text newLog = new Text("Agent: " + name +
                " Created at (" + node.getX() + "," + node.getY() + ")");
        newLog.setStroke(Color.WHITE);
        stationLog.getChildren().add(newLog);
    }

    /**
     * Updates the graph
     */
    public void start(){
        AnimationTimer a = new AnimationTimer() {
            @Override
            public void handle(long now) {

                update();
            }
        };
        a.start();
    }
    public void setUp(){

        //Loop through the edge list and add create lines between nodes
        for(int x = 0; x < Controller.edges.size(); x++){
            createLine(Controller.edges.get(x));
        }

        //Loop through node list and add nodes to display

        nodes = new ArrayList<>(nodeHashtable.values());
        Node nodeWithAgent = null;

        for(Node node : nodes){

            if(node.getAgent() != null){
                nodeWithAgent = node;
            }

            if(node.isStation()){
                drawNode(node.getX(), node.getY(), Color.GREEN, node);
            }


            else if(node.getState().equals("blue") && !node.isStation()){
                drawNode(node.getX(), node.getY(), Color.BLUE, node);
            }

            else if (node.getState().equals("fire")){
                drawNode(node.getX(), node.getY(), Color.RED, node);
            }

            else if (node.getState().equals("yellow")){
                drawNode(node.getX(), node.getY(), Color.YELLOW, node);
            }
        }

        if(nodeWithAgent != null) {
            drawAgent(nodeWithAgent.getX(), nodeWithAgent.getY(), nodeWithAgent.getAgent());
        }

    }

    public void update(){

        nodes = new ArrayList<>(nodeHashtable.values());
        for(Node node : nodes){
            if(node.getAgent() != null) {
                agentList.get(node.getAgent()).relocate((node.getX()*80)+85, (node.getY()*80)+5 ) ;
            }
            if(node.isStation()){
                nodeList.get(node).setFill(Color.GREEN);
            }

            else if(node.getState().equals("blue") && !node.isStation()){
                nodeList.get(node).setFill(Color.BLUE);
            }

            else if (node.getState().equals("fire")){
                nodeList.get(node).setFill(Color.RED);

            }

            else if (node.getState().equals("yellow")){
                nodeList.get(node).setFill(Color.YELLOW);

            }
        }
    }
}