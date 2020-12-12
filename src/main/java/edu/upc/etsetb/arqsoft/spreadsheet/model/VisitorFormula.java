/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascar�
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Term;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.Visitor;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * The @author of SpreadSheet is estev
 */
public class VisitorFormula implements Visitor {

    LinkedList<Term> queue;

    public VisitorFormula() {
        queue = new LinkedList<Term>();  // for values

    }

    @Override
    public void visitOperatorImpl(OperatorImpl term) {
        System.out.println("\tThe term is an operator");
        Term second = queue.removeLast();
        Term first = queue.removeLast();
        queue.add(operate(first, second, (OperatorImpl) term));
    }

    @Override
    public void visitOperatorFunction(OperatorFunction term) {
        queue = operateFunction(queue, (OperatorFunction) term);

    }

    @Override
    public void visitArgument(Argument term) {
        System.out.println("\t\tAdding to queue as value");
        queue.add(term);
    }

    @Override
    public void visitOperandFunction(OperandFunction term) {
        System.out.println("\t\tAdding to queue as value");
        queue.add(term);
    }

    @Override
    public void visitOperandNumber(OperandNumber term) {
        System.out.println("\t\tAdding to queue as value");
        queue.add(term);
    }

    public Double getResult() {
        Term result = queue.getLast();
        if (result instanceof OperandNumber) {
            return ((OperandNumber) result).getValue();
        } else if (result instanceof ArgumentIndividual) {
            return ((ArgumentIndividual) result).getValue().getValue();
        } else {
            System.out.println("Error " + result.print());
            return 0.0;
        }
    }

    /**
     * Operates two Terms when the operator is a OperatorImpl
     *
     * @param first OperandNumber or ArgumentIndividual
     * @param second OperandNumber or ArgumentIndividual
     * @param operator OperatorImpl
     * @return
     */
    private OperandNumber operate(Term first, Term second, OperatorImpl operator) {
        System.out.println("\t\t\tWe are computing: " + first.print() + " " + operator.print() + " " + second.print());
        OperandNumber first_val;
        OperandNumber second_val;
        if (first instanceof OperandNumber) {
            first_val = (OperandNumber) first;
        } else {
            first_val = (OperandNumber) first.getValue();
        }

        if (second instanceof OperandNumber) {
            second_val = (OperandNumber) second;
        } else {
            second_val = (OperandNumber) second.getValue();
        }
        return operator.computeOperation(first_val, second_val);
    }

    /**
     * Operates a list of Terms for when the operator is a Function. Creates an
     * OperandFunction from the list of Terms and Operates the terms that
     * Function contains in getTerms().
     *
     * @param queue List of Terms remaining in the queue of the PostFix
     * Expression
     * @param operator OperatorFunction
     * @return New List of Terms that remains after the Operation.
     */
    private LinkedList<Term> operateFunction(LinkedList<Term> queue, OperatorFunction operator) {
        System.out.println("\t\t\tWe are computing a formula " + operator.print());
        List<Term> operands = new LinkedList<Term>();
        int index = operator.getTerms() + 1;
        System.out.println("\t\t\tNumber of items are " + index);

        for (int count = 0; count < index; count++) {
            Term term = queue.remove(queue.size() - 1);
            operands.add(term);
            System.out.print("[ " + term.print() + " ]  ");
        }
        System.out.println();
        OperandNumber num = ((OperatorFunction) operator).computeOperation(new OperandFunction(operands));
        queue.add(num);
        return queue;

    }

}
