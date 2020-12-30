/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.formulacompute;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Term;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.Visitor;
import edu.upc.etsetb.arqsoft.spreadsheet.exceptions.MathematicalInvalidation;

/**
 * Class which implements Term Interface Used to Operate Terms explicit by the
 * Content: Functions can be: +, -, *, /, ^
 *
 * @author estev
 */
public class OperatorImpl implements Term {

    private int weight;

    /**
     * Sign of the Operator: +, -, *, /, ^, ;
     */
    public String sign;

    /**
     * Initialize the weight of the term depending on Operator, used to compute
     * PostFixExpression
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
     * Get the weight of the Term
     *
     * @return
     */
    public int getWeight() {
        return this.weight;
    }

    /**
     * Print the sign
     *
     * @return
     */
    public String toString() {
        return sign;
    }

    /**
     * Compute the operation between the two Terms of both sides of the Operator
     *
     * @param arg1 left Term
     * @param arg2 right Term
     * @return
     * @throws edu.upc.etsetb.arqsoft.spreadsheet.exceptions.MathematicalInvalidation
     */
    public OperandNumber computeOperation(OperandNumber arg1, OperandNumber arg2) throws MathematicalInvalidation {
        Double value = null;
        if (this.sign.equals("/")) {
            if((Double) (arg2.getValue())==0){
                throw new MathematicalInvalidation("Error when computing operation "+ ((Double) (arg1.getValue()))+ "/0. Can not divide to 0" );
            }
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
            value = arg1.getValue();
        }
        return new OperandNumber(value);
    }

    /**
     * Get the Type of Term
     *
     * @return
     */
    @Override
    public String isType() {
        return "OperatorImpl";
    }

    /**
     * Get the Sign of the Operator. Same to print()
     *
     * @return
     */
    @Override
    public String getValue() {
        return sign;
    }

    @Override
    public void acceptVisitor(Visitor v) {
        v.visitOperatorImpl(this);
    }
}
