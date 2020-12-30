/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascar�
 */
package edu.upc.etsetb.arqsoft.spreadsheet.formulacompute;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Term;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.Visitor;
import edu.upc.etsetb.arqsoft.spreadsheet.exceptions.MathematicalInvalidation;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * The @author of SpreadSheet is estev
 *
 * This class implements the Visitor Pattern to operate Formulas
 */
public class VisitorFormula implements Visitor {

    LinkedList<Term> queue;
    boolean debug = false;

    /**
     * Constructor of VisitorFormula. Contains a List with all the values of the
     * formula to Operate.
     *
     * @param debug used to test and debug the program
     */
    public VisitorFormula(boolean debug) {
        this.debug = debug;
        queue = new LinkedList<Term>();  // for values

    }

    /**
     * Function only used for printing steps of Visitor Formula when debugging.
     *
     * @param testing
     * @param toprint
     */
    private void print(String toprint) {
        if (this.debug) {
            System.out.println(toprint);
        }
    }

    /**
     * The term is an operator so we are opering the last two operants of the
     * queue and add the result to the queue.
     * 
     * If division to 0 error, then show error. Should throw the error.
     *
     * @param term
     */
    @Override
    public void visitOperatorImpl(OperatorImpl term) {
        print("\tThe term is an operator");
        Term second = queue.removeLast();
        Term first = queue.removeLast();
        try {
            queue.add(operate(first, second, (OperatorImpl) term));
        } catch (MathematicalInvalidation ex) {
            queue.add(new OperandNumber(0.0));
            System.out.println("ERROR: " + ex.getMessage());
        }
    }

    /**
     * Computes the value of a function given all its arguments
     * @param term 
     */
    @Override
    public void visitOperatorFunction(OperatorFunction term) {
        queue = operateFunction(queue, (OperatorFunction) term);

    }

    /**
     * Adds the Argument to the queue
     * @param term 
     */
    @Override
    public void visitArgument(Argument term) {
        print("\t\tAdding to queue as value");
        queue.add(term);
    }

    /**
     * Adds the OperandFunction to the queue
     * @param term 
     */
    @Override
    public void visitOperandFunction(OperandFunction term) {
        print("\t\tAdding to queue as value");
        queue.add(term);
    }

    /**
     * Adds the OperandNumber to the queue
     * @param term 
     */
    @Override
    public void visitOperandNumber(OperandNumber term) {
        print("\t\tAdding to queue as value");
        queue.add(term);
    }

    /**
     * Get the result of the last term in the queue as a Double value.
     * @return the final result (as a double) of the formula
     */
    public Double getResult() {
        Term result = queue.getLast();
        if (result instanceof OperandNumber) {
            return ((OperandNumber) result).getValue();
        } else if (result instanceof ArgumentIndividual) {
            return ((ArgumentIndividual) result).getValue().getValue();
        } else {
            print("Error " + result.toString());
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
    private OperandNumber operate(Term first, Term second, OperatorImpl operator) throws MathematicalInvalidation {
        print("\t\t\tWe are computing: " + first.toString() + " " + operator.toString() + " " + second.toString());
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
        print("\t\t\tWe are computing a formula " + operator.toString());
        List<Term> operands = new LinkedList<>();
        int index = operator.getTerms() + 1;
        print("\t\t\tNumber of items are " + index);

        for (int count = 0; count < index; count++) {
            Term term = queue.remove(queue.size() - 1);
            operands.add(term);
            print("[ " + term.toString() + " ]  ");
        }
        print("");
        OperandNumber num = ((OperatorFunction) operator).computeOperation(new OperandFunction(operands));
        queue.add(num);
        return queue;

    }

}
