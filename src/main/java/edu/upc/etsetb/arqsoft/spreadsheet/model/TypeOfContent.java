/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Content;

/**
 * Class used to classify the contents of the cells depending on how they are
 * made: FORMULA, NUMBER, TEXT or EMPTY
 *
 * @author estev
 */
public enum TypeOfContent implements Content {

    /**
     * A formula is a mathematical relationship or rule expressed in symbols,
     * which in a spreadsheet always starts with the character '='. A simple
     * example is "= 1 + 2"
     */
    FORMULA,
    /**
     * Contains numbers and no alphabetical characters
     */
    NUMBER,
    /**
     * Contains characters different of Numbers and does not start by ;
     */
    TEXT,
    /**
     * Does not Contain any Character
     */
    EMPTY
}
