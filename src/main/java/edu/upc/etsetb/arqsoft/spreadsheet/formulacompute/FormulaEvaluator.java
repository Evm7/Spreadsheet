/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.formulacompute;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Term;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.Function;
import edu.upc.etsetb.arqsoft.spreadsheet.exceptions.GrammarErrorFormula;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import jdk.nashorn.internal.runtime.ParserException;

/**
 * Class used to Evaluate a Formula: tokenize, parse, evaluate grammar, compute
 * Shanting Yard Algorithm and compute Post Fix Expression
 *
 * @author estev
 */
public class FormulaEvaluator {

    Tokenizer tokenizer;
    boolean debug = false;
    public PostFixEvaluator postfixEvaluator;
    ShuntingYardAlgorithm shuntingYardAlgorithm;
    GrammarEvaluator grammarEvaluator;
    public CircularDependencies circularDependencies;

    
    /**
     * Contains an static function with all the functions that implement
     * Interface Function. Used to compute the Operator and Operand Function
     */
    public static HashMap<String, Function> functions;

    /**
     * Constructor which initialize the TokenInfo to tokenize for the formula.
     * Also initialize the Parser of the Formula and the Functions. Allow
     * scalability to add new tokens or functions.
     */
    public FormulaEvaluator() {
        tokenizer = new Tokenizer();
        // Add the tokens to our series.
        tokenizer.add("SUMA|MIN|MAX|PROMEDIO|suma|min|max|promedio", TokenType.FORMULA, 5); // function
        tokenizer.add("\\(", TokenType.OPEN_BRACKET, 0); // open bracket
        tokenizer.add("\\)", TokenType.CLOSE_BRACKET, 0); // close bracket
        tokenizer.add("\\+", TokenType.PLUS, 1); // plus
        tokenizer.add("\\-", TokenType.MINUS, 1); // minus
        tokenizer.add("\\*", TokenType.MULT, 2); // mult
        tokenizer.add("\\/", TokenType.DIVIDE, 2); // divide
        tokenizer.add("\\^", TokenType.RAISED, 3); // raised
        //tokenizer.add("[0-9]+", REAL_NUMBER); // integer number
        tokenizer.add("[+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)", TokenType.REAL_NUMBER, 0); // Real number
        tokenizer.add("[a-zA-Z][a-zA-Z0-9_]*", TokenType.CELL, 0); // cell
        tokenizer.add("\\s", TokenType.WHITE_SPACE, 0); // white space
        tokenizer.add("\\=", TokenType.EQUAL, 0); // equal
        tokenizer.add("\\;", TokenType.SEMICOLON, 4); // semicolon
        tokenizer.add("\\:", TokenType.COLON, 5); // colon
        tokenizer.add("\\:", TokenType.RANGE, 5); // range

        functions = new HashMap<>();
        functions.put("MAX", new FunctionMax());
        functions.put("MIN", new FunctionMin());
        functions.put("PROMEDIO", new FunctionPromedio());
        functions.put("SUMA", new FunctionSuma());
        
        
        postfixEvaluator = new PostFixEvaluator();
        shuntingYardAlgorithm = new ShuntingYardAlgorithm();
        grammarEvaluator = new GrammarEvaluator();
        circularDependencies = new CircularDependencies();
    }

    
   /**
    *  Establishes the evaluator in Debugging mode. 
    * @param mode True if wans to print the steps followed to evaluate formula.
    */
    public void setDebugger(boolean mode) {
        debug = mode;
        postfixEvaluator.debug=mode;
        shuntingYardAlgorithm.debug=mode;
        grammarEvaluator.debug=mode;
        circularDependencies.debug =mode;
    }

    /**
     * Parses a Formula by Tokenizing, evaluating the Grammar and creating the
     * Post Fix Expression.Uses the help of the Parser.
     *
     * @param formula Formula as String
     * @return a List of Terms that are contained in the Formula as Post Fix
     * expression
     * @throws edu.upc.etsetb.arqsoft.spreadsheet.exceptions.GrammarErrorFormula Exception raised when formula does contain a not correct parameter
     */
    public List<Term> parseFormula(String formula) throws GrammarErrorFormula {
        formula = formula.replaceAll(" ", "");
        print(debug, "_______________________ PARSING FORMULA _______________________");
        try {
            print(debug, "[INFO] .. Tokenizing formula : " + formula);
            tokenizer.tokenize(formula);

        } catch (ParserException e) {
            System.out.println(e.getMessage());
        }
        print(debug, "[INFO] .. Evaluating Grammar");
        LinkedList<Tokenizer.Token> tokens = grammarEvaluator.evaluateGrammar(tokenizer.getTokens());
        tokens.pop(); // To remove the initial equal
        for (Tokenizer.Token tok : tokens) {
            print(debug, " [ " + tok.token + " ] ");
        }

        print(debug, "[INFO] .. Creating PostFix (Shaunting Yard Algorithm)");
        tokens = shuntingYardAlgorithm.shuntingYard(tokens);
        for (Tokenizer.Token tok : tokens) {
            print(debug, tok.sequence + "   ");
        }
        List<Term> terms = convertTokenToOTerm(tokens);
        for (Term tok : terms) {
            print(debug, tok.toString() + "   ");
        }
        return terms;
    }

    /**
     * Converts a List of Tokens to a list of Terms.
     *
     * @param tokens
     * @return List of Terms
     */
    private List<Term> convertTokenToOTerm(LinkedList<Tokenizer.Token> tokens) {
        List<Term> terms = new LinkedList<>();
        int facts = 0;
        for (Tokenizer.Token tok : tokens) {
            TokenType type = tok.token;
            switch (type) {
                case REAL_NUMBER:
                    terms.add(new OperandNumber(Double.parseDouble(tok.sequence)));
                    break;
                case CELL:
                    terms.add(new ArgumentIndividual(tok.sequence));
                    break;
                case RANGE:
                    terms.add(new ArgumentRange(tok.sequence));
                    break;
                case FORMULA:
                    terms.add(new OperatorFunction(tok.sequence, facts));
                    facts = 0;
                    break;
                case PLUS:
                    terms.add(new OperatorImpl(tok.sequence));
                    break;
                case MINUS:
                    terms.add(new OperatorImpl(tok.sequence));
                    break;
                case DIVIDE:
                    terms.add(new OperatorImpl(tok.sequence));
                    break;
                case MULT:
                    terms.add(new OperatorImpl(tok.sequence));
                    break;
                case RAISED:
                    terms.add(new OperatorImpl(tok.sequence));
                    break;
                case SEMICOLON:
                    facts++;
                    break;
            }
        }
        return terms;
    }

    private void print(boolean visualize, String s) {
        if (visualize) {
            System.out.println(s);
        }
    }
}
