/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Operant;

/**
 *
 * @author estev
 */
public class OperantNumber implements Operant {

    Double operant;

    public OperantNumber(Double operant) {
        this.operant = operant;
    }


    public Double getValue() {
        return this.operant;
    }

    @Override
    public String print() {
        return ""+ this.operant;
    }
}
