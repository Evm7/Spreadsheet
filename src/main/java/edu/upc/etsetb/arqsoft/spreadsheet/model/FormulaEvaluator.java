/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Term;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.Function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
    Parser parser_formula;

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

        parser_formula = new Parser();

        functions = new HashMap<>();
        functions.put("MAX", new FunctionMax());
        functions.put("MIN", new FunctionMin());
        functions.put("PROMEDIO", new FunctionPromedio());
        functions.put("SUMA", new FunctionSuma());
    }

    /**
     * Parses a Formula by Tokenizing, evaluating the Grammar and creating the
     * Post Fix Expression. Uses the help of the Parser.
     *
     * @param formula
     * @return a List of Terms that are contained in the Formula as Post Fix
     * expression
     */
    public List<Term> parseFormula(String formula) {
        formula = formula.replaceAll(" ", "");
        //formula = formula.replaceFirst("=", "");
        try {
            System.out.println("[INFO] .. Tokenizing formula : " + formula);
            tokenizer.tokenize(formula);

        } catch (ParserException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("[INFO] .. Evaluating Grammar");
        LinkedList<Tokenizer.Token> tokens = parser_formula.evaluateGrammar(tokenizer.getTokens());
        tokens.pop(); // To remove the initial equal
        for (Tokenizer.Token tok : tokens) {
            System.out.print(" [ " + tok.token + " ] ");
        }

        System.out.println("[INFO] .. Creating PostFix (Shaunting Yard Algorithm)");
        tokens = parser_formula.shuntingYard(tokens);
        for (Tokenizer.Token tok : tokens) {
            System.out.print(tok.sequence + "   ");
        }
        List<Term> terms = convertTokenToOTerm(tokens);
        for (Term tok : terms) {
            System.out.print(tok.print() + "   ");
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
        List<Term> terms = new LinkedList<Term>();
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

    /**
     * Evaluate the Post Fix Expression to compute the value depending on a List
     * of Terms passed as parameters.
     *
     * @param terms
     * @return A value as Double
     */
    public Double evaluatePostFix(List<Term> terms) {
        System.out.println();
        System.out.println("[INFO] .. Evaluating PostFix");
        Term first = null;
        Term second = null;

        LinkedList<Term> queue = new LinkedList<Term>();  // for values

        for (Term term : terms) {
            System.out.println("\t[evaluatePostFix] using term " + term.print());
            String type = term.isType();
            if ((type != "OperatorImpl") && (type != "OperatorFunction")) {  // Argument  | OperandFunction | OperandNumber | OperatorImpl | OperatorFunction
                System.out.println("\t\tAdding to queue as value");
                queue.add(term);
            } else if (type == "OperatorImpl") {
                System.out.println("\tThe term is an operator");
                second = queue.removeLast();
                first = queue.removeLast();
                queue.add(operate(first, second, (OperatorImpl) term));
            } else if (type == "OperatorFunction") {
                queue = operateFunction(queue, (OperatorFunction) term);
            }
            System.out.print("\t[INFO] Queue List is:   ");
            printList(queue);
        }
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

    /**
     * Check whether the Token is Operator or not. An Operator is considered
     * when token is type: +, -, *, /, ^
     *
     * @param token
     * @return True if Operator
     */
    private boolean isOperator(Tokenizer.Token token) {
        return ((token.token == TokenType.PLUS) || (token.token == TokenType.MINUS) || (token.token == TokenType.MULT) || (token.token == TokenType.DIVIDE) || (token.token == TokenType.RAISED));
    }

    /**
     * Check whether the Token is Value or Not. A Token is considered a value
     * when token is type: Formula, cell or real number
     *
     * @param token
     * @return True if Operator
     */
    private boolean isValue(Tokenizer.Token token) {
        return ((token.token == TokenType.FORMULA) || (token.token == TokenType.CELL) || (token.token == TokenType.REAL_NUMBER));
    }

    /**
     * Used for debugging. Prints the list of terms.
     *
     * @param terms
     */
    private void printList(LinkedList<Term> terms) {
        for (Term term : terms) {
            System.out.print(term.print() + "   ");
        }
        System.out.println();
    }

    /**
     * Used for Debugging with different type of functions
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        String easy_suma = "=SUMA(A2:A5)";
        String difficult = "=-1 + A1*((SUMA(A2:B5;PROMEDIO(B6:D8);C1;27)/4.5)+(D6-D8))";
        String difficult2 = "=SUMA(A2:B5;C1;27)";

        String error0 = "=*5+-4(/5)+";
        String easy_2 = "=3 + 4 * (2 - 1)"; // 3 4 2 1 − × +  || 7
        String easy_3 = "=10 + 3 * 5 / (16 - 4)"; // 10   3   5   *   16   4   -   /   +    || 11.25
        String easy_4 = "=(300+23)*(43-21)/(84+7)"; //  300 23 + 43 21 - * 84 7 + /    || 78.0789
        String easy_5 = "=(4+8)*(6-5)/((3-2)*(2+2))"; //  4 8 + 6 5 - * 3 2 – 2 2 + * /  || 3
        String easy_6 = "=SUMA(A2:A5) + 5 ";
        String easy_7 = "SUMA ( MAX ( 2;3 ) / 3 *2; 1 )"; // 2 3 MAX 3 / 2 * 1 SUMA

        //CellImpl cell = new CellImpl(0, 0, easy_2);
        FormulaEvaluator parser = new FormulaEvaluator();
        List<Term> terms = parser.parseFormula(difficult);
        parser.evaluatePostFix(terms);
    }

}
