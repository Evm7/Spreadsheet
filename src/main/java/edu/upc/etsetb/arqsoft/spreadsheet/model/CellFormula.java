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
public class CellFormula extends Cell{
    
    private String formula;
    private Float value;
    private Cell references[];
    
    public CellFormula(int row, int column, String formula) {
        super(row, column,  TypeOfContent.FORMULA);
        this.formula = formula;
        this.value = computeFormula();
        
    }
    
    private Float computeFormula(){
        if (!checkFormula()){
            throw(new Error("Formula contains an error"));
        }
        return new Float(3.14);
    }
    
    
    private boolean checkFormula(){
        return true;
    }
    
    public void show(){
        System.out.println("Cell ["+this.column+" "+ this.row+"] ("+this.type_of_content+"): "+this.value);
    }
    
    public String printValue(){
        return ""+ this.value;
    }
   
    public String getSource(){
        return formula;
    }
    
}
