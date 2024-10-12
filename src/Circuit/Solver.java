package Circuit;

import Elements.Element;
import Elements.ElementType;
import Elements.Node;
import Matrix.Matrix;

import java.util.*;

public class Solver {

    public static void test(List<Branch> branches, Graph circuitGraph) {
        boolean found = false;


        //Todo: if there are more ideal branches ground the common node.
        HashMap<Node, Integer> nodeVoltageCount = new HashMap<>();
        for(Branch branch : branches) {
            if(branch.getVoltageCount() > 0 && branch.getResistorCount() == 0 && branch.getCurrentCount() == 0) {
                nodeVoltageCount.put(branch.getStart(), nodeVoltageCount.getOrDefault(branch.getStart(), 0) + 1);
                nodeVoltageCount.put(branch.getEnd(), nodeVoltageCount.getOrDefault(branch.getEnd(), 0) + 1);
            }
        }

        if(!nodeVoltageCount.isEmpty()) {
            Node grounded = nodeVoltageCount.entrySet()
                    .stream()
                    .max(Map.Entry.comparingByValue())
                    .get().getKey();

            circuitGraph.numberOfEquations = circuitGraph.numberOfEquations - nodeVoltageCount.get(grounded)/2;

            grounded.setGrounded(true);
            for(Branch branch : branches) {
                if(branch.getStart().getId() == grounded.getId() && !branch.getStart().isGrounded()) {
                    circuitGraph.numberOfEquations--;
                    branch.getStart().setGrounded(true);
                }
                if(branch.getEnd().getId() == grounded.getId() && !branch.getEnd().isGrounded()) {
                    circuitGraph.numberOfEquations--;
                    branch.getEnd().setGrounded(true);
                }
            }

            for(Branch branch : branches) {
                if(branch.getVoltageCount() > 0 && branch.getResistorCount() == 0 && branch.getCurrentCount() == 0 && branch.getStart().equals(grounded)) {
                    for(Element element : branch.getElements()) {
                        if(element.getType() == ElementType.WIRE) continue;
                        branch.getEnd().setPotential(element.getValue() * -1.0);
                        for(Branch toChange : branches) {
                            if(toChange.getStart().getId() == branch.getEnd().getId() && toChange.getStart().getPotential() == Double.MAX_VALUE) {
                                toChange.getStart().setPotential(branch.getEnd().getPotential());
                                circuitGraph.numberOfEquations--;

                            }
                            if(toChange.getEnd().getId() == branch.getEnd().getId() && toChange.getEnd().getPotential() == Double.MAX_VALUE) {
                                toChange.getEnd().setPotential(branch.getEnd().getPotential());
                                circuitGraph.numberOfEquations--;
                            }
                        }
                        break;
                    }
                }
            }
            found = true;
        }
        if(!found) {
            for (Branch branch : branches) {
                if(branch.getVoltageCount() > 0) {
                    branch.getStart().setGrounded(true);
                    for(Branch toGround : branches) {
                        if(toGround.getStart().getId() == branch.getStart().getId() && !toGround.getStart().isGrounded()) {
                            toGround.getStart().setGrounded(true);
                        }
                        if(toGround.getEnd().getId() == branch.getStart().getId() && !toGround.getEnd().isGrounded()) {
                            toGround.getEnd().setGrounded(true);
                        }
                    }
                    System.out.println("Grounded: " + branch.getStart().getId());
                    break;
                }
            }
        }
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        circuitGraph.getJunctions().forEach(j->{
            if(!j.isGrounded() && j.getPotential() == Double.MAX_VALUE) hashMap.put(j.getId(), hashMap.getOrDefault(j.getId(), 0) + 1);
        });

        hashMap.forEach((k,v)->{
            if(v != 1) {
                circuitGraph.numberOfEquations = circuitGraph.numberOfEquations - v + 1;
            }
        });
        test2(branches,circuitGraph);
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
                if(branch.getStart().getId() == start.getId()) {
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
                if(branch.getStart().getId() == start.getId() && branch.getEnd().getId() == end.getId()) {
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
            if(branch.getStart().getId() == node.getId()) {
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
        if(circuitGraph.numberOfEquations - 1 == 0) {
            System.out.println("Already known voltages...");
            return;
        }
        double[][] leftElements = new double[circuitGraph.numberOfEquations - 1][circuitGraph.numberOfEquations - 1];
        double[][] rightElements = new double[circuitGraph.numberOfEquations - 1][1];
        int row = 0, column;
        List<Node> junctions = new ArrayList<>();
        for(Node node : circuitGraph.getJunctions()) {
            if(hasNode(junctions, node) || node.getPotential() != Double.MAX_VALUE) continue;
            junctions.add(node);
        }
        for(Node node : junctions) {
            column = 0;
            if(node.isGrounded() || node.getPotential() != Double.MAX_VALUE) {
                continue;
            }
            for(Node node2 : junctions) {
                if(node2.isGrounded() || node2.getPotential() != Double.MAX_VALUE) continue;
                leftElements[row][column] = getConductance(node,node2,branches) * (node.equals(node2) ? 1.0 : -1.0);
                column++;
            }
            rightElements[row][0] = getCurrentForNode(node,branches);
            row++;
        }

        Matrix leftMatrix = new Matrix(leftElements, circuitGraph.numberOfEquations - 1);
        Matrix rightMatrix = new Matrix(rightElements, circuitGraph.numberOfEquations - 1, 1);

        Matrix solutionMatrix = Matrix.solveEquation(leftMatrix,rightMatrix);
        int counter = 0;
        for(Node n : junctions) {
            for(Node node : circuitGraph.getJunctions()) {
                if(n.getId() == node.getId()) node.setPotential(solutionMatrix.getElements()[counter][0]);
            }
            counter++;
        }
        System.out.println(solutionMatrix);

        calculateCurrents(branches);
    }

    private static boolean hasNode(List<Node> ordered, Node node) {
        for(Node n : ordered) {
            if(n.getId() == node.getId()) return true;
        }
        return false;
    }

    private static void calculateCurrents(List<Branch> branches) {
        for(Branch branch : branches) {
            double current = 0.0;
            boolean found = false;
            for(Element element : branch.getElements()) {
                if(element.getType() == ElementType.CURRENT_SOURCE) {
                    branch.setCurrent(element.getValue());
                    found = true;
                    break;
                } else if(element.getType() == ElementType.VOLTAGE_SOURCE) {
                    if(branch.getResistance() != 0.0) {
                        current += element.getValue() * -1.0;
                    }
                }
            }
            if(!found) {
                if (branch.getResistance() != 0.0) {
                    current += (branch.getStart().getPotential() - branch.getEnd().getPotential());
                    current /= branch.getResistance();
                }
                branch.setCurrent(current);
            }
        }
    }
}
