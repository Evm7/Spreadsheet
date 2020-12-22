/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Value;

/**
 * Super Class implementing the Value InterFace
 *
 * @author estev
 */
public class CellValue implements Value {

    /**
     * Constructor empty.
     */
    public CellValue() {
    }

    /**
     * Get the Value of the Cell
     *
     * @return
     */
    public Object getValue() {
        return 0.0;
    }

    /**
     * Returns the value as String.
     * @return 
     */
    public String print() {
        return "";
    }

}
