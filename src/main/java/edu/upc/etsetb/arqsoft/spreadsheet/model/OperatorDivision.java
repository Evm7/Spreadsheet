/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Operant;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.Operator;

/**
 *
 * @author estev
 */
public class OperatorDivision implements Operator {

    private int weight = 1;
    public String sign = "/";

    public OperatorDivision() {
    }

    @Override
    public int getWeight() {
        return this.weight;
    }

    public String getSign() {
        return sign;
    }

    @Override
    public Operant computeOperation(Operant arg1, Operant arg2) {
        Double value = (Double) (arg1.getValue()) / (Double) (arg2.getValue());
        return new OperantNumber(value);
    }
}
