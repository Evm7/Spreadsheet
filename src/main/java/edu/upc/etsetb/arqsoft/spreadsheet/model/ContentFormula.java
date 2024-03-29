/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import edu.upc.etsetb.arqsoft.spreadsheet.formulacompute.FormulaEvaluator;
import edu.upc.etsetb.arqsoft.spreadsheet.formulacompute.Argument;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.Term;
import edu.upc.etsetb.arqsoft.spreadsheet.exceptions.GrammarErrorFormula;
import java.util.ArrayList;
import java.util.List;

/**
 * Class of the Content of a Cell when Formula.
 *
 * @author estev
 */
public class ContentFormula extends CellContent {

    private String formula;
    private List<Term> terms;

    /**
     * Constructor which evaluates the formula passed as string and creates the
     * list of terms.To Evaluate Formula we are using Formula Evaluator.
     *
     * @param formula Formula to evaluate
     * @throws edu.upc.etsetb.arqsoft.spreadsheet.exceptions.GrammarErrorFormula Raised when an incorrect String param is introduced
     */
    public ContentFormula(String formula) throws GrammarErrorFormula {
        super(TypeOfContent.FORMULA, formula);
        this.formula = formula;
        this.terms = new FormulaEvaluator().parseFormula(formula);
    }

    @Override
    public String toString() {
        return "ContentFormula{" + "formula=" + formula + '}';
    }

    /**
     * Getter of the List of Terms contained in the Formula as Post Fix
     * Expression.
     *
     * @return List of terms of the formula
     */
    public List<Term> getTerms() {
        return terms;
    }

    /**
     * Getter of the arguments referenced in the formula.
     *
     * @return List of all the arguments contained in the formula.
     */
    public List<Argument> getArguments() {
        List<Argument> arguments = new ArrayList<>();
        for (Term term : terms) {
            if (term.isType().equals("Argument")) {
                arguments.add((Argument) term);
            }
        }
        return arguments;
    }
}
