package Project4;
/**
 * Class to create and manipulate a sensor node and its state for the network.
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
     * Constructor for a sensor node, Sets the nodes coordinates.
     * Newly created nodes are not on fire and not a station by default
     * @param x : x coordinate of the node in the network
     * @param y : y coordinate of the node in the network
     */
    public Node(int x, int y) {
        this.xValue = x;
        this.yValue = y;
        this.isStation = false;
        this.isFire = false;
    }

    /**
     * Runs the node threads to process the messages and change states
     */
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

    /**
     * Processes messages in the message queue.
     * Sets the node states according to message in queue 
     */
    synchronized private void processMessageQueue() {
        if (!queue.isEmpty()) {
            String message = queue.remove();

            if ( message.equals("near") ){
                isYellow = true;
                state = "yellow";
            }
            else if ( message.equals("fire") ){
                this.agent = null;
                state = "fire";
            }

        }
    }

    /**
     * Moves an agent from one node to another.  Agents randomly walk along the network.
     */
    synchronized public void moveAgent() {
        //commence random walk
        Node neighbor = neighbors.get(randomNeighbor.nextInt(neighbors.size()));
        if (neighbor.agent == null) {
            if(neighbor.getState().equals("yellow")){ agent.addMessage("fire"); }
            else if (neighbor.setAgent(this.agent) && this.agent.setNode(neighbor)) {
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

    /**
     * Adds this node to the neighbors list
     * @param neighbor : node to add
     */
    public void addNeighbor(Node neighbor) {
        neighbors.add(neighbor);
    }

    /**
     * Sets the weight / of this node
     * @param newWeight : integer value of nodes distance from the station
     */
    public void setWeight(int newWeight) {
        this.weight = newWeight;
    }

    protected void setNeighbors(List<Node> newNeighbors) {
        this.neighbors = newNeighbors;
    }

    /**
     * Adds message to the message queue 
     * @param message : message to be added to the queue 
     */
    synchronized public void addMessage(String message) {
        queue.add(message);
    }

    /**
     * Prints out node states to standard out for evaluation / debugging
     * @return string : (state of the node)
     */
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

    /**
     * Sets the agent 
     * @param newAgent : agent to set
     * @return boolean
     */
    synchronized public Boolean setAgent(Agent newAgent) {
        this.agent = newAgent;
        return true;
        //return (this.agent.equals(newAgent)) ? true : false;
    }

    /**
     * Gets the agent on this node
     * @return agent : agent located on node
     */
    synchronized public Agent getAgent() {
        return this.agent;
    }
}
