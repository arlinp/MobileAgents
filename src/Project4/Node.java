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

public class Node implements Runnable{
    private Agent agent = null;
    private Boolean running = true;
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private List<Node> neighbors = new ArrayList<>();
    private Random randomNeighbor = new Random();
    private int xValue;
    private int yValue;
    private boolean isStation;
    private boolean isFire;
    private int weight = Integer.MAX_VALUE;

    /**
     * Constructor for a sensor node
     * @param x : x coordinate of the node in the network
     * @param y : y coordinate of the node in the network
     * @param station : indicates if the node is the station
     * @param fire : indicates if the node is on fire
     */
    public Node(int x, int y, boolean station, boolean fire){
        this.xValue = x;
        this.yValue = y;
        this.isStation = station;
        this.isFire = fire;
    }

    /**
     * Set this nodes fire to true
     */
    public void setFire(){
        isFire = true;
    }

    /**
     * Set this nodes station to true
     */
    public void setStation(){
        isStation = true;
    }

    /**
     * Get sensor station status
     */
    public Boolean isStation(){
        return isStation;
    }

    /**
     * Get sensor fire status
     */
    public Boolean isFire(){
        return isFire;
    }
    /**
     * Get x coordinate of this tile
     */
    public int getX(){
        return xValue;
    }

    /**
     * Get y coordinate of this tile
     */
    public int getY(){
        return yValue;
    }

    public void run(){
        while(running) {
            System.out.println("Thread is running: " + this);
            processMessageQueue();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException iE) {
                System.out.println("ZZzzzzzz interuption: " + iE);
            }
        }

    }

    synchronized private void processMessageQueue() {
        if (!queue.isEmpty()) {
            String[] message = queue.remove().split(" ");
        //    if (message[0].equals("moveAgent")) {
        //        moveAgent();
        //    }
        }
    }

    // Agents
    synchronized public void moveAgent(){
        Node neighbor = neighbors.get(randomNeighbor.nextInt(neighbors.size()));
        if(neighbor.agent == null){
            if(neighbor.isFire){
                System.out.println("FIRE FOUND");
                agent.foundFire();

                // todo: send the message that Fire was found
            }
            else if(neighbor.setAgent(this.agent)&&this.agent.setNode(neighbor)){
                this.agent = null;
            }
        }
    }


    // Nodes
    public void addNeighbor(Node neighbor){
        neighbors.add(neighbor);
    }

    public int getWeight(){
        return weight;
    }

    public List<Node> getNeighbors() {
        return neighbors;
    }
    // Setters

    public void setWeight(int newWeight){
        this.weight = newWeight;
    }

    protected void setNeighbors(List<Node> newNeighbors) {
        this.neighbors = newNeighbors;
    }
    // Queue Messages
    synchronized public void addMessage(String message){
        queue.add(message);
    }
    synchronized public Boolean setAgent(Agent newAgent){
        this.agent = newAgent;
        return (this.agent.equals(newAgent))?true:false;
    }
    // Override ToString
    @Override
    synchronized public String toString(){
        return String.format("Node @ ("+ xValue + ", " + yValue + ") weight: " +
                weight + ", isStation: " + isStation + ", " + "isOnFire" + ": " +
                isFire + ", and hasAgent: " +((agent!=null)?"true":"false"));
    }
}