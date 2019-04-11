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

    public synchronized boolean isAClone() {
        return isAClone;
    }

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

        //known error: if another node is created on the station, it's not registered in the log again
        //node.addMessage("new Agent " + ID + " created at " + node.getX() + ", " + node.getY());
    }

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

    public synchronized void cloneOn(Node node)
    {
        Agent newAgent = new Agent(node);
        node.setAgent(newAgent);
        newAgent.setAsClone(true);
        Thread agentThread = new Thread(newAgent);
        agentThread.start();

    }

    public synchronized void cloneAgent(Node node){

        for(Node neighbor : node.getNeighbors()){
            if(!neighbor.getState().equals("fire") && neighbor.getAgent() == null) {
                cloneOn(neighbor);
            }
        }
    }

    synchronized public void addMessage(String message) {
        try {
            queue.put(message);
        }
        catch (InterruptedException e){

        }
    }

    // Getters
    public Node getNode(){
        return node;
    }
    // Setters
    synchronized void setNode(Node newNode){
        this.node = newNode;
    }

    public void foundFire(){
        fireFound = true;
    }

    public String getID() {
        return ID;
    }
}
