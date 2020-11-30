/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.Term;

/**
 *
 * @author estev
 */
public class OperandFunction implements Term {

    List<Term> operants;

    public OperandFunction(List<Term> operants) {
        this.operants = operants;
    }

    public List<OperandNumber> getValue() {
        return flatten(this);
    }

    private List<OperandNumber> flatten(OperandFunction fun) {
        List<OperandNumber> flatList = new ArrayList<OperandNumber>();
        for (Term op : fun.operants) {
            if (op instanceof OperandNumber) {
                flatList.add((OperandNumber) op);
            } else if (op instanceof ArgumentRange) {
                flatList.addAll(flatten(((ArgumentRange) op).getValue()));
            }else if (op instanceof ArgumentIndividual) {
                flatList.add(((ArgumentIndividual) op).getValue());
            }else {
                flatList.addAll(flatten((OperandFunction) op));
            }
        }
        return flatList;
    }

    public String print() {
        String res = "[";
        for (Term op : operants) {
            res = res + op.print() + " ; ";
        }
        return res + "]";
    }

    @Override
    public String isType() {
        return "OperandFunction";
    }

}
