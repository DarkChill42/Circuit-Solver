package Elements;

import java.awt.*;

public class Node {

    private Point point;
    private int id;
    private boolean node; //Čvor???
    public Node(Point point, int id) {
        this.point = point;
        this.id = id;
    }

    public Point getPoint() {
        return point;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Node{" +
                "point=" + point +
                ", id=" + id +
                ", node=" + node +
                '}';
    }
}
