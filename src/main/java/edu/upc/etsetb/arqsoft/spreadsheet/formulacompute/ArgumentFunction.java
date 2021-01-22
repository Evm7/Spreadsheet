/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.formulacompute;

import java.util.ArrayList;
import java.util.List;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.Term;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.Visitor;

/**
 * Class which implements Term Interface Contains a list of Operands that are
 * used to compute a Function
 *
 * @author estev
 */
public class ArgumentFunction implements Term {

    List<Term> operants;

    /**
     * Constructor which creates a Term container for a List of Terms
     *
     * @param operants List of terms
     */
    public ArgumentFunction(List<Term> operants) {
        this.operants = operants;
    }

    /**
     * Get Value returns a List of OperandNumber
     *
     * @return List of Operand Numbers
     */
    public List<OperandNumber> getValue() {
        return flatten(this);
    }

    /**
     * Used to flatten the List of Terms into a List of Operand Numbers. If the
     * List of Terms contains another OperandFunction, also Flatten recursively
     * the OperandFunction
     *
     * @param fun
     * @return List of Operand Numbers
     */
    private List<OperandNumber> flatten(ArgumentFunction fun) {
        List<OperandNumber> flatList = new ArrayList<OperandNumber>();
        for (Term op : fun.operants) {
            if (op instanceof OperandNumber) {
                flatList.add((OperandNumber) op);
            } else if (op instanceof ArgumentRange) {
                flatList.addAll(flatten(((ArgumentRange) op).getValue()));
            } else if (op instanceof ArgumentIndividual) {
                flatList.add(((ArgumentIndividual) op).getValue());
            } else {
                flatList.addAll(flatten((ArgumentFunction) op));
            }
        }
        return flatList;
    }

    /**
     * gets as String the List of terms. For debugging use.
     *
     * @return List of terms as String
     */
    public String toString() {
        String res = "[";
        for (Term op : operants) {
            res = res + op.toString() + " ; ";
        }
        return res + "]";
    }

    public Double getDouble() {
        return Double.parseDouble("0");
    }
    
    public OperandNumber getOperand() {
        return new OperandNumber(getDouble());
    }

    /**
     * Return the ArgumentFunction type.
     *
     * @return Operand Function type
     */
    @Override
    public String isType() {
        return "OperandFunction";
    }

    @Override
    public void acceptVisitor(Visitor v) {
        v.visitOperandFunction(this);
    }

}
