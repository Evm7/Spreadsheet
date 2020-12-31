/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import edu.upc.etsetb.arqsoft.spreadsheet.exceptions.CircularDependencies;
import edu.upc.etsetb.arqsoft.spreadsheet.exceptions.NoNumberException;

/**
 * Contains the Value of a Cell which is explicitly a Number. Used for Formula
 * and Number Contents.
 *
 * @author estev
 */
public class ValueNumber extends CellValue {

    private Double value;

    /**
     * Constructor that initialize a Value of Number if the content is Number
     *
     * @param content Cell content
     */
    public ValueNumber(ContentNumber content) {
        this.value = Double.parseDouble(content.getContent());
    }

    /**
     * Constructor that initialize a Value of Number if the content is Formula
     *
     * @param content Cell content
     * @param coordinate Cell coordinate
     * @throws CircularDependencies Exception raised when exists two formula A and B, and
     * A depends on B and B depends on A.
     */
    public ValueNumber(ContentFormula content, CellCoordinate coordinate) throws CircularDependencies {
        SpreadSheet.parser.circularDependencies(coordinate);
        this.value = SpreadSheet.parser.evaluatePostFix(content.getTerms());
    }

    /**
     * Get the value as Double
     *
     * @return Cell value
     */
    @Override
    public Double getValue() {
        return value;
    }

    /**
     * Stablish a precise value
     *
     * @param value Value to set
     */
    public void setValue(Double value) {
        this.value = value;
    }

    /**
     * Gets the String representation of the value
     *
     * @return Cell value as String
     */
    @Override
    public String toString() {
        return "" + this.value;
    }

    /**
     * Only used for testing
     *
     * @return Cell value as Double
     * @throws NoNumberException Exception raised when formula does contain a not correct value
     */
    @Override
    public Double getValueasDouble() throws NoNumberException {
        return value;
    }
}
