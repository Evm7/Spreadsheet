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

    /**
     *
     */
    public String sign;

    /**
     *
     * @param sign
     */
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

    /**
     *
     * @return
     */
    public int getWeight() {
        return this.weight;
    }

    /**
     *
     * @return
     */
    public String print() {
        return sign;
    }

    /**
     *
     * @param arg1
     * @param arg2
     * @return
     */
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

    /**
     *
     * @return
     */
    @Override
    public String isType() {
        return "OperatorImpl";
    }
    
    /**
     *
     * @return
     */
    @Override
    public String getValue() {
        return sign;
    }
}
