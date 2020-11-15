/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Argument;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.Function;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.Value;

/**
 *
 * @author estev
 */
public class FunctionSuma implements Function{
    
    private String arguments;
    private Double value;
    
    public FunctionSuma() {
    }
    
    @Override
    public Double[] parseArguments() throws NoParseableArguments{
        throw new NoParseableArguments("Not supported yet."); 
    }

    @Override
    public OperantNumber computeFormula(OperantFunction args) {
        OperantNumber[] values = args.getValue();
        Double result = new Double(0);
        for (OperantNumber v: values){
            result = result + (Double) (v.getValue());
        }
        return new OperantNumber(result);
    }
    
    
}
