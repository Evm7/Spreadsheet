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
public class CellText extends Cell{
    
    private String value;
    
    public CellText(int row, int column, String text) {
        super(row,column,  TypeOfContent.TEXT);
        this.value = value;
        
    }

    public String printValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
        
    public void show(){
        System.out.println("Cell ["+this.column+" "+ this.row+"] ("+this.type_of_content+"): "+this.value);
    }
    
    public String getSource(){
        return value;
    }
}
