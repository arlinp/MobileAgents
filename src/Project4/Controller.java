package Project4;
/**
 * Class to create the graph for the graph / sensor network
 *
 * @authors A. Pedregon, J. Lusby
 * @date 03/24/19
 * @version 1.0
 */
import javafx.geometry.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class Controller {
    private int fireCount = 0;
    private int stationCount = 0;
    public static ArrayList<Node> sensors = new ArrayList<>();
    public static ArrayList<Edge> edges = new ArrayList<>();
    public static Hashtable<Point2D, Node> nodes = new Hashtable();
    Hashtable<Node, Thread> nodeThreads = new Hashtable();
    Node station = null;
    GraphDisplay gd;

    /**
     * Reads in the graph configuration file, parses the information of each line to either
     * create a new node, create an edge between nodes, set the fire location, or set the
     * station location
     * @throws Exception : if file not found
     */
    public void readGraph() throws Exception{
        File file = new File("src/Project4/MediumGraph.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;

        //Loop through each line of the input file
        while((st = br.readLine()) != null){

            //if line indicates a node, creates the node
            if(st.charAt(0) == 'n'){
                int x = st.charAt(5) - 48;
                int y = st.charAt(7) - 48;
                sensors.add(new Node(x, y, false, false));
                addNodeToTables(createPoint(st.charAt(5) - 48, st.charAt(7) - 48));
            }

            //if the line indicates an edge, create an edge
            if(st.charAt(0) == 'e'){
                Edge edge = new Edge((int)st.charAt(5) - 48,(int)st.charAt(7) - 48,
                        (int)st.charAt(9) - 48, (int)st.charAt(11) - 48);
                edges.add(edge);
                Point2D location1 = createPoint(st.charAt(5) - 48, st.charAt(7) - 48);
                Point2D location2 = createPoint(st.charAt(9) - 48, st.charAt(11) - 48);
                addEdgeToNodes(location1, location2);
            }

            //if the line indicates a node on fire, set node to be on fire
            if(st.charAt(0) == 'f' && fireCount < 1){
                fireCount++;
                int x = st.charAt(5) - 48;
                int y = st.charAt(7) - 48;
                for(int i = 0; i < sensors.size(); i++){
                    if(sensors.get(i).getX() == x && sensors.get(i).getY() == y){
                        sensors.get(i).setFire();
                        nodes.get(createPoint(st.charAt(5) - 48, st.charAt(7) - 48)).setFire();
                    }
                }
            }

            //if the line indicates the base station location, set the location
            if(st.charAt(0) == 's' && stationCount < 1){
                stationCount++;
                int x = st.charAt(8) - 48;
                int y = st.charAt(10) - 48;
                for(int i = 0; i < sensors.size(); i++){
                    if(sensors.get(i).getX() == x && sensors.get(i).getY() == y){
                        sensors.get(i).setStation();
                        sensors.get(i).hasAgent = true;
                        setNodeAsStation(createPoint(st.charAt(8) - 48, st.charAt(10) - 48));
                    }
                }
            }
        }

        //map node weights and start node threads
        mapWeightsToStation(0, station);
        for (Node node: nodeThreads.keySet()){
            nodeThreads.get(node).start();
        }
    }

    /**
     *
     * @param weight
     * @param currentNode
     */
    private void mapWeightsToStation(int weight, Node currentNode){
        if(currentNode == null){
            throw new IllegalArgumentException("There is no station");
        }
        if(weight<currentNode.getWeight()) {
            currentNode.setWeight(weight);
            for (Node node : currentNode.getNeighbors()) {
                mapWeightsToStation((weight+1), node);
            }
        }
    }

    /**
     *
     * @param location
     */
    private void setNodeAsStation(Point2D location) {
        station = nodes.get(location);
        station.setStation();
        createAgentOnNode(station);
    }

    /**
     *
     * @param agentNode
     */
    private void createAgentOnNode(Node agentNode){
        Agent agent = new Agent(agentNode);
        agentNode.setAgent(agent);
        Thread agentThread = new Thread(agent);
        agentThread.start();
    }

    /**
     *
     * @param location1
     * @param location2
     */
    private void addEdgeToNodes(Point2D location1, Point2D location2) {
        addNodeToTables(location1);
        addNodeToTables(location2);
        nodes.get(location1).addNeighbor(nodes.get(location2));
        nodes.get(location2).addNeighbor(nodes.get(location1));
    }

    /**
     *
     * @param location
     */
    private void addNodeToTables(Point2D location) {
        if (nodes.get(location) == null) {
            Node node = createNode(location);
            nodes.put(location, node);
            addThreadToTable(node);
        }
    }

    /**
     *
     * @param node
     */
    private void addThreadToTable(Node node) {
        if (nodeThreads.get(node) == null){
            Thread thread = new Thread(node);
            nodeThreads.put(node, thread);
        }
    }

    private Node createNode(Point2D location){

        return new Node((int)location.getX(), (int)location.getY(), false, false);
    }

    /**
     * Creates a point for the node
     * @param xValue : x coordinate
     * @param yValue : y coordinate
     * @return new 2D point
     */
    public static Point2D createPoint(int xValue, int yValue) {
        double x = xValue;
        double y = yValue;
        return new Point2D(x,y);
    }

    // Getters
    public Hashtable getNodes(){
        return nodes;
    }

    public Hashtable getThreads() {
        return nodeThreads;
    }
}