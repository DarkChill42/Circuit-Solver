package GUI;

import Elements.*;
import Main.MainFrame;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
public class MainPane extends JPanel {

    public static final int NUMBER_OF_NODES = 120;
    public static final int NODE_DIAMETER = 10;
    private List<Element> elements;
    private final Node[] nodes;

    private Node firstNode, secondNode = null;

    public MainPane() {
        this.elements = new ArrayList<>();
        this.nodes = new Node[NUMBER_OF_NODES];
        setBackground(new Color(0x313030));
        int nodeStartX = 125;
        int nodeStartY = 170;
        for(int i = 0; i < NUMBER_OF_NODES; i++) {
            if(i % 15 == 0 && i != 0) {
                nodeStartY+=60;
                nodeStartX=125;
            }
            nodes[i] = new Node(new Point(nodeStartX, nodeStartY), i);
            nodeStartX+=52;

        }
        mouseListeners();
    }





    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
        for(Element element : elements) {
            element.drawElement(g);
        }
    }


    private void mouseListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if(firstNode == null) {
                    firstNode = getClosestNode(e.getX(),e.getY());
                } else {
                    secondNode = getClosestNode(e.getX(),e.getY());
                    if(secondNode != null) {
                        String input = "0";
                        if(MainFrame.selectedElement != ElementType.WIRE) {
                            input = JOptionPane.showInputDialog(MainPane.this, "Enter elements value:");
                        }

                        try {
                            if(MainFrame.selectedElement == ElementType.WIRE) {
                                elements.add(new Wire(firstNode,secondNode));
                            } else if(MainFrame.selectedElement == ElementType.RESISTOR) {
                                elements.add(new Resistor(firstNode,secondNode,Double.parseDouble(input)));
                            } else if(MainFrame.selectedElement == ElementType.VOLTAGE_SOURCE) {
                                elements.add(new VoltageSource(firstNode,secondNode,Double.parseDouble(input)));
                            } else if(MainFrame.selectedElement == ElementType.CURRENT_SOURCE) {
                                elements.add(new CurrentSource(firstNode,secondNode,Double.parseDouble(input)));
                            }
                        } catch (Exception exc) {
                            System.out.println("Value is a number...");
                        }
                        repaint();
                        firstNode = null;
                    }
                }
            }
        });
    }

    private Node getClosestNode(int x, int y) {
        Node closest = nodes[0];
        for (int i = 0; i < NUMBER_OF_NODES; i++) {
            if(getDistance(x,y,nodes[i].getPoint().x, nodes[i].getPoint().y) <=
                getDistance(x,y,closest.getPoint().x, closest.getPoint().y)) {
                closest = nodes[i];
            }
        }
        return (getDistance(x,y,closest.getPoint().x,closest.getPoint().y) <= 20 ? closest : null);
    }

    private double getDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x1-x2,2) + Math.pow(y1-y2,2));
    }
    private void drawGrid(Graphics g) {
        //Draws rectangle border
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(new Color(0xFCDCB9));
        g2d.setStroke(new BasicStroke(5));
        int rectWidth = 800;
        int rectHeight = 500;
        int rectX = (this.getWidth() - rectWidth) / 2;
        int rectY = (this.getHeight() - rectHeight) / 2;
        g.drawRect(rectX, rectY, rectWidth, rectHeight);
        //Draws dots(nodes) inside the rectangle
        g.setColor(new Color(0xBCBEF3));
        for(int i = 0; i < NUMBER_OF_NODES; i++) {
            g.fillOval(nodes[i].getPoint().x,nodes[i].getPoint().y, NODE_DIAMETER, NODE_DIAMETER);
        }
    }
}
