package Project4;
/**
 * Class to create the a mobile agent object
 *
 * @authors A. Pedregon, J. Lusby
 * @date 03/24/19
 * @version 1.0
 */
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Agent implements Runnable, Cloneable{
    private boolean fireFound = false;
    private Node node;
    private Boolean running = true;
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    //using ID for test
    private String ID;
    private String ChildID;

    //Default Constructor not used
    public Agent(){
        //base case just makes empty agent
        this.node = null;
        this.ID = "A";
        // Todo: addMessage("report");
    }

    public Agent(Node newNode){
        this.node = newNode;
        //named after node's X,Y position
        String number = Integer.toString(newNode.getX());
        number += Integer.toString(newNode.getY());

        this.ID = number;
        System.out.println("Agent " + ID + "created");
    }
    public Agent(Node newNode, String name){
        this.node = newNode;
        this.ID = name;
    }

    public void run(){
        while (running) {
            System.out.println("Agent Thread: " + this);
            if (!fireFound) {
                node.moveAgent();
                // todo: report new location to base once

            } else {

                // todo: clone the agent into nearby nodes
                System.out.println("TEST FOR POINT ------");

            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + e);
            }
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