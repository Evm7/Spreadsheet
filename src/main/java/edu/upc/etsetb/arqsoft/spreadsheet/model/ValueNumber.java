/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import edu.upc.etsetb.arqsoft.spreadsheet.exceptions.CircularDependencies;

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
     * @param content
     */
    public ValueNumber(ContentNumber content) {
        this.value = Double.parseDouble(content.getContent());
    }

    /**
     * Constructor that initialize a Value of Number if the content is Formula
     *
     * @param content
     */
    public ValueNumber(ContentFormula content, CellCoordinate coordinate) throws CircularDependencies {
        SpreadSheet.parser.circularDependencies(coordinate);
        this.value = SpreadSheet.parser.evaluatePostFix(content.getTerms());
    }

    /**
     * Get the value as Double
     *
     * @return
     */
    @Override
    public Double getValue() {
        return value;
    }

    /**
     * Stablish a precise value
     *
     * @param value
     */
    public void setValue(Double value) {
        this.value = value;
    }

    /**
     * Gets the String representation of the value
     *
     * @return
     */
    @Override
    public String print() {
        return "" + this.value;
    }
}
