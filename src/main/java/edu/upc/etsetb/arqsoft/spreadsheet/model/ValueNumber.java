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

    public ValueNumber(ContentNumber content) {
        this.value = Double.parseDouble(content.getContent());
    }

    @Override
    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String print() {
        return "" + this.value;
    }
}
