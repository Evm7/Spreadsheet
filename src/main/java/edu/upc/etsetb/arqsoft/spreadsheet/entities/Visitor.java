/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascar�
 */
package edu.upc.etsetb.arqsoft.spreadsheet.entities;

import edu.upc.etsetb.arqsoft.spreadsheet.formulacompute.Argument;
import edu.upc.etsetb.arqsoft.spreadsheet.formulacompute.OperandFunction;
import edu.upc.etsetb.arqsoft.spreadsheet.formulacompute.OperandNumber;
import edu.upc.etsetb.arqsoft.spreadsheet.formulacompute.OperatorFunction;
import edu.upc.etsetb.arqsoft.spreadsheet.formulacompute.OperatorImpl;

/**
 *
 * @author estev
 */
public interface Visitor {

    /**
     * Computes the value of a Function (operating through all the corresponding
     * operands) and obtains the new queue
     * 
     * @param operator
     */
    void visitOperatorImpl(OperatorImpl operator);

    /**
     * Adds the OperatorFunction term to the queue
     * 
     * @param operator
     */
    void visitOperatorFunction(OperatorFunction operator);

    /**
     * Adds the Argument term to the queue
     * 
     * @param argument
     */
    void visitArgument(Argument argument);

    /**
     * Adds the OperandFunction term to the queue
     * 
     * @param operand
     */
    void visitOperandFunction(OperandFunction operand);

    /**
     * Adds the OperandNumber term to the queue
     * 
     * @param operand
     */
    void visitOperandNumber(OperandNumber operand);
}
