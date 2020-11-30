/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Function;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.Value;
import java.util.List;

/**
 *
 * @author estev
 */
public class FunctionMin implements Function{
    

    public FunctionMin() {
    }
    
    @Override
    public Double[] parseArguments() throws NoParseableArguments{
        throw new NoParseableArguments("Not supported yet."); 
    }

    @Override
    public OperandNumber computeFormula(OperandFunction args) {
        List<OperandNumber> values = args.getValue();
        OperandNumber result = values.get(0);
        for (OperandNumber v: values){
            if ((Double) result.getValue() > (Double) v.getValue()){
                result = v;
            }
        }
        return result;
    }
    
    
    
}
