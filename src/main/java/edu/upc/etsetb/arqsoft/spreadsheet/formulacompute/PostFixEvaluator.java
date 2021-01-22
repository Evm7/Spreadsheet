/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascar�
 */
package edu.upc.etsetb.arqsoft.spreadsheet.formulacompute;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Term;
import java.util.List;

/**
 *
 * The @author of SpreadSheet is estev
 */
public class PostFixEvaluator {

    boolean debug = false;

    public PostFixEvaluator() {
    }
    

    private void print(boolean visualize, String s) {
        if (visualize) {
            System.out.println(s);
        }
    }

    /**
     * Evaluate the Post Fix Expression to compute the value depending on a List
     * of Terms passed as parameters.
     *
     * @param formula List of terms
     * @return A value as Double
     */
    public Double evaluatePostFix(List<Term> formula) {
        print(debug, "_______________________ EVALUATE POSTFIX");
        print(debug, "[INFO] .. Evaluating PostFix"); // =A1+B1*C1-PROMEDIO(A1:C1) //A	B	C	D	E	 1| 	5.0	3.0	3.65	
        VisitorFormula visitor = new VisitorFormula(debug);
        for (Term term : formula) {
            print(debug, "\t[evaluatePostFix] using term " + term.toString());
            term.acceptVisitor(visitor);
        }
        return visitor.getResult();
    }
}
