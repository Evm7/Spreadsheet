/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascar�
 */
package edu.upc.etsetb.arqsoft.spreadsheet.formulacompute;

import edu.upc.etsetb.arqsoft.spreadsheet.exceptions.CircularDependencies;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * The @author of SpreadSheet is estev This class is basically used to check
 * comprehension of update SpreadSheet
 */
public class TreeSearch {

    HashMap<String, ArrayList> map = new HashMap<String, ArrayList>();
    ArrayList<String> stack = new ArrayList<>();

    /**
     * Constructor of Tree Search
     *
     * @param map
     * @param computeCell
     * @throws CircularDependencies
     */
    public TreeSearch(HashMap<String, ArrayList> map, String computeCell) throws CircularDependencies {
        this.map = map;
        stack.add(computeCell);
        Node root = addNode(null, computeCell);
        //printNode(root);
        errorDependencies(root);
    }

    /**
     * Function used to add a Node with a given identifier to a root Node.
     * addNode uses recursivity to create a Tree from only a node.
     *
     * @param root
     * @param id
     * @return Node added
     */
    private Node addNode(Node root, String id) {
        if (root == null) {
            //System.out.println("Starting with node root " + id);
        } else {
            //System.out.println("We are adding the node " + id + " to root " + root.toString());
            //System.out.println("The stack is: " + this.stack.toString());
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
                    //System.out.println("The node " + next + " is already contained in the stack. Adding as child to " + now.value);
                    now.addChild(new Node(next, now));

                }
            }
        }

        return now;
    }

    /**
     * Function used to recursively get a map with eahc nodes depth starting
     * from the root (0 depth)
     *
     * @param root
     * @return map with all nodes and depths
     */
    private HashMap<Integer, ArrayList> getDepths(Node root) {
        HashMap<Integer, ArrayList> depths = new HashMap<>();
        depths.put(0, new ArrayList(Arrays.asList(root)));
        depths = getDepth(root, depths);
        return depths;
    }

    /**
     * Get exact depth of a node and adds it to a given map, which is returned
     * afterwards.
     *
     * @param node node to check the depth of
     * @param depths map with past depths
     * @return map updated with the new node depth
     */
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

    /**
     * Checks all the Dependencies of the formula if there is error or not.
     *
     * @param root initial node to check
     * @return True if error, False if not.
     * @throws CircuarDependencies
     */
    private boolean errorDependencies(Node root) throws CircularDependencies {
        HashMap<Integer, ArrayList> depths = getDepths(root);
        boolean results = false;
        int size = depths.size() - 1;
        for (int i = size; i >= 0; i--) {
            for (Object object : depths.get(i)) {
                Node nd = (Node) object;
                //System.out.println("Checking circular dependency of node " + nd.value);
                results = results || checkCircularDependency(nd, nd);
            }

        }
        return results;
    }

    /**
     * Checking it the node Checker has circular dependencies recursively True
     * --> has circular dependencies False --> Does not have any circular
     * dependency.
     *
     * @param Node whose parent are we checking
     * @param Node whose dependency we are checking
     * @throws CircularDependencies
     */
    private boolean checkCircularDependency(Node node, Node checker) throws CircularDependencies {
        Node parent = node.parent;
        if ((parent != null) && (checker != null)) {
            //System.out.println("\tChecking node " + checker.value + " and its parent " + parent.value);
        }
        if (parent == null) {
            return false;
        } else if (parent.value.equals(checker.value)) {
            throw new CircularDependencies("\t\tThere is a Circular Dependency between " + checker.value + " and its parent " + node.value);
        } else {
            return checkCircularDependency(parent, checker);
        }
    }

    /**
     * Node class is used to capture the essence of a given Node of a Tree.
     * Contains a List of its direct Children Nodes, the parent Node who it
     * depends of, its value, and the depth from the tree root
     */
    private class Node {

        private ArrayList<Node> children = null;
        private Node parent = null;
        private String value;
        private int depth = 0;

        /**
         * Constructor of the Node given its parent and the value.
         * @param value
         * @param parent if null, then we are referring to root Node
         */
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

        /**
         * Adds a Node to the children list
         * @param child 
         */
        public void addChild(Node child) {
            children.add(child);
        }

        /**
         * Gets the parent of the Node
         * @return 
         */
        public Node getParent() {
            return this.parent;
        }

        /**
         * Prints the Node value
         */
        public void print() {
            System.out.print("Node " + value);
        }

        @Override
        public String toString() {
            return this.value;
        }
    }
}
