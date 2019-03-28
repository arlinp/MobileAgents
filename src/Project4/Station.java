package Project4;

import javafx.geometry.Point2D;

import java.util.Hashtable;
import java.util.List;

public class Station extends Node {
    Hashtable<Agent,Node> agents = new Hashtable<>();
    public Station (Point2D location, List<Node> newNeighbors){
        super(location);
        this.setStation(true);
        this.setNeighbors(newNeighbors);
    }
}
