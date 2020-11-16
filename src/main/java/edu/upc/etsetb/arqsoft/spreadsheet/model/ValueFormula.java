/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Argument;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.Function;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.Operant;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.Operator;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.Value;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import javafx.util.Pair;

/**
 *
 * @author estev
 */
public class ValueFormula extends CellValue {
    private Double value;

    public ValueFormula(ContentFormula content) {
        Pair<Argument[], Operator[]>[] pairs = content.getPairs();
        this.value = computeFormula(pairs);
    }

    @Override
    public Double getValue() {
        return value;
    }
    
    @Override
    public String print(){
        return ""+ this.value;
    }
    
     public Double computeFormula(Pair<Argument[], Operator[]>[] pairs) {
        Operant[] values_op = new Operant[pairs.length];
        int i = 0;
        for (Pair pair : pairs) {
            System.out.println(i + ".- Computing Formula ");
            Operant[] operant = rearrangeArguments(values_op, (Argument[]) pair.getKey());
            values_op[i] = calculateFormula(operant, (Operator[]) pair.getValue());
            System.out.println("Result is " + values_op[i].print());
            i++;
        }

        return ((OperantNumber) values_op[values_op.length-1]).getValue();
    }

    private Operant[] rearrangeArguments(Operant[] values, Argument[] arguments) {
        String source;
        Operant[] operants = new Operant[arguments.length];
        Operant value;
        String name;
        boolean modified = false;
        for (int i = 0; i < arguments.length; i++) {
            source = arguments[i].getSource();
            modified = false;
            // COMPUTE FORMULAS
            for (Map.Entry<String, Function> entry : FormulaParser.functions.entrySet()) {
                name = entry.getKey();
                if (source.contains(name)) {
                    value = values[Integer.parseInt(source.replaceAll("[A-Z_]", ""))];
                    System.out.println("Value is " + value.print());
                    Operant result = entry.getValue().computeFormula((OperantFunction) value);
                    operants[i] = result;
                    modified = true;
                }
            }
            // COMPUTE SUBSTITUTIONS
            if (!modified) {
                if(source.contains("_")){
                    operants[i] = values[Integer.parseInt(source.replaceAll("[A-Z_]", ""))];
                    modified = true;
                }else {
                    operants[i] = arguments[i].getValue();
                }
            }

        }
        return operants;
    }

    private Operant calculateFormula(Operant[] operant, Operator[] operator) {
        printList(operant);
        printList(operator);
        // First we need to compute MULTIPLICATIONS and DIVISIONS
        ArrayList<Operant> args = new ArrayList<>();
        ArrayList<Operator> op = new ArrayList<>();
        if (operator.length > 0) {
            Operant value = null;
            // FOR WEIGHT = 1 --> PRODUCT AND DIVISIONS
            boolean none1 = false;
            boolean sameWeight = false;
            for (int i = 0; i < operator.length; i++) {
                if (operator[i].getWeight() == 1) {
                    none1 = true;
                    if (sameWeight) {
                        value = operator[i].computeOperation(value, operant[i + 1]);

                    } else {
                        value = operator[i].computeOperation(operant[i], operant[i + 1]);
                    }
                    args.add(value);
                    sameWeight = true;
                } else {
                    args.add(operant[i]);
                    op.add(operator[i]);
                    if ((!none1) && (i + 1 >= operator.length)) {
                        args.add(operant[i + 1]);
                    }
                    sameWeight = false;
                }
            }
            printIteratorOp(op);
            printIteratorOperant(args);

            ArrayList<Operant> last_args = new ArrayList<>();
            ArrayList<Operator> last_op = new ArrayList<>();
            none1 = false;
            sameWeight = false;
            // FOR WEIGHT = 2 --> SUM AND SUBSTRACT
            if (op.size() != 0) {
                for (int i = 0; i < op.size(); i++) {
                    if (op.get(i).getWeight() == 2) {
                        none1 = true;

                        if (sameWeight) {
                            value = op.get(i).computeOperation(value, args.get(i + 1));

                        } else {
                            value = op.get(i).computeOperation(args.get(i), args.get(i + 1));
                        }
                        last_args.add(value);
                        sameWeight = true;
                    } else {
                        last_args.add(args.get(i));
                        if ((!none1) && (i + 1 >= op.size())) {
                            last_args.add(args.get(i + 1));
                        }
                        sameWeight = false;
                    }
                }
            } else {
                last_args.add(args.get(0));
            }
            printIteratorOperant(last_args);
            if (last_args.size() > 1) {
                Operant[] values = new Operant[last_args.size()];
                for (int i = 0; i < last_args.size(); i++) {
                    values[i] = (Operant) last_args.get(i);
                }
                return new OperantFunction(values);
            } else {
                return last_args.get(0);
            }
        } else {
            return operant[0];
        }

    }

    private void printIteratorOperant(ArrayList list) {
        Iterator<Operant> it = list.iterator();
        while (it.hasNext()) {
            System.out.print(it.next().print());
        }
        System.out.println("");
    }

    private void printIteratorOp(ArrayList list) {
        Iterator<Operator> it = list.iterator();
        while (it.hasNext()) {
            System.out.print("  " + it.next().getSign());
        }
        System.out.println("");

    }

    private void printList(Operant[] operant) {
        for (int i = 0; i < operant.length; i++) {
            System.out.println("\tArgument - Pos: " + i + " with value " + (operant[i].print()));
        }
    }

    private void printList(Operator[] operator) {
        for (int i = 0; i < operator.length; i++) {
            System.out.println("\tOperator - Pos: " + i + " with sign " + operator[i].getSign() + "  ");
        }
    }
    

}
