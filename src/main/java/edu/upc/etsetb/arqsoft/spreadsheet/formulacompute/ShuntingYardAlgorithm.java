/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascar�
 */
package edu.upc.etsetb.arqsoft.spreadsheet.formulacompute;

import java.util.LinkedList;

/**
 *
 * The @author of SpreadSheet is estev
 */
public class ShuntingYardAlgorithm {

    boolean debug = false;

    public ShuntingYardAlgorithm() {
    }
    

    private void print(boolean visualize, String s) {
        if (visualize) {
            System.out.println(s);
        }
    }
  /**
     * Computes the Shunting Yard algorithm for a list of tokens.
     *
     * @param tokens tokenized directly from the formula inputed by the user
     * @return list of tokens sorted depending on Shunting Yard Algorithm
     */
    public LinkedList<Tokenizer.Token> shuntingYard(LinkedList<Tokenizer.Token> tokens) {
        LinkedList<Tokenizer.Token> queue = new LinkedList<>();  // for values
        LinkedList<Tokenizer.Token> stack = new LinkedList<>();  // for operators

        Tokenizer.Token topStack;

        print(debug, "_______________________ SHUNTING YARD_______________________");

        for (Tokenizer.Token token : tokens) {
            print(debug, "\t[shuntingYard] using token " + token.token);
            if (toQueue(token)) {
                print(debug, "\t\tAdding to queue as value");
                queue.add(token);
            } else if (token.token == TokenType.FORMULA) {
                print(debug, "\t\tAdding to stack as formula");

                stack.add(token);
            } else if (toStack(token)) {
                print(debug, "\t\tOpperant to stack found " + token.token);
                if (stack.isEmpty()) {
                    print(debug, "\t\t\tAdding to stack as stack is empty");

                    stack.add(token);
                } else {
                    topStack = stack.getLast();
                    while ((topStack != null) && isOperator(topStack) && ((topStack.precedence > token.precedence) || ((topStack.precedence == token.precedence) && isOperator(token))) && (topStack.token != TokenType.OPEN_BRACKET)) {
                        stack.remove(topStack);
                        queue.add(topStack);
                        print(debug, "\t\t\tAdding the topstack " + topStack.token + " to queue as procedence is " + topStack.precedence);
                        if (stack.isEmpty()) {
                            break;
                        }
                        topStack = stack.getLast();
                    }
                    stack.add(token);
                    print(debug, "\t\t\tAdding token to stack after shuffer");

                }

            } else if (token.token == TokenType.OPEN_BRACKET) {
                print(debug, "\t\tAdding ( to stack as (");

                stack.add(token);
            } else if (token.token == TokenType.CLOSE_BRACKET) {
                print(debug, "\t\tClose Bracket found, search for )");
                topStack = stack.getLast();
                while ((topStack.token != TokenType.OPEN_BRACKET)) {
                    print(debug, "\t\t\t Top Stack " + topStack.token + " is not (, removed from stack and passed to queue");
                    stack.remove(topStack);
                    queue.add(topStack);
                    topStack = stack.getLast();
                }
                if (topStack.token == TokenType.OPEN_BRACKET) {
                    print(debug, "\t\t\tRemoving Open Bracket from Stack");
                    stack.remove(topStack);
                }
                if (stack.isEmpty()) {
                    continue;
                }
                topStack = stack.getLast();
                if (topStack.token == TokenType.FORMULA) {
                    stack.remove(topStack);
                    queue.add(topStack);
                    print(debug, "\t\t\tFORMULA FOUND: Adding the topstack " + topStack.token + " to queue as procedence is " + topStack.precedence);
                }
                if (stack.isEmpty()) {
                    continue;
                }
            }
            print(debug, "\t[INFO] Stack List is:   ");
            printList(debug, stack);
            print(debug, "\t[INFO] Queue List is:   ");
            printList(debug, queue);

        }
        for (int i = stack.size() - 1; i >= 0; i--) {
            topStack = stack.remove(i);
            queue.add(topStack);
        }

        return queue;
    }
    
        /**
     * Check wheteher a token should be added to Stack or not. To Stack we add
     * tokens of type: Operator, Semicolon or Colon
     *
     * @param token
     * @return boolean if it can be placed to Stack
     */
    private boolean toStack(Tokenizer.Token token) {
        TokenType type = token.token;
        return (isOperator(token) || type == TokenType.SEMICOLON || type == TokenType.COLON);
    }
    
        /**
     * Used for debugging. Print lIst of tokens.
     *
     * @param tokens
     */
    private void printList(boolean visited, LinkedList<Tokenizer.Token> tokens) {
        if (visited) {
            for (Tokenizer.Token tok : tokens) {
                System.out.print(tok.sequence + "   ");
            }
            System.out.println();
        }
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
     * Check wheteher a token should be added to Queue or not. To Queue we add
     * tokens of type: Real Number, Cell or Range
     *
     * @param token
     * @return boolean if it can be placed to Queue
     */
    private boolean toQueue(Tokenizer.Token token) {
        TokenType type = token.token;
        return (type == TokenType.REAL_NUMBER || type == TokenType.CELL || type == TokenType.RANGE);
    }
}
