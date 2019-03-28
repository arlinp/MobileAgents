package Project4;

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

    public Agent(){
        //base case just makes empty agent
        this.node = null;
        this.ID = "A";
        // Todo: addMessage("report");
    }
    public Agent(Node newNode){
        this.node = newNode;
        this.ID = "B";
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
                node.addMessage("moveAgent " + this.ID);
                node.addMessage("The agent has received the move");

            } else {

                // todo: clone the agent into nearby nodes
                System.out.println("AGENT HAS NOW STOPPED ------");

            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Interupted: " + e);
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
