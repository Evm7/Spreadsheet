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
public class ContentNumber extends CellContent{
    
    /**
     *
     * @param value
     */
    public ContentNumber(String value) {
        super(TypeOfContent.NUMBER, value);
    }
}
