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
public class OperandFunction implements Term {
    
    List<Term> operants;

    /**
     * Constructor which creates a Term container for a List of Terms
     *
     * @param operants
     */
    public OperandFunction(List<Term> operants) {
        this.operants = operants;
    }

    /**
     * Get Value returns a List of OperandNumber
     *
     * @return
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
     * @return
     */
    private List<OperandNumber> flatten(OperandFunction fun) {
        List<OperandNumber> flatList = new ArrayList<OperandNumber>();
        for (Term op : fun.operants) {
            if (op instanceof OperandNumber) {
                flatList.add((OperandNumber) op);
            } else if (op instanceof ArgumentRange) {
                flatList.addAll(flatten(((ArgumentRange) op).getValue()));
            } else if (op instanceof ArgumentIndividual) {
                flatList.add(((ArgumentIndividual) op).getValue());
            } else {
                flatList.addAll(flatten((OperandFunction) op));
            }
        }
        return flatList;
    }

    /**
     * gets as String the List of terms. For debugging use.
     *
     * @return
     */
    public String print() {
        String res = "[";
        for (Term op : operants) {
            res = res + op.print() + " ; ";
        }
        return res + "]";
    }

    /**
     * Return the OperandFunction type.
     *
     * @return
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
