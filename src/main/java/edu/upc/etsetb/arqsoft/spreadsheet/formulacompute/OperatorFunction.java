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
     * @param type Type of function
     * @param num_terms Number of terms
     */
    public OperatorFunction(String type, int num_terms) {
        this.formula = type;
        this.num_terms = num_terms;
    }

    /**
     * Print the type of formula SUMA; MIN; MAX; PROMEDIO
     *
     * @return Type of formula
     */
    public String toString() {
        return formula;
    }

    /**
     * Get the number of arguments
     *
     * @return Number of arguments
     */
    public int getArguments() {
        return num_terms;
    }

    /**
     * Compute the Function specified due to the ArgumentFunction
     *
     * @param arg Argument
     * @return Result of the computed function
     */
    public OperandNumber computeOperation(ArgumentFunction arg) {
        Function function = FormulaEvaluator.functions.get(this.formula);
        return function.computeFunction(arg);
    }

    /**
     * Get the Type of Term
     *
     * @return Type of term
     */
    public String isType() {
        return "OperatorFunction";
    }

    /**
     * Get the Value of the operand. Same as print: SUMA; MIN; MAX; PROMEDIO
     *
     * @return Operand value
     */
    @Override
    public String getValue() {
        return formula;
    }

    @Override
    public Double getDouble() {
        return null;
    }

    @Override
    public void acceptVisitor(Visitor v) {
        v.visitOperatorFunction(this);
    }

    public OperandNumber getOperand() {
        return null;
    }
}
