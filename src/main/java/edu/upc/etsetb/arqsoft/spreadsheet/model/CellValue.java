/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Value;
import edu.upc.etsetb.arqsoft.spreadsheet.exceptions.NoNumberException;

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
     * @return Value of the cell
     */
    @Override
    public Object getValue() {
        return 0.0;
    }

    /**
     * Returns the value as String.
     *
     * @return Value of the cell as a String
     */
    @Override
    public String toString() {
        return "";
    }

    /**
    * Only used for testing
     * @return Value of the cell as Double
     * @throws edu.upc.etsetb.arqsoft.spreadsheet.exceptions.NoNumberException 
     * Raised when formula does contain an incorrect content String
     */
    public Double getValueasDouble()  throws NoNumberException {
        return 0.0;
    }

}
