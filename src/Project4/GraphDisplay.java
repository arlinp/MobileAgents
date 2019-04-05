package Project4;
/**
 * Class to create the graph for the graph / sensor network
 *
 * @authors A. Pedregon, J. Lusby
 * @date 03/24/19
 * @version 1.0
 */
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class GraphDisplay {
    public static Pane display;
    public static Pane graph;
    public static FlowPane stationLog;
    public static ArrayList<Node> nodes;
    public static int displayWidth = 800;
    public static int displayHeight = 600;

    /**
     * Constructor for the graph graph.
     * @return display
     */
    public Pane createDisplay(){
        display = new Pane();
        display.setPrefSize(displayWidth, displayHeight);
        display.setStyle("-fx-background-color:  #003300;");
        display.getChildren().addAll(createGraph(), createStationLog());
        return display;
    }

    /**
     * Creates the pane to display the graph
     * @return graph pane
     */
    public Pane createGraph(){
        graph = new Pane();
        graph.setPrefSize(displayWidth, 400);
        graph.setStyle("-fx-background-color:  #003300;");
        return graph;
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
    public void createNode(int x, int y, Color color){
        Circle circle = new Circle(15, color);
        circle.relocate((x*80)+100, (y*80)+20);
        graph.getChildren().add(circle);
        for(int i = 0; i < Controller.sensors.size(); i++){
            if(Controller.sensors.get(i).getX() == x && Controller.sensors.get(i).getY() == y){
                if(Controller.sensors.get(i).hasAgent){
                    Circle agent = new Circle(7, Color.BLACK);
                    agent.relocate((x*80)+110, (y*80)+30);
                    graph.getChildren().add(agent);
                }
            }
        }
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
        graph.getChildren().add(line);
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
        nodes = Controller.sensors;
        //Loop through sensor list and add nodes to display
        for(int x  = 0; x < nodes.size(); x++){
            Node node = nodes.get(x);
            if(node.isStation()){
                createNode(node.getX(), node.getY(), Color.GREEN);
            }

            if (node.isFire()){
                for(int i = 0; i < Controller.edges.size(); i++){
                    if((Controller.edges.get(i).getEndX() == node.getX()) && (
                            Controller.edges.get(i).getEndY() == node.getY()) &&
                            !node.isFire()){
                        createNode(Controller.edges.get(i).getStartX(), Controller.edges.get(i).getStartY(), Color.YELLOW);
                        for(int y  = 0; y < nodes.size(); y++) {
                            if(nodes.get(y).getX() == Controller.edges.get(i).getStartX() && 
                                    nodes.get(y).getY() == Controller.edges.get(i).getStartY()){
                                nodes.get(y).setNearFire();
                            }
                        }
                    }
                    if((Controller.edges.get(i).getStartX() == node.getX()) && 
                            (Controller.edges.get(i).getStartY() == node.getY())){
                        createNode(Controller.edges.get(i).getEndX(), Controller.edges.get(i).getEndY(), Color.YELLOW);
                        for(int y  = 0; y < nodes.size(); y++) {
                            if(nodes.get(y).getX() == Controller.edges.get(i).getEndX() && 
                                    nodes.get(y).getY() == Controller.edges.get(i).getEndY()){
                                nodes.get(y).setNearFire();
                            }
                        }
                    }
                }
                createNode(node.getX(), node.getY(), Color.RED);
            }
            if(node.isNearFire()){
                createNode(node.getX(), node.getY(), Color.YELLOW);
            }
        }

        for(int x  = 0; x < nodes.size(); x++) {
            Node node = nodes.get(x);
            if (!node.isStation() && !node.isFire() && !node.isNearFire()) {
                createNode(node.getX(), node.getY(), Color.BLUE);
            }
        }

        //Loop through the edge list and add create lines between nodes
        for(int x = 0; x < Controller.edges.size(); x++){
            createLine(Controller.edges.get(x));
        }
    }

    public void update(){
    }
}