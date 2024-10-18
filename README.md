# Circuit Solver

This project is designed for solving simple electronic circuits using **Node-Voltage Analysis**. It can handle basic circuit elements such as:

- Resistors
- Wires
- Voltage Sources
- Current Sources

Currently, the application allows you to build and analyze circuits consisting of these components and calculate the node voltages across different points in the circuit.

## Features

- **Visual Interface**: Build your circuits using an intuitive grid layout and drag-and-drop components.
- **Automatic Analysis**: Solve your circuit using node-voltage analysis to find the potential at each node.
- **Component Types**: Add resistors, voltage sources, current sources, and wires to form any type of circuit.
- **Breadboard Simulation**: Build circuits as if using a breadboard layout, making it easy to visualize connections and relationships between elements.

## Example

Here is a snapshot of the application with a simple circuit:

In this example, the circuit consists of voltage sources (marked with "V"), resistors, and current sources (marked with "A"). The Node ID and voltage at selected nodes are displayed in the bottom right corner for easy reference.


![Circuit Solver Example](https://github.com/user-attachments/assets/a63d6002-110a-4a37-8ac5-4c34d8109287)

## Future Enhancements

- Support for additional circuit elements (e.g., capacitors)
- Improving current nodal-analysis alogrithm so it handles more circuit examples.
- Advanced analysis techniques, including mesh current analysis
- Support for exporting circuit to a .txt file and importing from it.

