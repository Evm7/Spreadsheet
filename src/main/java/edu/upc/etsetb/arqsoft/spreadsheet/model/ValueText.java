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
public class ValueText extends CellValue {

    private String value;

    /**
     *
     * @param content
     */
    public ValueText(ContentText content) {
        this.value = content.getContent();
    }

    /**
     *
     * @return
     */
    @Override
    public String getValue() {
        return value;
    }

    /**
     *
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     *
     * @return
     */
    @Override
    public String print() {
        return this.value;
    }
}
