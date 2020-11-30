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
public class ArgumentIndividual extends Argument {

    public ArgumentIndividual(String arg) {
        super(arg);     // One the argument refers to individial cell --> ex: A1

    }

    @Override
    public OperandNumber getValue() {
        return new OperandNumber(Double.parseDouble("2.0"));
    }

    @Override
    public String getSource() {
        return arg;
    }

}
