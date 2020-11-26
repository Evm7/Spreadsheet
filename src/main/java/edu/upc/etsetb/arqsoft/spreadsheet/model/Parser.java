/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author estev
 */
public class Parser {

    public Parser() {
    }

    public LinkedList<Tokenizer.Token> evaluateGrammar(LinkedList<Tokenizer.Token> tokens) {
        Tokenizer.Token prev = null;
        Tokenizer.Token curr = null;
        Tokenizer.Token next = null;

        List<Integer> indexes = new ArrayList<>();
        int number_brackets = 0;
        for (int i = 0; i < tokens.size() - 1; i++) {
            prev = i == 0 ? null : tokens.get(i - 1);
            curr = tokens.get(i);
            next = tokens.get(i + 1);

            System.out.println("Current is " + curr.token);

            if (curr.token == TokenType.OPEN_BRACKET) {
                number_brackets += 1;
            } else if (curr.token == TokenType.CLOSE_BRACKET) {
                number_brackets -= 1;
                if (number_brackets < 0) {
                    System.out.println("Error! Can not close brackets before opening");
                }
            }

            // CHECKING FINISHED GRAMMAR
            if (i + 1 == tokens.size() - 1) {
                if (number_brackets != 0) {
                    System.out.println("Error! Different number of opens - close brackets");
                } else if (!isValue(next, true)) {
                    System.out.println("Error! Function can not end up with operator " + next.sequence);
                }
            }
            if (prev == null) {
                continue;
            }// CHEKING STARTER GRAMMAR --> ONLY VALUES, OPEN BRACKETS OR +- REAL_NUMBER ALLOWED
            else if (isStarter(prev)) {
                if (!isValue(curr, false)) {
                    // USED WHEN WE WANT TO INPUT VALUE : -1 --> THEN VALUE IS ACTUALLY "-1", NO SUBTRACTION IS NEEDED
                    if (curr.token == TokenType.MINUS && next.token == TokenType.REAL_NUMBER) {
                        next.modifySequence("" + (-1 * Double.parseDouble(next.sequence)));
                        System.out.println("new token at index " + i);
                        indexes.add(i);
                    } // USED WHEN WE WANT TO INPUT VALUE : +1 --> THEN VALUE IS ACTUALLY "+1", NO SUM IS NEEDED
                    else if (curr.token == TokenType.PLUS && next.token == TokenType.REAL_NUMBER) {
                        indexes.add(i);
                    } else {
                        System.out.println("ERROR! Can not place " + curr.sequence + " at starting of a function");

                    }
                }

            } // COLON ONLY USED BETWEEN CELLS
            else if (((prev.token != TokenType.CELL) || (next.token != TokenType.CELL)) && (curr.token == TokenType.COLON)) {
                System.out.println("ERROR! Colon can only be located between cell values");
            } // OPERATORS ONLY BETWEEN VALUES
            else if (isOperator(curr) && !(isValue(prev, true) && isValue(next, false))) {
                System.out.println("ERROR! Operator " + curr.sequence + " should be placed between values");
            } // FORMULA MUST BE FOLLOWED 
            else if (curr.token == TokenType.FORMULA && (next.token != TokenType.OPEN_BRACKET)) {
                System.out.println("ERROR! Formula " + curr.sequence + " needs to be followd by brackets, not " + next.sequence);
            } // BEFORE CLOSING BRACKET, ONLY VALUE ACCEPTED
            else if (next.token == TokenType.CLOSE_BRACKET && !isValue(curr, true)) {
                System.out.println("Error! There can not be a " + next.sequence + " just before closing a bracket");
            } else if (next.token == TokenType.OPEN_BRACKET && isValue(curr, true)) {
                System.out.println("Error! Before Opening a bracket there can not be a " + curr.sequence);

            }
        }
        for (int i = 0;
                i < indexes.size();
                i++) {
            tokens.remove(indexes.get(i) - i);
        }
        return tokens;
    }

    private boolean isStarter(Tokenizer.Token token) {
        return ((token == null || token.token == TokenType.OPEN_BRACKET || token.token == TokenType.EQUAL));
    }

    private boolean isOperator(Tokenizer.Token token) {
        return ((token.token == TokenType.PLUS) || (token.token == TokenType.MINUS) || (token.token == TokenType.MULT) || (token.token == TokenType.DIVIDE) || (token.token == TokenType.RAISED));
    }

    private boolean isValue(Tokenizer.Token token, boolean before) {
        return ((token.token == TokenType.FORMULA && !before) || (token.token == TokenType.CELL) || (token.token == TokenType.REAL_NUMBER) || (token.token == TokenType.OPEN_BRACKET && !before) || (token.token == TokenType.CLOSE_BRACKET && before));
    }
}
