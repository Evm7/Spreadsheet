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
        tokenizer.add("SUMA|MIN|MAX|PROMEDIO|suma|min|max|promedio", TokenType.FORMULA); // function
        tokenizer.add("\\(", TokenType.OPEN_BRACKET); // open bracket
        tokenizer.add("\\)", TokenType.CLOSE_BRACKET); // close bracket
        tokenizer.add("\\+", TokenType.PLUS); // plus
        tokenizer.add("\\-", TokenType.MINUS); // minus
        tokenizer.add("\\*", TokenType.MULT); // mult
        tokenizer.add("\\/", TokenType.DIVIDE); // divide
        tokenizer.add("\\^", TokenType.RAISED); // raised
        //tokenizer.add("[0-9]+", REAL_NUMBER); // integer number
        tokenizer.add("[+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)", TokenType.REAL_NUMBER); // Real number
        tokenizer.add("[a-zA-Z][a-zA-Z0-9_]*", TokenType.CELL); // cell
        tokenizer.add("\\s", TokenType.WHITE_SPACE); // white space
        tokenizer.add("\\=", TokenType.EQUAL); // equal
        tokenizer.add("\\;", TokenType.SEMICOLON); // semicolon
        tokenizer.add("\\:", TokenType.COLON); // colon
         
        parser = new Parser();
    }

    public void parseFormula(String formula) {
        try {
            tokenizer.tokenize(formula);
            LinkedList<Tokenizer.Token> tokens = parser.evaluateGrammar(tokenizer.getTokens());

            for (Tokenizer.Token tok : tokens) {
                System.out.println("" + tok.token + " " + tok.sequence);
            }
        } catch (ParserException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        String easy_suma = "=SUMA(A2:A5)";
        String difficult = "=-1 + A1*((SUMA(A2:B5;PROMEDIO(B6:D8);C1;27)/4.5)+(D6-D8))";
        String error0 = "=*5+-4(/5)+";
        System.out.println("We are parsing : "+ error0);
        FormulaEvaluator parser = new FormulaEvaluator();
        parser.parseFormula(error0);
    }

}
