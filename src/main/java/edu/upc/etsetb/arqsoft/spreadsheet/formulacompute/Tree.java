/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascar�
 */
package edu.upc.etsetb.arqsoft.spreadsheet.formulacompute;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Term;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * The @author of SpreadSheet is estev This class is basically used to check
 * comprehension of update SpreadSheet
 */
public class Tree {

    HashMap<String, ArrayList> map = new HashMap<String, ArrayList>();
    ArrayList<String> stack = new ArrayList<>();

    public Tree(HashMap<String, ArrayList> map, String computeCell) {
        this.map = map;
        stack.add(computeCell);
        Node root = addNode(null, computeCell);
        printNode(root);
        errorDependencies(root);
    }

    private Node addNode(Node root, String id) {
        if (root == null) {
            System.out.println("Starting with node root " + id);
        } else {
            System.out.println("We are adding the node " + id + " to root " + root.toString());
            System.out.println("The stack is: " + this.stack.toString());
        }
        Node now = new Node(id, root);
        if (this.map.containsKey(id)) {
            for (Iterator<String> iterator = this.map.get(id).iterator(); iterator.hasNext();) {
                String next = iterator.next();
                //System.out.println("Searching now for node " + next);

                if (!stack.contains(next)) {
                    stack.add(next);
                    Node child = addNode(now, next);
                    now.addChild(child);
                } else {
                    System.out.println("The node " + next + " is already contained in the stack. Adding as child to " + now.value);
                    now.addChild(new Node(next, now));

                }
            }
        }

        return now;
    }

    private ArrayList<String> notDependent(HashMap<String, ArrayList> map) {
        ArrayList<String> notDependent = new ArrayList<>();
        for (Map.Entry<String, ArrayList> entry : map.entrySet()) {
            ArrayList<String> val = entry.getValue();
            for (Iterator<String> iterator = val.iterator(); iterator.hasNext();) {
                String next = iterator.next();
                if (!map.containsKey(next)) {
                    notDependent.add(next);
                }
            }
        }
        return notDependent;
    }

    private void printNode(Node root) {
        HashMap<Integer, ArrayList> depths = getDepths(root);
        for (Map.Entry<Integer, ArrayList> entry : depths.entrySet()) {
            int key = entry.getKey();
            System.out.println("Depth: " + key);
            for (Object object : entry.getValue()) {
                System.out.print("\t" + ((Node) object).value);
            }
            System.out.println("");
        }
    }

    private HashMap<Integer, ArrayList> getDepths(Node root) {
        HashMap<Integer, ArrayList> depths = new HashMap<>();
        depths.put(0, new ArrayList(Arrays.asList(root)));
        depths = getDepth(root, depths);
        return depths;
    }

    /**
     * Checks all the Dependencies of the formula if there is error or not.
     *
     * @param root initial node to check
     * @return True if error, False if not.
     */
    private boolean errorDependencies(Node root) {
        HashMap<Integer, ArrayList> depths = getDepths(root);
        boolean results = false;
        int size = depths.size() - 1;
        for (int i = size; i >= 0; i--) {
            for (Object object : depths.get(i)) {
                Node nd = (Node) object;
                System.out.println("Checking circular dependency of node " + nd.value);
                results = results || checkCircularDependency(nd, nd);
            }

        }
        return results;
    }

    /*
    * Checking it the node Checker has circular dependencies.
    *  True --> has circular dependencies
    *  False --> Does not have any circular dependency.
    *  used recursively
     */
    private boolean checkCircularDependency(Node node, Node checker) {
        Node parent = node.parent;
        if ((parent != null) && (checker != null)){
            System.out.println("\tChecking node " + checker.value + " and its parent " + parent.value);
        }
        if (parent == null) {
            return false;
        } else if (parent.value == checker.value) {
            System.out.println("\t\tThere is a Circular Dependency between " + checker.value + " and its parent " + parent.value);
            return true;
        } else {
            return checkCircularDependency(parent, checker);
        }
    }

    private HashMap<Integer, ArrayList> getDepth(Node node, HashMap<Integer, ArrayList> depths) {
        for (Node nd : node.children) {
            ArrayList<Node> nodes;
            if (!depths.containsKey(nd.depth)) {
                nodes = new ArrayList<>();
            } else {
                nodes = depths.get(nd.depth);
            }
            nodes.add(nd);
            depths.put(nd.depth, nodes);
            depths = getDepth(nd, depths);
        }
        return depths;
    }

    private class Node {

        private ArrayList<Node> children = null;
        private Node parent = null;
        private String value;
        private int depth = 0;

        public Node(String value, Node parent) {
            this.children = new ArrayList<Node>();
            this.value = value;
            this.parent = parent;
            if (parent == null) {
                depth = 0;
            } else {
                depth = parent.depth + 1;
            }
        }

        public void addChild(Node child) {
            children.add(child);
        }

        public Node getParent() {
            return this.parent;
        }

        public void print() {
            System.out.print("Node " + value);
        }

        public String toString() {
            return this.value;
        }
    }

    /**
     * Used for Debugging with different type of functions
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        HashMap<String, ArrayList> map = new HashMap<String, ArrayList>();
        map.put("D2", new ArrayList(Arrays.asList("D1", "C3")));
        map.put("D1", new ArrayList(Arrays.asList("A3", "B2")));
        map.put("C3", new ArrayList(Arrays.asList("A3", "B2", "B1")));
        //map.put("A3", new ArrayList(Arrays.asList("B2")));
        map.put("A3", new ArrayList(Arrays.asList("D1", "B2")));  //WE HAVE TO AVOID THIS
        map.put("B2", new ArrayList(Arrays.asList("A1")));

        Tree tree = new Tree(map, "D2");
    }

}
