/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

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
     * @param content
     */
    public ValueText(ContentText content) {
        this.value = content.getContent();
    }

    /**
     * Get the value as String
     *
     * @return
     */
    @Override
    public String getValue() {
        return value;
    }

    /**
     * Set the Value
     *
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Get the representatonas String of the Value: as TextValue, same as
     * getValue()
     *
     * @return
     */
    @Override
    public String print() {
        return this.value;
    }
}
