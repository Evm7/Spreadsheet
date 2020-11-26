/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import java.util.LinkedList;
import jdk.nashorn.internal.runtime.ParserException;

/**
 *
 * @author estev
 */
public class FormulaEvaluator {

    Tokenizer tokenizer;
    Parser parser;

    public FormulaEvaluator() {
        tokenizer = new Tokenizer();
        // Add the tokens to our series.
        tokenizer.add("SUMA|MIN|MAX|PROMEDIO|suma|min|max|promedio", TokenType.FORMULA, 0); // function
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

        parser = new Parser();
    }

    public void parseFormula(String formula) {
        try {
            System.out.println("[INFO] .. Tokenizing formula : " + formula);
            tokenizer.tokenize(formula);

            System.out.println("[INFO] .. Evaluating Grammar");
            LinkedList<Tokenizer.Token> tokens = parser.evaluateGrammar(tokenizer.getTokens());
            for (Tokenizer.Token tok : tokens) {
                System.out.println("" + tok.token + " " + tok.sequence + " precedence " + tok.precedence);
            }

            System.out.println("[INFO] .. Creating PostFix (Shaunting Yard Algorithm)");
            tokens = parser.shuntingYard(tokens);
            for (Tokenizer.Token tok : tokens) {
                System.out.print(tok.sequence + "   ");
            }
            System.out.println();
            System.out.println("[INFO] .. Evaluating PostFix");
            Double result = evaluatePostFix(tokens);
            System.out.println("[RESULT] .. Result is " + result);

        } catch (ParserException e) {
            System.out.println(e.getMessage());
        }
    }

    private Double evaluatePostFix(LinkedList<Tokenizer.Token> tokens) {
        Tokenizer.Token first = null;
        Tokenizer.Token second = null;

        LinkedList<Tokenizer.Token> queue = new LinkedList<Tokenizer.Token>();  // for values

        for (Tokenizer.Token tok : tokens) {
            System.out.println("\t[evaluatePostFix] using token " + tok.token);
            if (isValue(tok)) {
                System.out.println("\t\tAdding to queue as value");
                queue.add(tok);
            } else if (isOperator(tok)) {
                System.out.println("\tThe token is an operator");
                second = queue.removeLast();
                first = queue.removeLast();
                queue.add(tokenizer.createToken(TokenType.REAL_NUMBER, "" + operate(first, second, tok), 0));
            }
            System.out.print("\t[INFO] Queue List is:   ");
            printList(queue);
        }
        return Double.parseDouble(queue.getLast().sequence);
    }

    private void printList(LinkedList<Tokenizer.Token> tokens) {
        for (Tokenizer.Token tok : tokens) {
            System.out.print(tok.sequence + "   ");
        }
        System.out.println();
    }

    private Double operate(Tokenizer.Token first, Tokenizer.Token second, Tokenizer.Token operator) {
        Double first_value = getValue(first);
        Double second_value = getValue(second);
        if (operator.token == TokenType.MINUS) {
            return first_value - second_value;
        } else if (operator.token == TokenType.DIVIDE) {
            return first_value / second_value;
        } else if (operator.token == TokenType.PLUS) {
            return first_value + second_value;
        } else if (operator.token == TokenType.MULT) {
            return first_value * second_value;
        } else if (operator.token == TokenType.RAISED) {
            return Math.pow(first_value, second_value);
        } else {
            System.out.println("OPERATOR IS " + operator.token);
            return 0.0;
        }
    }

    private Double getValue(Tokenizer.Token tok_value) {
        Double value;
        if (tok_value.token == TokenType.REAL_NUMBER) {
            value = Double.parseDouble(tok_value.sequence);
        } else {
            value = 3.75;
        }
        return value;
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
        String error0 = "=*5+-4(/5)+";
        String easy_2 = "3 + 4 * (2 - 1)"; // 3 4 2 1 − × +
        String easy_3 = "10 + 3 * 5 / (16 - 4)";
        String easy_4 = "(300+23)*(43-21)/(84+7)"; //  300 23 + 43 21 - * 84 7 + /   
        String easy_5 = "(4+8)*(6-5)/((3-2)*(2+2))"; //  4 8 + 6 5 - * 3 2 – 2 2 + * /  
        String easy_6 = "";
        String easy_7 = "";

        FormulaEvaluator parser = new FormulaEvaluator();
        parser.parseFormula(easy_suma);
    }

}
