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
import java.util.Timer;

public class Node implements Runnable {
    private Agent agent = null;
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private List<Node> neighbors = new ArrayList<>();
    private Random randomNeighbor = new Random();
    private int xValue;
    private int yValue;
    private boolean isStation;
    private String state = "blue";
    private int weight = Integer.MAX_VALUE;
    private Controller controller;
    private boolean turnedYellow = false;
    private boolean alertedNeighbors = false;
    private Timer timer;
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
    }
    public synchronized boolean isTurnedYellow() {
        return turnedYellow;
    }

    public synchronized void setTurnedYellow(boolean turnedYellow) {
        this.turnedYellow = turnedYellow;
    }

    public synchronized boolean isAlertedNeighbors() {
        return alertedNeighbors;
    }

    public synchronized void setAlertedNeighbors(boolean alertedNeighbors) {
        this.alertedNeighbors = alertedNeighbors;
    }

    @Override
    public void run() {
        while(true) {
            //System.out.println("Thread is running: " + this);
            try {
                Thread.sleep(1000);

            } catch (InterruptedException iE) {
                System.out.println("ZZzzzzzz interuption: " + iE);
            }
            try {

                if (getState().equals("fire") && !isAlertedNeighbors()) {
                    for (Node neighbor : neighbors) {
                        if (neighbor.getState().equals("blue")) {
                            neighbor.getQueue().put("near");
                            neighbor.setState("yellow");
                        }
                    }
                    lastMessageAndDelete();

                    setAlertedNeighbors(true);
//                    System.out.println("agent = " + agent);
                } else if (getState().equals("yellow") && !isTurnedYellow()) {
                    //queue.put("near");
                    processMessageQueue();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void processMessageQueue() {
        try {
            String message = getQueue().take();

            if (message.contains("near")) {
                setTurnedYellow(true);
                setState("yellow");
                setNodeOnFire();
            }
            if(message.equals("blue")){
                System.out.println("BLUE");
            }
            //LOG
//            else if (this.isStation && (message.contains("new") ||
//                    message.contains("cloned"))) {
//                GraphDisplay.createMessage(message);
//            }
//            else if (!isStation && (message.contains("new") || message.contains("cloned"))) {
//                passCreationMessage(message);
//            }
        }
        catch (InterruptedException e) {
            System.out.println("empty");
        }
    }

    private synchronized void setNodeOnFire(){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                setState("fire");
                setFire();
                lastMessageAndDelete();
            }
        }, new Date(System.currentTimeMillis() + 2000));
    }

    private void lastMessageAndDelete() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                setState("grey");
                agent = null;
            }
        }, new Date(System.currentTimeMillis() + 2000));
    }


    private synchronized void passCreationMessage(String message) {
        for(Node node : neighbors){
            if(node.weight < weight){
                node.addMessage(message);
                break;
            }
        }
    }

    // Agents
    public synchronized void moveAgent() {
        //commence random walk
        Node neighbor = neighbors.get(randomNeighbor.nextInt(neighbors.size()));
        if (neighbor.agent == null && !getState().equals("grey")) {
            neighbor.setAgent(agent);
            agent.setNode(neighbor);
            this.agent = null;
        }

    }

    // Nodes
    public synchronized void addNeighbor(Node neighbor) {
        neighbors.add(neighbor);
    }

    // Setters
    public void setWeight(int newWeight) {
        this.weight = newWeight;
    }

    // Queue Messages
    public synchronized void addMessage(String message) {
        try{
            getQueue().put(message);
        }
        catch(InterruptedException e){
        }
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
        setState("fire");
    }

    /**
     * Set this nodes station to true
     */
    public synchronized void setStation() {
        isStation = true;
    }

    /**
     * Get sensor station status
     */
    public synchronized boolean isStation() {
        return isStation;
    }

    /**
     * Get x coordinate of this tile
     */
    public synchronized int getX() {
        return xValue;
    }

    /**
     * Get y coordinate of this tile
     */
    public synchronized int getY() {
        return yValue;
    }

    /**
     * get weight of the node (distance)
     */
    public synchronized int getWeight() {
        return weight;
    }

    /**
     * get node's neighbors
     */
    synchronized public List<Node> getNeighbors() {
        return neighbors;
    }

    synchronized void setAgent(Agent newAgent) {
        agent = newAgent;
    }

    public synchronized Agent getAgent() {
        return agent;
    }
    
    public synchronized void setController(Controller controller) {
        this.controller = controller;
    }

    public synchronized BlockingQueue<String> getQueue() {
        return queue;
    }

}
