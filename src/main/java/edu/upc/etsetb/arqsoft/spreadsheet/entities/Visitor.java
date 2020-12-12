/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascar�
 */
package edu.upc.etsetb.arqsoft.spreadsheet.entities;

import edu.upc.etsetb.arqsoft.spreadsheet.model.Argument;
import edu.upc.etsetb.arqsoft.spreadsheet.model.OperandFunction;
import edu.upc.etsetb.arqsoft.spreadsheet.model.OperandNumber;
import edu.upc.etsetb.arqsoft.spreadsheet.model.OperatorFunction;
import edu.upc.etsetb.arqsoft.spreadsheet.model.OperatorImpl;

/**
 *
 * @author estev
 */
public interface Visitor {
    void visitOperatorImpl(OperatorImpl operator);
    void visitOperatorFunction(OperatorFunction operator);
    void visitArgument(Argument argument);
    void visitOperandFunction(OperandFunction operand);
    void visitOperandNumber(OperandNumber operand);
}
