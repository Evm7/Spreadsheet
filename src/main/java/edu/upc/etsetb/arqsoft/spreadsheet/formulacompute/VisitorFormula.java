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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * The @author of SpreadSheet is estev
 */
public class VisitorFormula implements Visitor {

    LinkedList<Term> queue;
    boolean debug = false;
    

    public VisitorFormula(boolean debug) {
        debug=debug;
        queue = new LinkedList<Term>();  // for values

    }
    
    private void print(boolean testing, String toprint){
        if(testing){
            System.out.println(toprint);
        }
    }

    @Override
    public void visitOperatorImpl(OperatorImpl term) {
        print(debug, "\tThe term is an operator");
        Term second = queue.removeLast();
        Term first = queue.removeLast();
        try {
            queue.add(operate(first, second, (OperatorImpl) term));
        } catch (MathematicalInvalidation ex) {
            queue.add(new OperandNumber(0.0));
            System.out.println("ERROR: " + ex.getMessage());
        }
    }

    @Override
    public void visitOperatorFunction(OperatorFunction term) {
        queue = operateFunction(queue, (OperatorFunction) term);

    }

    @Override
    public void visitArgument(Argument term) {
        print(debug,"\t\tAdding to queue as value");
        queue.add(term);
    }

    @Override
    public void visitOperandFunction(OperandFunction term) {
        print(debug,"\t\tAdding to queue as value");
        queue.add(term);
    }

    @Override
    public void visitOperandNumber(OperandNumber term) {
        print(debug,"\t\tAdding to queue as value");
        queue.add(term);
    }

    public Double getResult() {
        Term result = queue.getLast();
        if (result instanceof OperandNumber) {
            return ((OperandNumber) result).getValue();
        } else if (result instanceof ArgumentIndividual) {
            return ((ArgumentIndividual) result).getValue().getValue();
        } else {
            print(debug,"Error " + result.toString());
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
        print(debug,"\t\t\tWe are computing: " + first.toString() + " " + operator.toString() + " " + second.toString());
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
        print(debug,"\t\t\tWe are computing a formula " + operator.toString());
        List<Term> operands = new LinkedList<Term>();
        int index = operator.getTerms() + 1;
        print(debug,"\t\t\tNumber of items are " + index);

        for (int count = 0; count < index; count++) {
            Term term = queue.remove(queue.size() - 1);
            operands.add(term);
            print(debug,"[ " + term.toString() + " ]  ");
        }
        print(debug,"");
        OperandNumber num = ((OperatorFunction) operator).computeOperation(new OperandFunction(operands));
        queue.add(num);
        return queue;

    }

}
