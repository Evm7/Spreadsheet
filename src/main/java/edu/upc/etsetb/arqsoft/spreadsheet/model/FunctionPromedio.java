/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Function;

/**
 *
 * @author estev
 */
public class FunctionPromedio implements Function{
    
    private String arguments;
    private Float value;
    
    public FunctionPromedio(String arguments) {
        this.arguments=arguments;
    }
    
    @Override
    public Float[] parseArguments() throws NoParseableArguments{
        throw new NoParseableArguments("Not supported yet."); 
    }

    @Override
    public Float computeFormula() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
