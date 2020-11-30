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
public class OperandNumber implements Term {

    Double operant;

    public OperandNumber(Double operant) {
        this.operant = operant;
    }

    public Double getValue() {
        return this.operant;
    }

    public String print() {
        return "" + this.operant;
    }

    @Override
    public String isType() {
        return "OperandNumber";
    }
}
