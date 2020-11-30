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
public class ArgumentValue extends Argument {

    public ArgumentValue(String arg) {
        super(arg); // When the argument refers to a determined value --> ex: 3

    }

    public OperandNumber getValue() {
        return new OperandNumber(Double.parseDouble(arg));
    }

    @Override
    public String getSource() {
        return arg;
    }

}
