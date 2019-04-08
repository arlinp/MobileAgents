package Project4;
/**
 * Class to create the a mobile agent object
 *
 * @authors A. Pedregon, J. Lusby
 * @date 03/24/19
 * @version 1.0
 */
import javafx.geometry.Point2D;
import javafx.scene.text.Text;

import java.awt.*;
import java.util.Hashtable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Agent implements Runnable, Cloneable{
    public boolean fireFound = false;
    private Node node;
    private Boolean running = true;
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    //using ID for test
    private String ID;
    private String ChildID;
    private GraphDisplay display;
    public static Text log;
    public Hashtable<Point2D, Agent> agents = new Hashtable();


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
        Point2D point = new Point2D(newNode.getX(), newNode.getY());
        agents.put(point, this);
    }

    public void run(){
        while(!Thread.interrupted()) {

            processMessageQueue();


            System.out.println("Agent Thread: " + this);

            if(!node.getState().equals("blue")){
                fireFound = true;
                addMessage("fire");
            }
            else{
                if(!queue.contains("moveAgent")) {
                    addMessage("moveAgent");
                }
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
            String[] message = queue.remove().split(" ");

            if(message[0].equals("moveAgent")){
                node.moveAgent();
            }

            if(message[0].equals("fire")){
                    fireFound = true;
                    System.out.println("FIRE FOUND GOTTA CLONE");

                    for(Node neighbor : node.getNeighbors()){
                        if(!neighbor.getState().equals("fire")){
                            cloneOn(neighbor);
                        }
                    }
            }
        }
    }

    synchronized public void cloneOn(Node node)
    {
        Agent newAgent = new Agent(node);
        node.setAgent(newAgent);
    }

    synchronized public void cloneAgent(Node node){
        for(Node neighbor : node.getNeighbors()){
            cloneOn(neighbor);
        }

    }
    
    synchronized public void addMessage(String message) {
        queue.add(message);
    }

    //Clones the agent
//    public Agent clone() throws CloneNotSupportedException
//    {
//        char[] letter = this.ID.toCharArray();
//        char lastLetter = letter[letter.length-1];
//        char newChar = lastLetter++;
//        ChildID = this.ID += newChar;
//        Agent newAgent = (Agent) super.clone();
//        newAgent.setID("AB");
//        System.out.println("NEW NODE CREATED");
//        return newAgent;
//
//
//    }

    // Getters
    public Node getNode(){
        return this.node;
    }
    // Setters
    synchronized public Boolean setNode(Node newNode){
        this.node = newNode;
        return (this.node.equals(newNode))?true:false;
    }
    @Override
    synchronized public String toString(){
        return String.format("AgentID: "+ ID +" is on node: "+ node);
    }

    public void foundFire(){
        fireFound = true;
    }

    public String getChildID() {
        return ChildID;
    }

    public void setChildID(String ID) {
        this.ChildID = ID;
    }

    public String getID() {
        return ChildID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
