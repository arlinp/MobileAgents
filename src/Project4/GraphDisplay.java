package Project4;

import javafx.geometry.Point2D;

import java.util.*;
import java.awt.*;
import javax.swing.*;

public class GraphDisplay extends JFrame {
    private int width;
    private int height;

    private ArrayList<Node> nodes;
    private ArrayList<Edge> edges;

    public GraphDisplay() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
        width = 30;
        height = 30;
    }

    class Node {
        int x, y;
        String status;
        Point2D location;

        public Node(Point2D locale, String type) {
            location = locale;
            status = type;
            x = (int)locale.getX();
            y = (int)locale.getY();
        }
    }

    class Edge {
        Point2D i,j;
        int firstX;
        int firstY;
        int secondX;
        int secondY;

        public Edge(Point2D ii, Point2D jj) {
            i = ii;
            j = jj;
            firstX = (int)i.getX();
            firstY = (int)i.getY();
            secondX = (int)j.getX();
            secondY = (int)j.getY();
        }

    }

    public void addNode(Point2D location, String status) {
        nodes.add(new Node(location, status));
        this.repaint();
    }

    public void addEdge(Point2D i, Point2D j) {
        //adds edge between i and j
        edges.add(new Edge(i,j));
        this.repaint();
    }

    public void paint(Graphics g) {

        int nodeHeight = 20;

        g.setColor(Color.black);
        for (Edge e : edges) {
            g.drawLine(e.firstX+5, e.firstY+5, e.secondX+5, e.secondY+5);
        }

        for (Node n : nodes) {
            int nodeWidth = 20;
            switch (n.status){
                case "station":
                    g.setColor(Color.green);
                    g.fillOval(n.x, n.y,
                            nodeWidth, nodeHeight);
                    g.setColor(Color.black);
                    g.drawOval(n.x, n.y,
                            nodeWidth, nodeHeight);
                    break;
                case "onFire":
                    g.setColor(Color.red);
                    g.fillOval(n.x, n.y,
                            nodeWidth, nodeHeight);
                    g.setColor(Color.black);
                    g.drawOval(n.x, n.y,
                            nodeWidth, nodeHeight);
                    break;
                case "closeToFire":
                    g.setColor(Color.yellow);
                    g.fillOval(n.x, n.y,
                            nodeWidth, nodeHeight);
                    g.setColor(Color.black);
                    g.drawOval(n.x, n.y,
                            nodeWidth, nodeHeight);
                    break;
                default:
                    g.setColor(Color.blue);
                    g.fillOval(n.x, n.y,
                            nodeWidth, nodeHeight);
                    g.setColor(Color.black);
                    g.drawOval(n.x, n.y,
                            nodeWidth, nodeHeight);
            }
        }
    }
}