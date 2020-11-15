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
public class ArgumentIndividual implements Argument {

    String arg;  // One the argument refers to individial cell --> ex: A1

    public ArgumentIndividual(String arg) {
        this.arg = arg;
    }

    @Override
    public OperantNumber getValue() {
        return new OperantNumber(Double.parseDouble("2.0"));
    }

    @Override
    public String getSource() {
        return arg;
    }

}
