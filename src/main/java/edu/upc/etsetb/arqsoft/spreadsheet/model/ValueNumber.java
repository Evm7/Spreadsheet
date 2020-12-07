/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

/**
 *
 * @author estev
 */
public class ValueNumber extends CellValue {

    private Double value;

    /**
     *
     * @param content
     */
    public ValueNumber(ContentNumber content) {
        this.value = Double.parseDouble(content.getContent());
    }
    
    /**
     *
     * @param content
     */
    public ValueNumber(ContentFormula content) {
        this.value = SpreadSheet.parser.evaluatePostFix(content.getTerms());
    }

    /**
     *
     * @return
     */
    @Override
    public Double getValue() {
        return value;
    }

    /**
     *
     * @param value
     */
    public void setValue(Double value) {
        this.value = value;
    }

    /**
     *
     * @return
     */
    @Override
    public String print() {
        return "" + this.value;
    }
}
