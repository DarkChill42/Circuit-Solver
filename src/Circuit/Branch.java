package Circuit;

import Elements.Element;
import Elements.ElementType;
import Elements.Node;

import java.util.ArrayList;
import java.util.List;

public class Branch {
    private Node start,end;
    private List<Element> elements;

    private int resistorCount,wireCount,voltageCount,currentCount;


    public Branch() {
        this.elements = new ArrayList<>();
        resistorCount = wireCount = voltageCount = currentCount = 0;
    }

    public Node getStart() {
        return start;
    }

    public void setStart(Node start) {
        this.start = start;
    }

    public Node getEnd() {
        return end;
    }

    public void setEnd(Node end) {
        this.end = end;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    public int getResistorCount() {
        return resistorCount;
    }


    public int getVoltageCount() {
        return voltageCount;
    }


    public int getCurrentCount() {
        return currentCount;
    }


    public void increaseCount(ElementType type) {
        if(type == ElementType.WIRE) {
            wireCount++;
        } else if(type == ElementType.RESISTOR) {
            resistorCount++;
        } else if(type == ElementType.VOLTAGE_SOURCE) {
            voltageCount++;
        } else {
            currentCount++;
        }
    }
}
