package Elements;

import java.awt.*;

public class Node {

    private Point point;
    private int id;
    private boolean junction;

    private double potential;

    private boolean grounded;
    public Node(Point point) {
        this.id = -1;
        this.potential = Double.MAX_VALUE;
        this.point = point;
        this.junction = false;
        this.grounded = false;
    }

    public Point getPoint() {
        return point;
    }

    public int getId() {
        return id;
    }

    public boolean isJunction() {
        return junction;
    }

    public void setJunction(boolean junction) {
        this.junction = junction;
    }

    public double getPotential() {
        return potential;
    }

    public void setPotential(double potential) {
        this.potential = potential;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isGrounded() {
        return grounded;
    }

    public void setGrounded(boolean grounded) {
        this.potential = 0.0;
        this.grounded = grounded;
    }

    @Override
    public String toString() {
        return "Node{" +
                "point=" + point +
                ", id=" + id +
                ", node=" + junction +
                '}';
    }
}
