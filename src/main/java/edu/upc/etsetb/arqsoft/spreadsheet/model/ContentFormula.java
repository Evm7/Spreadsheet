/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Term;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author estev
 */
public class ContentFormula extends CellContent {

    private String formula;
    private List<Term> terms;

    /**
     *
     * @param formula
     */
    public ContentFormula(String formula) {
        super(TypeOfContent.FORMULA, formula);
        this.formula = formula;
        this.terms = new FormulaEvaluator().parseFormula(formula);  // SHOULD USE A FORMULA EVALUATOR ALREADY STIPULATED IN SPREADSHEET, JUST FOR TESTING
    }

    private boolean checkFormula() {
        return true;
    }

    /**
     *
     * @return
     */
    public List<Term> getTerms() {
        return terms;
    }

    /**
     *
     * @return
     */
    public List<Argument> getArguments() {
        List<Argument> arguments = new ArrayList<>();
        for (Term term : terms) {
            if (term.isType().equals("Argument")) {
                arguments.add((Argument) term);
            }
        }
        return arguments;
    }
}
