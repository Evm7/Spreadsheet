/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import edu.upc.etsetb.arqsoft.spreadsheet.exceptions.NoNumberException;

/**
 * Contains the Value of a Cell which is explicitly a Text. Used for Text
 * Content
 *
 * @author estev
 */
public class ValueText extends CellValue {

    private String value;

    /**
     * Constructor that initialize a Value of Text.
     *
     * @param content Cell content
     */
    public ValueText(ContentText content) {
        this.value = content.getContent();
    }

    /**
     * Get the value as String
     *
     * @return Cell value
     */
    @Override
    public String getValue() {
        return value;
    }

    /**
     * Set the Value
     *
     * @param value Value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Get the representatonas String of the Value: as TextValue, same as
     * getValue()
     *
     * @return Cell value as String
     */
    @Override
    public String toString() {
        return this.value;
    }

    /**
     * Only used for testing
     *
     * @return Cell value as Double
     * @throws NoNumberException Exception raised when formula does contain a not correct value
     */
    @Override
    public Double getValueasDouble() throws NoNumberException{
        throw new NoNumberException("The value of the cell is "+ this.value);
    }
}
