/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.exceptions;

/**
 * Exception class with Formula Update problematic
 * @author estev
 */
public class CircularDependencies extends Exception {

    /**
     * Exception raise when exists two formula A and B, and
     * A depends on B and B depends on A.
     * 
     * @param string String to display
     */
    public CircularDependencies(String string) {
        super(string);
    }
    
}
