/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Operant;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.Operator;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.Value;

/**
 *
 * @author estev
 */
public class OperatorSubstract implements Operator {

    private int weight = 2;
    public String sign = "-";

    public OperatorSubstract() {
    }

    public String getSign() {
        return sign;
    }

    @Override
    public int getWeight() {
        return this.weight;
    }

    @Override
    public Operant computeOperation(Operant arg1, Operant arg2) {
        Double value = (Double) (arg1.getValue()) - (Double) (arg2.getValue());
        return new OperantNumber(value);
    }

}
