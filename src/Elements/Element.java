package Elements;


import java.awt.*;

public class Element {

    private Node start,end;
    private double value;
    private ElementType type;

    public Element(Node start, Node end, double value, ElementType type) {
        this.start = start;
        this.end = end;
        this.value = value;
        this.type = type;
    }

    public Node getStart() {
        return start;
    }

    public Node getEnd() {
        return end;
    }

    public double getValue() {
        return value;
    }

    public ElementType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Element{" +
                "start=" + start +
                ", end=" + end +
                ", value=" + value +
                ", type=" + type +
                '}';
    }

    public void drawElement(Graphics g) {

    }
}
