/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.formulacompute;

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
        LinkedList<Tokenizer.Token> tokens = evaluateGrammar(tokenizer.getTokens());
        tokens.pop(); // To remove the initial equal
        for (Tokenizer.Token tok : tokens) {
            System.out.print(" [ " + tok.token + " ] ");
        }

        System.out.println("[INFO] .. Creating PostFix (Shaunting Yard Algorithm)");
        tokens = shuntingYard(tokens);
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
     * Computes the Shunting Yard algorithm for a list of tokens.
     *
     * @param tokens tokenized directly from the formula inputed by the user
     * @return list of tokens sorted depending on Shunting Yard Algorithm
     */
    public LinkedList<Tokenizer.Token> shuntingYard(LinkedList<Tokenizer.Token> tokens) {
        LinkedList<Tokenizer.Token> queue = new LinkedList<Tokenizer.Token>();  // for values
        LinkedList<Tokenizer.Token> stack = new LinkedList<Tokenizer.Token>();  // for operators

        Tokenizer.Token topStack;

        for (Tokenizer.Token token : tokens) {
            System.out.println("\t[shuntingYard] using token " + token.token);
            if (toQueue(token)) {
                System.out.println("\t\tAdding to queue as value");
                queue.add(token);
            } else if (token.token == TokenType.FORMULA) {
                System.out.println("\t\tAdding to stack as formula");

                stack.add(token);
            } else if (toStack(token)) {
                System.out.println("\t\tOpperant to stack found " + token.token);
                if (stack.isEmpty()) {
                    System.out.println("\t\t\tAdding to stack as stack is empty");

                    stack.add(token);
                } else {
                    topStack = stack.getLast();
                    while ((topStack != null) && isOperator(topStack) && ((topStack.precedence > token.precedence) || ((topStack.precedence == token.precedence) && isOperator(token))) && (topStack.token != TokenType.OPEN_BRACKET)) {
                        stack.remove(topStack);
                        queue.add(topStack);
                        System.out.println("\t\t\tAdding the topstack " + topStack.token + " to queue as procedence is " + topStack.precedence);
                        if (stack.isEmpty()) {
                            break;
                        }
                        topStack = stack.getLast();
                    }
                    stack.add(token);
                    System.out.println("\t\t\tAdding token to stack after shuffer");

                }

            } else if (token.token == TokenType.OPEN_BRACKET) {
                System.out.println("\t\tAdding ( to stack as (");

                stack.add(token);
            } else if (token.token == TokenType.CLOSE_BRACKET) {
                System.out.println("\t\tClose Bracket found, search for )");
                topStack = stack.getLast();
                while ((topStack.token != TokenType.OPEN_BRACKET)) {
                    System.out.println("\t\t\t Top Stack " + topStack.token + " is not (, removed from stack and passed to queue");
                    stack.remove(topStack);
                    queue.add(topStack);
                    topStack = stack.getLast();
                }
                if (topStack.token == TokenType.OPEN_BRACKET) {
                    System.out.println("\t\t\tRemoving Open Bracket from Stack");
                    stack.remove(topStack);
                }
                topStack = stack.getLast();
                if (topStack.token == TokenType.FORMULA) {
                    stack.remove(topStack);
                    queue.add(topStack);
                    System.out.println("\t\t\tFORMULA FOUND: Adding the topstack " + topStack.token + " to queue as procedence is " + topStack.precedence);
                }
                if (stack.isEmpty()) {
                    break;
                }
            }
            System.out.print("\t[INFO] Stack List is:   ");
            printList(stack);
            System.out.print("\t[INFO] Queue List is:   ");
            printList(queue);

        }
        for (int i = stack.size() - 1; i >= 0; i--) {
            topStack = stack.remove(i);
            queue.add(topStack);
        }

        return queue;
    }

    /**
     * Evaluates the grammar of the list of tokens.
     *
     * Userd to check if the user has inputted a formula which does not have a
     * Proper Mathematical format. i. Check that number of OPEN BRACKETS equals
     * the number of CLOSED BRACKETS.
     *
     * ii. Check if the brackets are closed before being opened .
     *
     * iii. If there is a MINUS or PLUS token before a REAL VALUE and after OPEN
     * BRACKETS or starting the formula, unify both tokens to REAL VALUE with
     * the corresponding sign.
     *
     * iv. Check that COLON can only be used between CELL token.
     *
     * v. Check that operators can only be used between CELL , REAL NUMBER,
     * brackets or FORMULA.
     *
     * vi. Check if formula is followed by OPEN BRACKET.
     *
     * vii. Before closing a bracket only a CELL, REAL NUMBER or CLOSE BRACKET
     * accepted.
     *
     * viii. Before opening a bracket there can not be a CELL or REAL NUMBER.
     *
     * @param tokens
     * @return
     */
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

            //System.out.println("Current is " + curr.token);
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
                if (next.token == TokenType.CLOSE_BRACKET) {
                    number_brackets -= 1;
                }
                if (number_brackets != 0) {
                    System.out.println("Error! Different number of opens - close brackets");
                } else if (!isValue(next, true)) {
                    System.out.println("Error! Function can not end up with operator " + next.sequence);
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
            else if (curr.token == TokenType.COLON) {
                if ((prev.token != TokenType.CELL) || (next.token != TokenType.CELL)) {
                    System.out.println("ERROR! Colon can only be located between cell values");
                } else {
                    indexes.add(i - 1);
                    indexes.add(i);
                    next.update(TokenType.RANGE, prev.sequence + curr.sequence + next.sequence, 5);

                }
            } // OPERATORS ONLY BETWEEN VALUES
            else if (isOperator(curr) && !(isValue(prev, true) && isValue(next, false))) {
                System.out.println("ERROR! Operator " + curr.sequence + " should be placed between values");
            } // FORMULA MUST BE FOLLOWED 
            else if (curr.token == TokenType.FORMULA && (next.token != TokenType.OPEN_BRACKET)) {
                System.out.println("ERROR! Formula " + curr.sequence + " needs to be followed by brackets, not " + next.sequence);
            } // BEFORE CLOSING BRACKET, ONLY VALUE ACCEPTED
            else if (next.token == TokenType.CLOSE_BRACKET && !isValue(curr, true)) {
                System.out.println("Error! There can not be a " + next.sequence + " just before closing a bracket");
            } else if (next.token == TokenType.OPEN_BRACKET && isValue(curr, true)) {
                System.out.println("Error! Before Opening a bracket there can not be a " + curr.sequence);

            }
        }
        for (int i = 0; i < indexes.size(); i++) {
            tokens.remove(indexes.get(i) - i);
        }
        return tokens;
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

    /**
     * Evaluate the Post Fix Expression to compute the value depending on a List
     * of Terms passed as parameters.
     *
     * @param terms
     * @return A value as Double
     */
    public Double evaluatePostFix(List<Term> formula) {
        System.out.println();
        System.out.println("[INFO] .. Evaluating PostFix"); // =A1+B1*C1-PROMEDIO(A1:C1) //A	B	C	D	E	 1| 	5.0	3.0	3.65	
        VisitorFormula visitor = new VisitorFormula();
        for (Term term : formula) {
            System.out.println("\t[evaluatePostFix] using term " + term.print());
            String type = term.isType();
            term.acceptVisitor(visitor);
        }
        return visitor.getResult();
    }

    /**
     * Used for debugging. Print lIst of tokens.
     *
     * @param tokens
     */
    private void printList(LinkedList<Tokenizer.Token> tokens) {
        for (Tokenizer.Token tok : tokens) {
            System.out.print(tok.sequence + "   ");
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
