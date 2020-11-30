/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 *
 * @author estev
 */
public class FormulaEvaluator {

    Tokenizer tokenizer;
    Parser parser_formula;
    public static HashMap<String, Function> functions;

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

        return Double.parseDouble(queue.getLast().print());

    }

    private void printList(LinkedList<Term> terms) {
        for (Term term : terms) {
            System.out.print(term.print() + "   ");
        }
        System.out.println();
    }

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

    private boolean isOperator(Tokenizer.Token token) {
        return ((token.token == TokenType.PLUS) || (token.token == TokenType.MINUS) || (token.token == TokenType.MULT) || (token.token == TokenType.DIVIDE) || (token.token == TokenType.RAISED));
    }

    private boolean isValue(Tokenizer.Token token) {
        return ((token.token == TokenType.FORMULA) || (token.token == TokenType.CELL) || (token.token == TokenType.REAL_NUMBER));
    }

    /**
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
