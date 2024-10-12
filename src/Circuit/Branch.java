package Circuit;

import Elements.Element;
import Elements.ElementType;
import Elements.Node;

import java.util.ArrayList;
import java.util.List;

public class Branch {
    private Node start,end;
    private List<Element> elements;

    private double resistance;
    private int resistorCount,wireCount,voltageCount,currentCount;

    private double current;


    public Branch() {
        this.elements = new ArrayList<>();
        resistorCount = wireCount = voltageCount = currentCount = 0;
        this.resistance = 0.0;
        this.current = 0.0;
    }

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public double getResistance() {
        return resistance;
    }

    public void setResistance(double resistance) {
        this.resistance = resistance;
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
