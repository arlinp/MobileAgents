package Project4;
/**
 * Entry point for the program.  Sets the stage for the simulation.
 * Initializes the GUI and methods to update changes.
 * Starts the simulation.
 *
 * @authors A. Pedregon, J. Lusby
 * @date 03/24/19
 * @version 1.0
 */
import javafx.animation.AnimationTimer;
import javafx.application.*;
import javafx.geometry.Point2D;
import javafx.scene.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.*;
import java.util.*;

public class GraphDisplay extends Application{
    public  Pane display;
    public  FlowPane stationLog;
    public ArrayList<Agent> agents;
    public ArrayList<Node> nodes;
    public static int displayWidth = 800;
    public static int displayHeight = 600;
    public Hashtable<Point2D, Node> nodeHashtable = new Hashtable();
    private Hashtable<Node, Shape> nodeList = new Hashtable<>();
    private Hashtable<Agent, Shape> agentList = new Hashtable<>();
    private Hashtable<Node, Agent> nodesToAgents = new Hashtable<>();

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
    public void drawAgent(int x, int y, Agent agent, Node node){

        Circle circle = new Circle(14, null );
        circle.relocate((x*80)+85, (y*80)+5 );
        circle.setStroke(Color.LIMEGREEN);
        circle.setStrokeWidth(4);
        agentList.put(agent, circle);
        nodesToAgents.put(node, agent);
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
     * @param  name : unique Agent Id name
     * @param  node : node the agent was created on
     */
    public void createMessage(String name, Node node){
        Text newLog = new Text("\tAgent: " + name +
                " Created at (" + node.getX() + "," + node.getY() + ")\t");
        newLog.setStroke(Color.WHITE);
        stationLog.getChildren().add(newLog);
    }

    /**
     * Starts animation timer to update the GUI
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

    /**
     * Sets up the initial configuration of the graph
     */
    public void setUp(){
        //Loop through the edge list and add create lines between nodes
        for(int x = 0; x < Controller.edges.size(); x++){
            createLine(Controller.edges.get(x));
        }

        //Create node list from node hashtable,
        // Loop through list and add nodes to display
        nodes = new ArrayList<>(nodeHashtable.values());
        Node nodeWithAgent = null;

        for(Node node : nodes){
            if(node.getAgent() != null){
                nodeWithAgent = node;
                nodesToAgents.put(node, node.getAgent());
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
            drawAgent(nodeWithAgent.getX(), nodeWithAgent.getY(), nodeWithAgent.getAgent(), nodeWithAgent);
        }
    }

    /**
     * Updates the GUI.
     * First updates the nodes list from changes made to the nodes hash table during simulation.
     * Loops through the nodes list, checks if node has an agent and draws or moves agent on GUI.
     * If an agent is on a fire node, agent turns to gray to indicate the agent has died.
     * If node is a station sets color to green. If node on fire, set color to red.  If node near fire,
     * set color to yellow. If node has no agent and is not on fire or near the fire, set color to blue.
     */
    public void update(){
        nodes = new ArrayList<>(nodeHashtable.values());
        agents = new ArrayList<>(nodesToAgents.values());
        for(Agent agent : agents) {
            if (!agent.reported) {
                createMessage(agent.getID(), agent.getNode());
                agent.reported = true;
            }
        }
        for(Node node : nodes){
            if(node.getAgent() != null && !agentList.isEmpty()) {
                Agent agent = node.getAgent();

                if(agentList.containsKey(agent)) {
                    agentList.get(agent).relocate((node.getX() * 80) + 85, (node.getY() * 80) + 5);
                }

                else{
                    drawAgent(node.getX(), node.getY(), agent, node);
                    agentList.get(agent).relocate((node.getX() * 80) + 85, (node.getY() * 80) + 5);
                }
            }

            if (node.getState().equals("grey")){
                nodeList.get(node).setFill(Color.GREY);
            }
            else if (node.getState().equals("fire")){
                nodeList.get(node).setFill(Color.RED);
            }
            else if(node.getState().equals("blue") && !node.isStation()){
                nodeList.get(node).setFill(Color.BLUE);
            }
            else if (node.getState().equals("yellow")){
                nodeList.get(node).setFill(Color.YELLOW);
            }
            else if(node.isStation()){
                nodeList.get(node).setFill(Color.GREEN);
            }
        }
    }
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
