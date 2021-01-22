/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascar�
 */
package edu.upc.etsetb.arqsoft.spreadsheet.entities;

import edu.upc.etsetb.arqsoft.spreadsheet.formulacompute.Argument;
import edu.upc.etsetb.arqsoft.spreadsheet.formulacompute.ArgumentFunction;
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
     * @param operator Operator
     */
    void visitOperatorImpl(OperatorImpl operator);

    /**
     * Adds the OperatorFunction term to the queue
     * 
     * @param operator Function Operator
     */
    void visitOperatorFunction(OperatorFunction operator);

    /**
     * Adds the Argument term to the queue
     * 
     * @param argument Argument
     */
    void visitArgument(Argument argument);

    /**
     * Adds the ArgumentFunction term to the queue
     * 
     * @param operand Function Operand
     */
    void visitOperandFunction(ArgumentFunction operand);

    /**
     * Adds the OperandNumber term to the queue
     * 
     * @param operand Number Operand
     */
    void visitOperandNumber(OperandNumber operand);
}
