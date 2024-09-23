package Circuit;

import Elements.Element;
import Elements.ElementType;
import Elements.Node;
import Matrix.Matrix;

import java.util.*;

public class Solver {

    public static void test(List<Branch> branches, Graph circuitGraph) {
        boolean found = false;
        for (Branch branch : branches) {
            if(branch.getVoltageCount() > 0 && branch.getResistorCount() == 0  && branch.getCurrentCount() == 0) {
                circuitGraph.numberOfEquations = circuitGraph.numberOfEquations - 1; //
                System.out.println("Grounded node is: " + branch.getStart().getId());
                branch.getStart().setGrounded(true);
                groundWires(branches, branch.getStart(), circuitGraph);

                for(Node n : circuitGraph.getJunctions()) {
                    if(n.isGrounded()) {
                        System.out.println("Grounded after: " + n.getId());
                    }
                }

                System.out.println("**********************");

                for(Element element : branch.getElements()) {
                    if(element.getType() == ElementType.VOLTAGE_SOURCE) {
                        branch.getEnd().setPotential(element.getValue() * -1.0);
                        break;
                    }
                }
                setVoltage(branches,branch.getEnd(), circuitGraph);

                for(Node n : circuitGraph.getJunctions()) {
                    if(n.getPotential() != Double.MAX_VALUE) {
                        System.out.println("Potential for node: " + n.getId() + " is: " + n.getPotential());
                    }
                }
                found = true;
                break;
            }
        }
        if(!found) {
            for (Branch branch : branches) {
                if(branch.getVoltageCount() > 0) {
                    branch.getStart().setGrounded(true);
                    System.out.println("Grounded: " + branch.getStart().getId());
                    break;
                }
            }
        }

        test2(branches,circuitGraph);
    }

    private static void groundWires(List<Branch> branches, Node node, Graph circuitGraph) {
        for(Branch branch : branches) {
            if(branch.getResistorCount() == 0 && branch.getVoltageCount() == 0 && branch.getCurrentCount() == 0) {
                if(branch.getStart().equals(node) && !branch.getEnd().isGrounded()) {
                    branch.getEnd().setGrounded(true);
                    circuitGraph.numberOfEquations--;
                    groundWires(branches, branch.getEnd(), circuitGraph);
                }
                if(branch.getEnd().equals(node) && !branch.getStart().isGrounded()) {
                    branch.getStart().setGrounded(true);
                    circuitGraph.numberOfEquations--;
                    groundWires(branches, branch.getStart(), circuitGraph);
                }
            }
        }
    }

    private static void setVoltage(List<Branch> branches, Node node, Graph circuitGraph) {
        for(Branch branch : branches) {
            if(branch.getResistorCount() == 0 && branch.getVoltageCount() == 0 && branch.getCurrentCount() == 0) {
                if(branch.getStart().equals(node) && !(branch.getEnd().getPotential() != Double.MAX_VALUE)) {
                    branch.getEnd().setPotential(node.getPotential());
                    circuitGraph.numberOfEquations--;
                    setVoltage(branches, branch.getEnd(), circuitGraph);
                }
                if(branch.getEnd().equals(node) && !(branch.getStart().getPotential() != Double.MAX_VALUE)) {
                    branch.getStart().setPotential(node.getPotential());
                    circuitGraph.numberOfEquations--;
                    setVoltage(branches, branch.getStart(), circuitGraph);
                }
            }
        }
    }

    private static boolean containsElement(Branch branch, ElementType type) {
        for(Element element : branch.getElements()) {
            if(element.getType() == type) {
                return false;
            }
        }
        return true;
    }

    private static double getConductance(Node start, Node end, List<Branch> branches) {
        double result = 0.0;
        if(start.equals(end)) {
            for(Branch branch : branches) {
                double tempAdd = 0.0;
                if(branch.getStart().equals(start)) {
                    if(containsElement(branch, ElementType.CURRENT_SOURCE)) {
                        for(Element element : branch.getElements()) {
                            if(element.getType() == ElementType.RESISTOR) {
                                tempAdd+=element.getValue();
                            }
                        }
                    }
                }
                result+=(tempAdd == 0.0 ? 0.0 : 1.0 / tempAdd);
            }
            return result;
        } else {
            for(Branch branch : branches) {
                if(branch.getStart().equals(start) && branch.getEnd().equals(end)) {
                    if(containsElement(branch, ElementType.CURRENT_SOURCE)) {
                        double tempAdd = 0.0;
                        for(Element element : branch.getElements()) {
                            if(element.getType() == ElementType.RESISTOR && !branch.getEnd().isGrounded()) {
                                tempAdd+=element.getValue();
                            }
                        }
                        result += (tempAdd == 0.0 ? 0.0 : 1.0 / tempAdd);
                    }
                }
            }
        }
        return result;
    }

    private static double getCurrentForNode(Node node, List<Branch> branches) {
        double result = 0.0;
        for(Branch branch : branches) {
            if(branch.getStart().equals(node)) {
                if(branch.getEnd().getPotential() != Double.MAX_VALUE) {
                    double resistance = 0.0;
                    for(Element element : branch.getElements()) {
                        if(element.getType() == ElementType.CURRENT_SOURCE) {
                            resistance = 0.0;
                            break;
                        }
                        if(element.getType() == ElementType.RESISTOR) resistance+=element.getValue();
                    }
                    result+= (resistance == 0.0 ? 0.0 : 1.0/resistance)*branch.getEnd().getPotential();
                }
                for(Element element : branch.getElements()) {
                    if(element.getType() == ElementType.CURRENT_SOURCE) {
                        result+= element.getValue();
                    } else if(element.getType() == ElementType.VOLTAGE_SOURCE) {
                        double resistance = 0.0;
                        for(Element el : branch.getElements()) {
                            if(el.getType() == ElementType.RESISTOR) {
                                resistance+=el.getValue();
                            }
                        }
                        result+= resistance == 0.0 ? 0.0 : element.getValue() / resistance;
                    }
                }
            }
        }
        return result;
    }
    public static void test2(List<Branch> branches, Graph circuitGraph) {
        if(circuitGraph.numberOfEquations == 0) {
            System.out.println("Already known voltages...");
            return;
        }


        double[][] leftElements = new double[circuitGraph.numberOfEquations - 1][circuitGraph.numberOfEquations - 1];
        double[][] rightElements = new double[circuitGraph.numberOfEquations - 1][1];
        int row = 0, column = 0;
        for(Node node : circuitGraph.getJunctions()) {
            column = 0;
            if(node.isGrounded() || node.getPotential() != Double.MAX_VALUE) {
                continue;
            }
            for(Node node2 : circuitGraph.getJunctions()) {
                if(node2.isGrounded() || node2.getPotential() != Double.MAX_VALUE) continue;
                leftElements[row][column] = getConductance(node,node2,branches) * (node.equals(node2) ? 1.0 : -1.0);
                column++;
                System.out.println("Resistance between: " + node.getId() + " and: " + node2.getId() + " is: " + getConductance(node,node2,branches));
            }
            System.out.println("Current for node: " + node.getId() + " is: " + getCurrentForNode(node,branches));
            rightElements[row][0] = getCurrentForNode(node,branches);
            row++;
        }

        Matrix leftMatrix = new Matrix(leftElements, circuitGraph.numberOfEquations - 1);
        Matrix rightMatrix = new Matrix(rightElements, circuitGraph.numberOfEquations - 1, 1);

        System.out.println(leftMatrix);
        System.out.println("******************************");
        System.out.println(rightMatrix);


        System.out.println(Matrix.solveEquation(leftMatrix,rightMatrix));
    }




}
