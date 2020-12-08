/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

/**
 * Exception class for error in Formula arguments
 * @author estev
 */
public class NoParseableArguments extends Exception {

    /**
     * Exception raise when exists Terms in the formula can not be parsed correctly.
     * Format of formula inserted by user is not correct or adequate.
     * @param string
     */
    public NoParseableArguments(String string) {
        super(string);
    }
    
}
