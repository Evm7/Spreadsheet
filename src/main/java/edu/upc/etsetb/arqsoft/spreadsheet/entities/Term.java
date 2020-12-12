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
     * @return String
     */
    String print();

    /**
     * Shows the Type of term we are refering to as String Argument |
     * OperandFunction | OperandNumber | OperatorImpl
     *
     * @return
     */
    String isType();

    /**
     * Gets the value of the Term.
     *
     * @return
     */
    Object getValue();
    
    /**
     * Visitor pattern
     * @param v
     */
    void acceptVisitor(Visitor v);
}
