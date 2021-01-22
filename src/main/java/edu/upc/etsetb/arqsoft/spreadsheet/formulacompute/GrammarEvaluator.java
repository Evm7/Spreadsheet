/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascar�
 */
package edu.upc.etsetb.arqsoft.spreadsheet.formulacompute;

import edu.upc.etsetb.arqsoft.spreadsheet.exceptions.GrammarErrorFormula;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * The @author of SpreadSheet is estev
 */
public class GrammarEvaluator {

    boolean debug = false;

    public GrammarEvaluator() {
    }
    

    private void print(boolean visualize, String s) {
        if (visualize) {
            System.out.println(s);
        }
    }

  

    /**
     * Evaluates the grammar of the list of tokens.Userd to check if the user
     * has inputted a formula which does not have a Proper Mathematical format.
     *
     * i. Check that number of OPEN BRACKETS equals the number of CLOSED
     * BRACKETS. ii. Check if the brackets are closed before being opened . iii.
     * If there is a MINUS or PLUS token before a REAL VALUE and after OPEN
     * BRACKETS or starting the formula, unify both tokens to REAL VALUE with
     * the corresponding sign. iv. Check that COLON can only be used between
     * CELL token. v. Check that operators can only be used between CELL , REAL
     * NUMBER, brackets or FORMULA. vi. Check if formula is followed by OPEN
     * BRACKET. vii. Before closing a bracket only a CELL, REAL NUMBER or CLOSE
     * BRACKET accepted. viii. Before opening a bracket there can not be a CELL
     * or REAL NUMBER.
     *
     * @param tokens List of tokens
     * @return List of tokens
     * @throws edu.upc.etsetb.arqsoft.spreadsheet.exceptions.GrammarErrorFormula Exception raise when formula does contain a not correct parameter
     */
    public LinkedList<Tokenizer.Token> evaluateGrammar(LinkedList<Tokenizer.Token> tokens) throws GrammarErrorFormula {
        Tokenizer.Token prev = null;
        Tokenizer.Token curr = null;
        Tokenizer.Token next = null;

        List<Integer> indexes = new ArrayList<>();
        int number_brackets = 0;
        for (int i = 0; i < tokens.size() - 1; i++) {
            prev = i == 0 ? null : tokens.get(i - 1);
            curr = tokens.get(i);
            next = tokens.get(i + 1);

            //System.out.println("Current is " + curr.token);
            if (curr.token == TokenType.OPEN_BRACKET) {
                number_brackets += 1;
            } else if (curr.token == TokenType.CLOSE_BRACKET) {
                number_brackets -= 1;
                if (number_brackets < 0) {
                    throw new GrammarErrorFormula("Error! Can not close brackets before opening");
                }
            }

            // CHECKING FINISHED GRAMMAR
            if (i + 1 == tokens.size() - 1) {
                if (next.token == TokenType.CLOSE_BRACKET) {
                    number_brackets -= 1;
                }
                if (number_brackets != 0) {
                    throw new GrammarErrorFormula("Error! Different number of opens - close brackets");

                } else if (!isValue(next, true)) {
                    throw new GrammarErrorFormula("Error! Formula can not end up with operator " + next.sequence);
                }
            }
            if (prev == null) {
                continue;
            } else if (curr.token == TokenType.EQUAL) {
                indexes.add(i);
            } // CHEKING STARTER GRAMMAR --> ONLY VALUES, OPEN BRACKETS OR +- REAL_NUMBER ALLOWED
            else if (isStarter(prev)) {
                if (!isValue(curr, false)) {
                    // USED WHEN WE WANT TO INPUT VALUE : -1 --> THEN VALUE IS ACTUALLY "-1", NO SUBTRACTION IS NEEDED
                    if (curr.token == TokenType.MINUS && next.token == TokenType.REAL_NUMBER) {
                        next.modifySequence("" + (-1 * Double.parseDouble(next.sequence)));
                        // System.out.println("new token at index " + i);
                        indexes.add(i);
                    } // USED WHEN WE WANT TO INPUT VALUE : +1 --> THEN VALUE IS ACTUALLY "+1", NO SUM IS NEEDED
                    else if (curr.token == TokenType.PLUS && next.token == TokenType.REAL_NUMBER) {
                        indexes.add(i);
                    } else {
                        throw new GrammarErrorFormula("ERROR! Can not place " + curr.sequence + " at starting of a function");

                    }
                }

            } // COLON ONLY USED BETWEEN CELLS
            else if (curr.token == TokenType.COLON) {
                if ((prev.token != TokenType.CELL) || (next.token != TokenType.CELL)) {
                    throw new GrammarErrorFormula("ERROR! Colon can only be located between cell values");
                } else {
                    indexes.add(i - 1);
                    indexes.add(i);
                    next.update(TokenType.RANGE, prev.sequence + curr.sequence + next.sequence, 5);

                }
            } // OPERATORS ONLY BETWEEN VALUES
            else if (isOperator(curr) && !(isValue(prev, true) && isValue(next, false))) {
                throw new GrammarErrorFormula("ERROR! Operator " + curr.sequence + " should be placed between values");
            } // FORMULA MUST BE FOLLOWED 
            else if (curr.token == TokenType.FORMULA && (next.token != TokenType.OPEN_BRACKET)) {
                throw new GrammarErrorFormula("ERROR! Formula " + curr.sequence + " needs to be followed by brackets, not " + next.sequence);
            } // BEFORE CLOSING BRACKET, ONLY VALUE ACCEPTED
            else if (next.token == TokenType.CLOSE_BRACKET && !isValue(curr, true)) {
                throw new GrammarErrorFormula("Error! There can not be a " + next.sequence + " just before closing a bracket");
            } else if (next.token == TokenType.OPEN_BRACKET && isValue(curr, true)) {
                throw new GrammarErrorFormula("Error! Before Opening a bracket there can not be a " + curr.sequence);

            }
        }
        for (int i = 0; i < indexes.size(); i++) {
            tokens.remove(indexes.get(i) - i);
        }
        return tokens;
    }

        /**
     * Check wheteher a token is an Operator Operators are tokens of type: +, -,
     * *, /, ^
     *
     * @param token
     * @return boolean if it is an Starter
     */
    private boolean isOperator(Tokenizer.Token token) {
        return ((token.token == TokenType.PLUS) || (token.token == TokenType.MINUS) || (token.token == TokenType.MULT) || (token.token == TokenType.DIVIDE) || (token.token == TokenType.RAISED));
    }
    
    
    /**
     * Check wheteher a token can be the a Value. Values are tokens of type:
     * Formula, Cell, Real Number, Range, ), (
     *
     * @param token
     * @param before Sometimes it is considered value if
     * @return boolean if checking NEXT Token after the Current (False) or
     * Previous One (True)
     */
    private boolean isValue(Tokenizer.Token token, boolean before) {
        return ((token.token == TokenType.FORMULA && !before) || (token.token == TokenType.CELL) || (token.token == TokenType.REAL_NUMBER) || (token.token == TokenType.RANGE) || (token.token == TokenType.OPEN_BRACKET && !before) || (token.token == TokenType.CLOSE_BRACKET && before));
    }
    
        /**
     * Check wheteher a token can be the first character of a formula. Starters
     * are tokens of type: Null, OpenBracket or Equal
     *
     * @param token
     * @return boolean if it is an Starter
     */
    private boolean isStarter(Tokenizer.Token token) {
        return ((token == null || token.token == TokenType.OPEN_BRACKET || token.token == TokenType.EQUAL));
    }
}
