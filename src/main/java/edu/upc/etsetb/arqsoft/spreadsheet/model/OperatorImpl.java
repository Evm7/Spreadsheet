/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Operator;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.Operand;

/**
 *
 * @author estev
 */
public class OperatorImpl implements Operator {

    private int weight;
    public String sign;

    public OperatorImpl(String sign) {
        this.sign = sign;
        if ((this.sign == "/") || (this.sign == "*")) {
            this.weight = 1;
        } else if ((this.sign == "+") || (this.sign == "-")) {
            this.weight = 2;
        } else if (this.sign == ";") {
            this.weight = 3;
        }
    }

    @Override
    public int getWeight() {
        return this.weight;
    }

    public String getSign() {
        return sign;
    }

    @Override
    public Operand computeOperation(Operand arg1, Operand arg2) {
        Double value = null;
        if (this.sign == "/") {
            value = (Double) (arg1.getValue()) / (Double) (arg2.getValue());

        } else if (this.sign == "*") {
            value = (Double) (arg1.getValue()) * (Double) (arg2.getValue());

        } else if (this.sign == "+") {
            value = (Double) (arg1.getValue()) + (Double) (arg2.getValue());

        } else if (this.sign == "-") {
            value = (Double) (arg1.getValue()) - (Double) (arg2.getValue());

        } else if (this.sign == ";") {
            value = value = (Double) (arg1.getValue());
        }
        return new OperandNumber(value);
    }
}
