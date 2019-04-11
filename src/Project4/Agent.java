package Project4;
/**
 * Class to create the a mobile agent object
 *
 * @authors A. Pedregon, J. Lusby
 * @date 03/24/19
 * @version 1.0
 */
import javafx.geometry.Point2D;
import java.util.Hashtable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Agent implements Runnable, Cloneable{
    private Node node;
    private String ID;
    private boolean isAClone = false;
    private boolean cloned = false;
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    public Boolean reported;
    public boolean fireFound = false;
    public Hashtable<Point2D, Agent> agents = new Hashtable();

    public synchronized boolean isCloned() {
        return cloned;
    }

    public synchronized void setCloned(boolean cloned) {
        this.cloned = cloned;
    }

    /**
     * Checks if this agent is a clone
     * @return true if agent is a clone, false otherwise
     */
    public synchronized boolean isAClone() {
        return isAClone;
    }

    /**
     * Sets this ages as a clone
     * @param AClone
     */
    public synchronized void setAsClone(boolean AClone) {
        isAClone = AClone;
    }

    /**
     * Makes a new agent,
     * assigns the node to it,
     * names it after position of node
     * -This makes the naming of each node unique
     * @param newNode
     */
    public Agent(Node newNode){
        this.node = newNode;
        //named after node's X,Y position
        String number = Integer.toString(newNode.getX());
        number += Integer.toString(newNode.getY());
        this.ID = number;
        this.reported = false;
        Point2D point = new Point2D(newNode.getX(), newNode.getY());
        agents.put(point, this);
    }

    /**
     * process agent's action to clone or move along the graph
     */
    public void run(){
        while(true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException iE) {
                System.out.println("ZZzzzzzz interuption: " + iE);
            }
            if ( (node.getState().equals("fire") || node.getState().equals("yellow") ) && !cloned) {
                cloneAgent(node);
                cloned = true;

            } else if (node.getState().equals("blue") && !isAClone()) {
                node.moveAgent();
            }
        }
    }

    /**
     * Clones a new agent on this node and starts it's thread
     * @param node : location of this node
     */
    public synchronized void cloneOn(Node node)
    {
        Agent newAgent = new Agent(node);
        node.setAgent(newAgent);
        newAgent.setAsClone(true);
        Thread agentThread = new Thread(newAgent);
        agentThread.start();
    }

    /**
     * Checks which neighbor to clone a new agent
     * by looping through this nodes neighbors
     * @param node : current node to check
     */
    public synchronized void cloneAgent(Node node){
        for(Node neighbor : node.getNeighbors()){
            if(!neighbor.getState().equals("fire") && !neighbor.getState().equals("grey")
                    && neighbor.getAgent() == null) {
                cloneOn(neighbor);
            }
        }
    }

    /**
     * puts message into the blocking queue
     * @param message
     */
    synchronized public void addMessage(String message) {
        try {
            queue.put(message);
        }
        catch (InterruptedException e){

        }
    }

    /**
     * Get this node
     */
    public Node getNode(){
        return node;
    }

    /**
     * Get this agent ID
     */
    public String getID() {
        return ID;
    }

    //setters
    synchronized void setNode(Node newNode){
        this.node = newNode;
    }

    public void foundFire(){
        fireFound = true;
    }

}
