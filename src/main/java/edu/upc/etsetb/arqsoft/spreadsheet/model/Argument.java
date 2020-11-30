/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Term;

/**
 *
 * @author estev
 */
public class Argument implements Term {

    String arg;  

    public Argument(String arg) {
        this.arg = arg;
    }

    @Override
    public String print() {
        return arg; 
    }
    
    public Term getValue() {
        return new OperandNumber(Double.parseDouble(arg));
    }

    public String getSource() {
        return arg;
    }

    @Override
    public String isType() {
        return "Argument";
    }
    
    

}
