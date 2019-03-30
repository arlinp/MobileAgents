package Project4;
/**
 * Class to create the edges / line between nodes in the graph
 * Used by the GraphDisplay class
 *
 * @authors A. Pedregon, J. Lusby
 * @date 03/24/19
 * @version 1.0
 */
public class Edge {
    private int startX;
    private int startY;
    private int endX;
    private int endY;

    /**
     * Constructor for and Edge object
     * @param startX : x coordinate of the start of the line
     * @param startY : y coordinate of the start of the line
     * @param endX : end x coordinate
     * @param endY : end y coordinate
     */
    public Edge(int startX, int startY, int endX, int endY){
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public int getStartY(){
        return startY;
    }

    public int getStartX(){
        return startX;
    }
    public int getEndX(){
        return endX;
    }
    public int getEndY(){
        return endY;
    }


}
