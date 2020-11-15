/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Operator;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.Argument;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.Function;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javafx.util.Pair;

/**
 *
 * @author estev
 */
public class FormulaParser {

    String[] FUNCTIONS;
    public static HashMap<String, Operator> operators;
    public static HashMap<String, Function> functions;

    public FormulaParser() {
        FUNCTIONS = new String[]{"SUMA", "MIN", "MAX", "PROMEDIO"};
        operators = new HashMap<>();
        operators.put(";", new OperatorSeparator());
        operators.put("/", new OperatorDivision());
        operators.put("*", new OperatorProduct());
        operators.put("-", new OperatorSubstract());
        operators.put("+", new OperatorSum());
        
        functions = new HashMap<>();
        functions.put("MAX", new FunctionMax());
        functions.put("MIN", new FunctionMin());
        functions.put("PROMEDIO", new FunctionPromedio());
        functions.put("SUMA", new FunctionSuma());

    }

    public void print(String sth) {
        System.out.println(sth);
    }

    public Pair[] parseFormula(String formula) {
        // To avoid the errors by deleting the spaces and the equal
        formula = formula.replace("=", "").replace(" ", "");
        print("The formula to parse is : " + formula);
        // GET THE ORDER OF COMPUTING THE FORMULA
        String[] order = getOrder(formula);
        Pair[] argument_operator = new Pair[order.length];
        int i = 0;
        for (String ord : order) {
            Pair pair = computePartial(ord);
            argument_operator[i] = computePartial(ord);
            i++;
        }
        return argument_operator;
    }
    

    private String containers(String formula) {
        String results = "";
        for (String sign : this.operators.keySet()) {
            if (formula.contains(sign)) {
                results += sign;
            }
        }
        return results;
    }

    // TO UNDERSTAND WHAT IS IN THE FORMULA:
    private Pair<Argument[], Operator[]> computePartial(String partial) {
        partial = partial.replace("(", "").replace(")", "");
        String[] words_argum = partial.split("[^\\w:_]+");
        Argument[] arguments = new Argument[words_argum.length];
        int i = 0;
        for (String w : words_argum) {
            arguments[i] = obtainArgument(w);
            i++;
        }
        String word_operator = partial.replaceAll("[\\w:_]", "");
        Operator[] operators_list = new Operator[word_operator.length()];
        i=0;
        for (char w : word_operator.toCharArray()) {
            operators_list[i] = operators.get(w+"");
            i++;
        }

        return new Pair(arguments,operators_list);
    }

    private Argument obtainArgument(String arg) {
        Argument argument;
        if (arg.contains(":")) {
            return new ArgumentRange(arg);
        } else {
            if (inList(arg, this.FUNCTIONS)) {
                return new ArgumentFunction(arg);
            } else if (arg.matches(".*[A-Z]+.*")) {
                return new ArgumentIndividual(arg);
            } else {
                return new ArgumentValue(arg);
            }
        }

    }

    private boolean inList(String container, String[] items) {
        for (String i : items) {
            if (container.contains(i)) {
                return true;
            }
        }
        return false;
    }

    // TO ORDER THE FORMULA
    private String[] getOrder(String formula) {
        ArrayList<Integer> openers = getIndexesOf(formula, '(');
        ArrayList<Integer> closures = getIndexesOf(formula, ')');
        int size = openers.size();
        if (size == closures.size()) {
            int[][] relations = relateBrackets(openers, closures);
            String[] order = new String[size + 1];
            for (int i = 0; i < size; i++) {
                order[i] = formula.substring(relations[i][0], relations[i][1]) + ")";
            }
            order[size] = formula;
            for (int i = size; i > 0; i--) {
                for (int j = i - 1; j >= 0; j--) {
                    order[i] = order[i].replace(order[j], "_" + j + "_");
                }
            }

            return order;
        } else {
            print("Error --> no brackets coincidence");
        }
        return null;
    }

    private ArrayList<Integer> getIndexesOf(String formula, char guess) {
        ArrayList<Integer> positions = new ArrayList<Integer>();
        int index = formula.indexOf(guess);
        while (index >= 0) {
            positions.add(index);
            index = formula.indexOf(guess, index + 1);
        }
        return positions;
    }

    private int[][] relateBrackets(ArrayList<Integer> openers, ArrayList<Integer> closures) {
        int size = openers.size();
        int[][] segments = new int[openers.size()][2];
        Iterator it = closures.iterator();
        int num = 0;
        int[] seg;
        while (it.hasNext()) {
            seg = new int[2];
            seg[1] = (int) it.next();
            seg[0] = getLower(openers, seg[1]);
            segments[num] = seg;
            num = num + 1;
            openers.remove((Object) seg[0]);
        }

        return segments;
    }

    private int getLower(ArrayList<Integer> openers, int top) {
        Iterator it = openers.iterator();
        int lower, after = 0;
        while (it.hasNext()) {
            lower = (int) it.next();
            if (lower > top) {
                return after;
            }
            after = lower;
        }
        return after;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        String easy_suma = "=SUMA(A2:A5)";
        String difficult = "=1 + A1*((SUMA(A2:B5;PROMEDIO(B6:D8);C1;27)/4)+(D6-D8))";

        FormulaParser parser = new FormulaParser();
        ContentFormula content = new ContentFormula(difficult);
        content.computeFormula();
    }

}
