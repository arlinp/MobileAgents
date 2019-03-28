package Project4;

import javafx.geometry.Point2D;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {
    Hashtable<Point2D, Node> nodes = new Hashtable();
    Hashtable<Node, Thread> nodeThreads = new Hashtable();
    List<String> instructions = new ArrayList<>();
    Node station = null;

    public void processGraph(Path path) {

        try (Stream<String> stream = Files.lines(path)) {
            instructions = stream.collect(Collectors.toList());
        } catch (IOException ex) {
            System.err.println("File: " + path + " , " + ex);
        }

        for (String instruction: instructions){
            if(!instruction.equals("")) {
                String[] graphEntity = instruction.split(" ");
                processGraphEntity(graphEntity);
            }
        }
        try {
            mapWeightsToStation(0, station);
        } catch (IllegalArgumentException e) {
            e.getMessage();
        }

        //STARTING THE THREADS
        for (Node node: nodeThreads.keySet()){
            nodeThreads.get(node).start();
        }/**/
    }

    private void processGraphEntity(String[] graphEntity) {
        Point2D location1 = createPoint(graphEntity[1],graphEntity[2]);
        switch (graphEntity[0]) {
            case "node" :
                addNodeToTables(location1);
                break;
            case "edge" :
                Point2D location2 = createPoint(graphEntity[3],graphEntity[4]);
                addEdgeToNodes(location1, location2);
                break;
            case "station" :
                setNodeAsStation(location1);
                break;
            case "fire" :
                nodes.get(location1).setOnFire(true);
                break;
            default:
                System.err.println("Line in file does not fit the format");
                break;
        }
    }

    /**
     * This is a simple djekstra-esque algorithm to map
     * the nodes' distance from the station.
     * @param weight the weight to get to that node
     * @param currentNode
     */
    private void mapWeightsToStation(int weight, Node currentNode){

        if(currentNode == null) {
            throw new IllegalArgumentException( "There is no station" );
        }

        if( weight<currentNode.getWeight() ) {
            currentNode.setWeight(weight);
            for ( Node node : currentNode.getNeighbors() ) {
                mapWeightsToStation((weight+1), node);
            }
        }
    }

    private void setNodeAsStation(Point2D location) {
        station = nodes.get(location);
        station.setStation(true);
        createAgentOnNode(station, "A");
    }

    private void createAgentOnNode(Node agentNode){
        Agent agent = new Agent(agentNode);
        agentNode.setAgent(agent);
        Thread agentThread = new Thread(agent);
        agentThread.start();
    }

    private void createAgentOnNode(Node agentNode, String name){
        Agent agent = new Agent(agentNode, name);
        agentNode.setAgent(agent);
        Thread agentThread = new Thread(agent);
        //old thread start
        agentThread.start();
    }

    private void addEdgeToNodes(Point2D location1, Point2D location2) {
        addNodeToTables(location1);
        addNodeToTables(location2);
        nodes.get(location1).addNeighbor(nodes.get(location2));
        nodes.get(location2).addNeighbor(nodes.get(location1));
    }

    private void addNodeToTables(Point2D location) {
        if (nodes.get(location) == null) {
            Node node = createNode(location);
            nodes.put(location, node);
            addThreadToTable(node);
        }
    }

    private void addThreadToTable(Node node) {
        if (nodeThreads.get(node) == null){
            Thread thread = new Thread(node);
            nodeThreads.put(node, thread);
        }
    }

    private Node createNode(Point2D location){
        return new Node(location);
    }

    public static Point2D createPoint(String sx, String sy) {
        double x = Double.parseDouble(sx);
        double y = Double.parseDouble(sy);
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
