package Project4;
/**
 * Class to create and manipulate a sensor node for the network
 *
 * @authors A. Pedregon, J. Lusby
 * @date 03/24/19
 * @version 1.0
 */
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Node implements Runnable {


    private Agent agent = null;
    private Boolean running = true;
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private List<Node> neighbors = new ArrayList<>();
    private Random randomNeighbor = new Random();
    private int xValue;
    private int yValue;
    private boolean isStation;
    private boolean isYellow;
    private boolean isFire;
    private String state = "blue";
    private int weight = Integer.MAX_VALUE;

    /**
     * Constructor for a sensor node
     *
     * @param x : x coordinate of the node in the network
     * @param y : y coordinate of the node in the network
     */
    public Node(int x, int y) {
        this.xValue = x;
        this.yValue = y;
        this.isStation = false;
        this.isFire = false;
    }



    public void run() {
        while (!Thread.interrupted()) {

            processMessageQueue();

            System.out.println("Thread is running: " + this);

            if(state.equals("fire")){
                isFire = true;
                for (Node neighbor : neighbors) {
                    if(neighbor.state == "blue") {
                        neighbor.addMessage("near");
                    }
                }
            }

            if(state.equals("yellow")){
                isYellow = true;
                addMessage("fire");
            }

            try {
                Thread.sleep(1000);

            } catch (InterruptedException iE) {
                System.out.println("ZZzzzzzz interuption: " + iE);
            }

        }

    }

    synchronized private void processMessageQueue() {
        if (!queue.isEmpty()) {

            String message = queue.remove();

            if ( message.equals("near") ){
                isYellow = true;
                state = "yellow";
            }

            else if ( message.equals("fire") ){
                state = "fire";
                agent = null;
            }

        }
    }


    // Agents
    synchronized public void moveAgent() {

            //commence random walk
        Node neighbor = neighbors.get(randomNeighbor.nextInt(neighbors.size()));

        if (neighbor.agent == null) {
            if (neighbor.setAgent(this.agent) && this.agent.setNode(neighbor)) {
                this.agent = null;
            }
        }

    }


    private void waitAndLightOnFire() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException iE) {
            System.out.println("ZZzzzzzz interuption: " + iE);
        }


    }

//    synchronized public void cloneAgent(Node node) {
//        for (Node neighbor : node.neighbors) {
//            cloneOn(neighbor);
//        }
//
//    }

//    synchronized public void cloneOn(Node node) {
//        Agent newAgent = new Agent(node);
//        node.setAgent(newAgent);
//    }


    // Nodes
    public void addNeighbor(Node neighbor) {
        neighbors.add(neighbor);
    }


    // Setters

    public void setWeight(int newWeight) {
        this.weight = newWeight;
    }

    protected void setNeighbors(List<Node> newNeighbors) {
        this.neighbors = newNeighbors;
    }

    // Queue Messages
    synchronized public void addMessage(String message) {
        queue.add(message);
    }

    // Override ToString
    @Override
    synchronized public String toString() {
        return String.format("Node @ (" + xValue + ", " + yValue + ") weight: " +
                weight + ", isStation: " + isStation + ", " + "isOnFire" + ": " +
                isFire + " isYellow" + ": " +
                isYellow + ", and hasAgent: " + ((agent != null) ? "true" : "false"));
    }


    /**
     * Get node's state
     */
    synchronized public String getState() {
        return state;
    }

    /**
     * Set this node's state to
     * @param state = the state node is in
     *              (blue, yellow, fire)
     */
    synchronized public void setState(String state) {
        this.state = state;
    }

    /**
     * Set this nodes fire to true
     */
    synchronized public void setFire() {
        isFire = true;
        state = "fire";
    }


    /**
     * Set this nodes station to true
     */
    public void setStation() {
        isStation = true;
    }

    /**
     * Get sensor station status
     */
    public Boolean isStation() {
        return isStation;
    }

    /**
     * Get x coordinate of this tile
     */
    public int getX() {
        return xValue;
    }

    /**
     * Get y coordinate of this tile
     */
    public int getY() {
        return yValue;
    }

    /**
     * get weight of the node (distance)
     */
    public int getWeight() {
        return weight;
    }

    /**
     * get node's neighbors
     */
    synchronized public List<Node> getNeighbors() {
        return neighbors;
    }

    synchronized public Boolean setAgent(Agent newAgent) {
        this.agent = newAgent;
        return true;
        //return (this.agent.equals(newAgent)) ? true : false;
    }

    synchronized public Agent getAgent() {
        return this.agent;
    }



}
