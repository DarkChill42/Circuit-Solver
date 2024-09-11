package Main;

import GUI.*;
import Elements.ElementType;

import javax.swing.*;
import java.awt.*;
public class MainFrame extends JFrame {

    public static ElementType selectedElement = ElementType.WIRE;

    public MainFrame() {
        setTitle("Electric Circuit Solver");
        setSize(1000,800);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.add(new MainPane(), BorderLayout.CENTER);
        this.setJMenuBar(createMenuBar());
        setVisible(true);

    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu elementMenu = new JMenu("Element");
        JMenuItem resistorItem = new JMenuItem("Resistor");
        resistorItem.addActionListener(e -> {
            System.out.println("Resistor selected");
            selectedElement = ElementType.RESISTOR;
        });

        JMenuItem wireItem = new JMenuItem("Wire");
        wireItem.addActionListener(e -> {
            System.out.println("Wire selected");
            selectedElement = ElementType.WIRE;
        });

        JMenuItem voltageSourceItem = new JMenuItem("Voltage Source");
        voltageSourceItem.addActionListener(e -> {
            System.out.println("Voltage Source selected");
            selectedElement = ElementType.VOLTAGE_SOURCE;
        });

        JMenuItem currentSourceItem = new JMenuItem("Current Source");
        currentSourceItem.addActionListener(e -> {
            System.out.println("Current Source selected");
            selectedElement = ElementType.CURRENT_SOURCE;
        });

        elementMenu.add(resistorItem);
        elementMenu.add(wireItem);
        elementMenu.add(voltageSourceItem);
        elementMenu.add(currentSourceItem);

        JMenu optionsMenu = new JMenu("Options");

        JMenu helpMenu = new JMenu("Help");

        menuBar.add(elementMenu);
        menuBar.add(optionsMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponents(g);
    }
}
