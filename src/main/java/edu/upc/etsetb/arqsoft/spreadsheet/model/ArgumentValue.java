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
public class ArgumentValue implements Argument {

    String arg;  // When the argument refers to a determined value --> ex: 3

    public ArgumentValue(String arg) {
        this.arg = arg;
    }

    @Override
    public OperantNumber getValue() {
        return new OperantNumber(Double.parseDouble(arg));
    }

    @Override
    public String getSource() {
        return arg;
    }

}
