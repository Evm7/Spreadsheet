/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.formulacompute;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Term;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.Function;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.Visitor;

/**
 * Class which implements Term Interface Used to Operate a Function explicit by
 * the Content: Reflects operators that implement Function Interface
 *
 * @author estev
 */
public class OperatorFunction implements Term {

    private String formula;
    private int num_terms;

    /**
     * Constructor of the OperatorFunction which initialize the type of Function
     * and Num of Terms
     *
     * @param type
     * @param num_terms
     */
    public OperatorFunction(String type, int num_terms) {
        this.formula = type;
        this.num_terms = num_terms;
    }

    /**
     * Print the type of formula SUMA; MIN; MAX; PROMEDIO
     *
     * @return
     */
    public String print() {
        return formula;
    }

    /**
     * Get the number of terms
     *
     * @return
     */
    public int getTerms() {
        return num_terms;
    }

    /**
     * Compute the Function specified due to the OperandFunction
     *
     * @param arg
     * @return
     */
    public OperandNumber computeOperation(OperandFunction arg) {
        Function function = FormulaEvaluator.functions.get(this.formula);
        return function.computeFunction(arg);
    }

    /**
     * Get the Type of Term
     *
     * @return
     */
    public String isType() {
        return "OperatorFunction";
    }

    /**
     * Get the Value of the operand. Same as print: SUMA; MIN; MAX; PROMEDIO
     *
     * @return
     */
    @Override
    public String getValue() {
        return formula;
    }

    @Override
    public void acceptVisitor(Visitor v) {
        v.visitOperatorFunction(this);
    }
}
