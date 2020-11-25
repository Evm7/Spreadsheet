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
public class ArgumentRange implements Argument {

    String arg; // When the argument refers to more than one cell in a range --> ex:  A1:B3

    public ArgumentRange(String arg) {
        this.arg = arg;
    }

    @Override
    public OperandFunction getValue() {
        OperandNumber[] example = new OperandNumber[]{new OperandNumber(3.0),new OperandNumber(6.0),new OperandNumber(12.0)};
        System.out.println("3;6;12");
        return new OperandFunction(example);
    }

    @Override
    public String getSource() {
        return arg;
    }

}
