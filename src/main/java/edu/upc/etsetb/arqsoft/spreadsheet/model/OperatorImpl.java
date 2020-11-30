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
public class OperatorImpl implements Term {

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

    public int getWeight() {
        return this.weight;
    }

    public String print() {
        return sign;
    }

    public OperandNumber computeOperation(OperandNumber arg1, OperandNumber arg2) {
        Double value = null;
        if (this.sign.equals("/")) {
            value = (Double) (arg1.getValue()) / (Double) (arg2.getValue());

        } else if (this.sign.equals("*")) {
            value = (Double) (arg1.getValue()) * (Double) (arg2.getValue());

        } else if (this.sign.equals("+")) {
            value = (Double) (arg1.getValue()) + (Double) (arg2.getValue());

        } else if (this.sign.equals("-")) {
            value = (Double) (arg1.getValue()) - (Double) (arg2.getValue());
        } else if (this.sign.equals("^")) {
            value = Math.pow((Double) (arg1.getValue()), (Double) (arg2.getValue()));

        } else if (this.sign.equals(";")) {
            value =  arg1.getValue();
        }
        return new OperandNumber(value);
    }

    @Override
    public String isType() {
        return "OperatorImpl";
    }
    
        @Override
    public String getValue() {
        return sign;
    }
}
