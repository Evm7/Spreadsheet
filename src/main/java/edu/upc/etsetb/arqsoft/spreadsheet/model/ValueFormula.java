/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Value;

/**
 *
 * @author estev
 */
public class ValueFormula extends CellValue {
    private Value[] values;

    public ValueFormula(Value[] value) {
        this.values = value;
    }

    @Override
    public Value[] getValue() {
        return values;
    }
    
    @Override
    public String print(){
        String result = "";
        for(Value f: values){
            result = result +" "+ f.getValue();
        }
        return result;
    }
    

}
