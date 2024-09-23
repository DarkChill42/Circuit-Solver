package Circuit;

import Elements.Element;
import Elements.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Graph {

    private HashMap<Node, List<Node>> adjacencyList;
    private List<Element> elements;

    private List<Element> toAdd;

    private List<Branch> branches;

    private List<Node> junctions;

    public int numberOfEquations = 0;

    public Graph(List<Element> elements) {
        this.toAdd = new ArrayList<>();
        this.branches = new ArrayList<>();
        this.junctions = new ArrayList<>();
        this.elements = elements;
        adjacencyList = new HashMap<>();
    }

    public void addEdge(Node start, Node end) {
        adjacencyList.putIfAbsent(start, new ArrayList<>());
        adjacencyList.putIfAbsent(end, new ArrayList<>());

        adjacencyList.get(start).add(end);
        adjacencyList.get(end).add(start);

        if(adjacencyList.get(start).size() >= 3) start.setJunction(true);
        if(adjacencyList.get(end).size() >= 3) end.setJunction(true);
    }

    public HashMap<Node, List<Node>> getAdjacencyList() {
        return adjacencyList;
    }

    public List<Branch> getBranches() {
        return branches;
    }

    public List<Node> getJunctions() {
        return junctions;
    }

    public void findBranch(Node startNode) {
        List<Node> visited = new ArrayList<>();
        visited.add(startNode);

        findBranchRec(startNode, visited);
    }

    public void findBranchRec(Node startNode, List<Node> visited) {
        for(Node n : adjacencyList.get(startNode)) {
            if(!visited.contains(n)) {
                if(!n.isJunction()) {
                    visited.add(n);
                    toAdd.add(getElementFromList(startNode,n));
                    findBranchRec(n, visited);
                } else {
                    toAdd.add(getElementFromList(startNode,n));

                    Branch branch = getBranch();
                    toAdd.forEach(element -> {
                        branch.getElements().add(element);
                        branch.increaseCount(element.getType());
                    });
                    branches.add(branch);

                    toAdd.clear();
                }
            }
        }
    }

    private Branch getBranch() {
        Branch branch = new Branch();
        if(toAdd.get(0).getStart().isJunction()) {
            branch.setStart(toAdd.get(0).getStart());
        } else {
            branch.setStart(toAdd.get(0).getEnd());
        }

        if(toAdd.get(toAdd.size() - 1).getEnd().isJunction()) {
            branch.setEnd(toAdd.get(toAdd.size() - 1).getEnd());
        } else {
            branch.setEnd(toAdd.get(toAdd.size() - 1).getStart());
        }
        return branch;
    }

    public void printBranches() {
        branches.forEach(b->{
            System.out.println("Branch starting at: " + b.getStart().getId() + " and ending at: " + b.getEnd().getId() + " has next elements: ");
            b.getElements().forEach(element -> {
                System.out.println(element.getType() + ", ");
            });
        });
    }

    private Element getElementFromList(Node start, Node end) {
        for(Element element : elements) {
            if((element.getStart().equals(start)) && element.getEnd().equals(end)) {
                return element;
            }
        }
        for(Element element : elements) {
            if(element.getStart().equals(end) && element.getEnd().equals(start)) {
                return element;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Graph{" +
                "adjacencyList=" + adjacencyList +
                '}';
    }

    public void printAdjList() {
        adjacencyList.forEach((k,v)->{
            System.out.println("Node: " + k.getId() + " has " + v.size() + " connections." + " Junction: " + k.isJunction());
            System.out.println("They are: ");
            v.forEach(c-> System.out.print(c.getId() + " " + " Junction: " + c.isJunction() + ","));
            System.out.println();
        });
        System.out.println("******************************************************************");
    }
}
