/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.formulacompute;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Term;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.Visitor;

/**
 * Class which implements Term Interface
 * Contains an OperandNumber that is used to compute a Formula

 * @author estev
 */
public class OperandNumber implements Term {

    Double operant;

    /**
     * Constructor of an OperandNumber
     * @param operant Operand
     */
    public OperandNumber(Double operant) {
        this.operant = operant;
    }

    /**
     * Gets the value of the Operand as Double
     * @return Value of the operand as a Double
     */
    @Override
    public Double getValue() {
        return this.operant;
    }

    /**
     * Gets the value as String
     * @return Value of the operand as a String
     */
    @Override
    public String toString() {
        return "" + this.operant;
    }

    /**
     * Return the OperandNumber type.
     * @return Operand type
     */
    @Override
    public String isType() {
        return "OperandNumber";
    }
    
        @Override
    public void acceptVisitor(Visitor v) {
        v.visitOperandNumber(this);
    }
}
