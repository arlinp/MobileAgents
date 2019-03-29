package Project4;

import javafx.geometry.Point2D;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * This class creates a node and subsequently stores/sends
 * information back to the main station
 */
public class Node implements Runnable{

    private String name;
    private String state;
    private Point2D location;
    private Agent agent = null;
    private Boolean isStation;
    private Boolean isOnFire;
    private Boolean running = true;
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private List<Node> neighbors = new ArrayList<>();
    private Random randomNeighbor = new Random();
    int weight = Integer.MAX_VALUE;

    public Node(Point2D locale) {
        this.location = locale;
        this.isStation = false;
        this.isOnFire = false;
    }
    public Node(Point2D locale, Boolean base, Boolean fire){
        this.location = locale;
        this.isStation = base;
        this.isOnFire = fire;
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
//            if (message[0].equals("moveAgent")) {
//                moveAgent();
//            }
        }
    }

    // Perform a random walk to find fire
    synchronized public void moveAgent(){
        Node neighbor = neighbors.get(randomNeighbor.nextInt(neighbors.size()));
        if(neighbor.agent == null){
            if(neighbor.isOnFire){
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
    // Getters
    public Boolean getIsStation() {
        return isStation;
    }
    public Boolean getIsOnFire() {
        return isOnFire;
    }
    public int getWeight(){
        return weight;
    }
    public Point2D getLocation() {
        return location;
    }
    public List<Node> getNeighbors() {
        return neighbors;
    }
    // Setters
    public void setStation(Boolean isBase){
        this.isStation = isBase;
    }
    public void setOnFire(boolean isBurning){
        this.isOnFire = isBurning;
    }
    public void setWeight(int newWeight){
        this.weight = newWeight;
    }
    public void setLocation(Point2D newLocation) {
        this.location = newLocation;
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
        return String.format("Node @ ("+location.getX()+", "+
                location.getY()+") weight: "+weight+", isStation: "+isStation+", " +
                "isOnFire" +
                ": "+
                isOnFire+ ", and hasAgent: "+((agent!=null)?"true":"false"));
    }

}
