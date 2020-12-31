/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascar�
 */
package edu.upc.etsetb.arqsoft.spreadsheet.exceptions;

/**
 * Exception raise when a command can't be correctly read 
 * 
 * 
 * @author estev
 */
public class ReadCommandException extends Exception{
    
        /**
     * Exception raise when a command can't be correctly read 
     * @param string String to display
     */
    public ReadCommandException(String string) {
        super(string);
    }
    
}
