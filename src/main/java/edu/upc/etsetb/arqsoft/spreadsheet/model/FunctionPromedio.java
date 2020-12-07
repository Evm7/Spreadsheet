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
public class FunctionPromedio implements Function{
    
    /**
     *
     */
    public FunctionPromedio() {
    }
    
    /**
     *
     * @return
     * @throws NoParseableArguments
     */
    @Override
    public Double[] parseArguments() throws NoParseableArguments{
        throw new NoParseableArguments("Not supported yet."); 
    }

    /**
     *
     * @param args
     * @return
     */
    @Override
    public OperandNumber computeFunction(OperandFunction args) {
        List<OperandNumber> values = args.getValue();
        Double result = new Double(0);
        for (OperandNumber v: values){
            result = result + (Double) (v.getValue());
        }
        return new OperandNumber(result/values.size());
    }
    
    
    
}
