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
public class ContentFormula extends CellContent{
    
    private String formula;
    private Argument arguments[];
    
    public ContentFormula(String formula) {
        super(TypeOfContent.FORMULA, formula);
        this.formula = formula;
        
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

    
}
