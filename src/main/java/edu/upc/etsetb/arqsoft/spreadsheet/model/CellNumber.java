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
public class CellNumber extends Cell{
    private Float value;
    
    public CellNumber(int row, int column, Float value) {
        super(row, column, TypeOfContent.NUMBER);
        this.value = value;
    }


    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }
    
        public void show(){
        System.out.println("Cell ["+this.column+" "+ this.row+"] ("+this.type_of_content+"): "+this.value);
    }
        
    public String printValue(){
        return ""+ this.value;
    }
    
    public String getSource(){
        return ""+ this.value;
    }
        
}
