/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Argument;

/**
 *
 * @author estev
 */
public class ArgumentFunction implements Argument {

    String arg;  // Example:  different Arguments separated by ; --> 3;4;5;A1:3B

    public ArgumentFunction(String arg) {
        this.arg = arg;
    }

    @Override
    public OperandFunction getValue() {
        OperandNumber[] example = new OperandNumber[]{new OperandNumber(4.0),new OperandNumber(1.0),new OperandNumber(2.0)};
        System.out.println("4;1;2");
        return new OperandFunction(example);
    }
    

    @Override
    public String getSource() {
        return arg;
    }

}
