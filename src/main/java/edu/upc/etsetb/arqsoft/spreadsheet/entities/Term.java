/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
}
