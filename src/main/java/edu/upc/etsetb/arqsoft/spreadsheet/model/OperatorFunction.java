/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Term;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.Function;

/**
 *
 * @author estev
 */
public class OperatorFunction implements Term {

    private String formula;
    private int num_terms;

    public OperatorFunction(String type, int num_terms) {
        this.formula = type;
        this.num_terms = num_terms;
    }

    public String print() {
        return formula;
    }
    
    public int getTerms(){
        return num_terms;
    }

    public OperandNumber computeOperation(OperandFunction arg) {
        Function function = FormulaEvaluator.functions.get(this.formula);
        return function.computeFormula(arg);
    }

    public String isType() {
        return "OperatorFunction";
    }

    @Override
    public String getValue() {
        return formula;
    }
}
