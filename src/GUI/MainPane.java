package GUI;

import Circuit.Branch;
import Circuit.Graph;
import Circuit.Solver;
import Elements.*;
import Main.MainFrame;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainPane extends JPanel {

    public static final int NUMBER_OF_NODES = 120;
    public static final int NODE_DIAMETER = 10;
    private List<Element> elements;

    private List<Element> drawnElements;
    private final Node[] nodes;

    Point test1 = new Point(200,200);
    Point test2 = new Point(400,200);

    public Graph circuitGraph;

    public static int nodeCount = 1;

    private Node firstNode, secondNode = null;

    private JLabel voltage;
    private JLabel nodeID;

    Popup popup;

    public MainPane() {
        this.voltage = new JLabel();
        this.nodeID = new JLabel();
        this.elements = new ArrayList<>();
        this.nodes = new Node[NUMBER_OF_NODES];
        this.drawnElements = new ArrayList<>();
        circuitGraph = new Graph(elements);
        setBackground(new Color(0x313030));
        int nodeStartX = 125;
        int nodeStartY = 170;
        for(int i = 0; i < NUMBER_OF_NODES; i++) {
            if(i % 15 == 0 && i != 0) {
                nodeStartY+=60;
                nodeStartX=125;
            }
            nodes[i] = new Node(new Point(nodeStartX, nodeStartY));
            nodeStartX+=52;

        }
        setFocusable(true);
        requestFocusInWindow();
        keyListeners();
        mouseListeners();
        mouseMove();
        showPopup();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawGrid(g);
        for(Element element : elements) {
            element.drawElement(g);
        }
    }

    private void keyListeners() {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if(e.getKeyCode() == KeyEvent.VK_SPACE) {
                    for(Map.Entry<Node, List<Node>> entry : circuitGraph.getAdjacencyList().entrySet()) {
                        if(entry.getKey().isJunction()) {
                            circuitGraph.numberOfEquations++;
                            circuitGraph.getJunctions().add(entry.getKey());
                            circuitGraph.findBranch(entry.getKey());
                        }
                    }
                    //circuitGraph.printBranches();

                    Solver.test(circuitGraph.getBranches(), circuitGraph);
                    repaint();

                } else if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
                   for(Branch branch : circuitGraph.getBranches()) {
                       System.out.println("Branch starting at: " + branch.getStart().getId() + " and ending at: " + branch.getEnd().getId() + " has current: " + branch.getCurrent());
                   }
                }
                //circuitGraph.printBranches();
            }
        });
    }

    private void showPopup() {

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS)); // Vertical layout
        messagePanel.setPreferredSize(new Dimension(120, 60)); // Set preferred size
        messagePanel.setBackground(new Color(0xA2A2A2));

        messagePanel.add(nodeID);
        messagePanel.add(voltage);

        SwingUtilities.invokeLater(() -> {
            // Find the parent window or component
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                // Get window size to position the popup at bottom-right
                int popupX = window.getWidth();  // Adjust based on width of your popup
                int popupY = window.getHeight() - 100; // Adjust based on height of your popup

                PopupFactory factory = PopupFactory.getSharedInstance();
                popup = factory.getPopup(window, messagePanel, popupX, popupY);
                popup.show();
            }
        });
    }



    private void mouseMove() {
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                for(Node n : nodes) {
                    if(n.getId() == -1) continue;
                    if(isInsideCircle(n, e.getPoint())) {
                        voltage.setText("Voltage: " + (n.getPotential() == Double.MAX_VALUE ? 0.0 : Util.numberFormatter(n.getPotential(), 'V')));
                        nodeID.setText("Node ID: " + n.getId());
                        n.setHovering(true);
                        repaint();
                    } else {
                        if(n.isHovering()) {
                            n.setHovering(false);
                            repaint();
                        }
                    }
                }
                for(Branch branch : circuitGraph.getBranches()) {
                    for(Element element : branch.getElements()) {
                        if(Util.isHoveringLine(element.getStart().getPoint(), element.getEnd().getPoint(), e.getPoint(), 10)) {
                            element.setHighlight(true);
                            repaint();
                        } else {
                            if(element.isHighlight()) {
                                element.setHighlight(false);
                                repaint();
                            }
                        }
                    }
                }
            }
        });
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
                            if(firstNode.getId() == -1 && MainFrame.selectedElement != ElementType.WIRE) firstNode.setId(nodeCount++);
                            if(secondNode.getId() == -1 && MainFrame.selectedElement != ElementType.WIRE) secondNode.setId(nodeCount++);
                            if(MainFrame.selectedElement == ElementType.WIRE) {
                                if(firstNode.getId() != -1) {
                                    secondNode.setId(firstNode.getId());
                                } else if(secondNode.getId() != -1) {
                                    firstNode.setId(secondNode.getId());
                                } else {
                                    firstNode.setId(nodeCount++);
                                    secondNode.setId(nodeCount);
                                }
                                elements.add(new Wire(firstNode,secondNode));
                                elements.add(new Wire(secondNode,firstNode));


                            } else if(MainFrame.selectedElement == ElementType.RESISTOR) {
                                elements.add(new Resistor(firstNode,secondNode,Double.parseDouble(input)));
                                elements.add(new Resistor(secondNode,firstNode,Double.parseDouble(input)));

                            } else if(MainFrame.selectedElement == ElementType.VOLTAGE_SOURCE) {
                                elements.add(new VoltageSource(firstNode,secondNode,-Double.parseDouble(input)));
                                elements.add(new VoltageSource(secondNode,firstNode,Double.parseDouble(input)));

                            } else if(MainFrame.selectedElement == ElementType.CURRENT_SOURCE) {
                                elements.add(new CurrentSource(firstNode,secondNode,-Double.parseDouble(input)));
                                elements.add(new CurrentSource(secondNode,firstNode,Double.parseDouble(input)));

                            }
                            circuitGraph.addEdge(firstNode, secondNode);
                            //circuitGraph.printAdjList();

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

    private boolean isInsideCircle(Node node, Point mousePoint) {
        return getDistance(node.getPoint().x, node.getPoint().y, mousePoint.x, mousePoint.y) <= NODE_DIAMETER;
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
        g.setColor(new Color(0xB6B7B7));
        for(int i = 0; i < NUMBER_OF_NODES; i++) {
            if(nodes[i].isHovering()) {
                g.setColor(new Color(0x8E8FF8));
                g.fillRect(nodes[i].getPoint().x-2, nodes[i].getPoint().y-2, NODE_DIAMETER+4, NODE_DIAMETER+4);
                g.setColor(new Color(0xB6B7B7));
                continue;
            }
            if(nodes[i].isGrounded()) {
                g.setColor(new Color(0x1A1A1C));
                g.fillOval(nodes[i].getPoint().x-2,nodes[i].getPoint().y-2, NODE_DIAMETER+4, NODE_DIAMETER+4);
                g.setColor(new Color(0xB6B7B7));
                continue;
            }
            g.fillOval(nodes[i].getPoint().x,nodes[i].getPoint().y, NODE_DIAMETER, NODE_DIAMETER);
        }
    }
}
