/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.entities;

/**
 * Interface which groups the different Terms that form the Content of a
 * Formula.
 *
 * @author estev
 */
public interface Term {

    /**
     * Prints the Term
     *
     * @return String Term as String
     */
    String toString();

    /**
     * Shows the Type of term we are refering to as String Argument |
     * OperandFunction | OperandNumber | OperatorImpl
     *
     * @return Type of the Term as String
     */
    String isType();

    /**
     * Gets the value of the Term.
     *
     * @return Value of the Term
     */
    Object getValue();
    
    /**
     * Visitor pattern
     * @param v Visitor to accept
     */
    void acceptVisitor(Visitor v);
}
