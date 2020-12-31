/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.formulacompute;

import edu.upc.etsetb.arqsoft.spreadsheet.exceptions.CircularDependencies;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.Term;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.Function;
import edu.upc.etsetb.arqsoft.spreadsheet.exceptions.GrammarErrorFormula;
import edu.upc.etsetb.arqsoft.spreadsheet.model.Cell;
import edu.upc.etsetb.arqsoft.spreadsheet.model.CellCoordinate;
import edu.upc.etsetb.arqsoft.spreadsheet.model.ContentFormula;
import edu.upc.etsetb.arqsoft.spreadsheet.model.SpreadSheet;
import edu.upc.etsetb.arqsoft.spreadsheet.model.TypeOfContent;

import java.util.ArrayList;
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

    public void setDebugger(boolean mode) {
        debug = mode;
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
        LinkedList<Tokenizer.Token> tokens = evaluateGrammar(tokenizer.getTokens());
        tokens.pop(); // To remove the initial equal
        for (Tokenizer.Token tok : tokens) {
            print(debug, " [ " + tok.token + " ] ");
        }

        print(debug, "[INFO] .. Creating PostFix (Shaunting Yard Algorithm)");
        tokens = shuntingYard(tokens);
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
     * @param formula List of terms
     * @return A value as Double
     */
    public Double evaluatePostFix(List<Term> formula) {
        print(debug, "_______________________ EVALUATE POSTFIX");
        print(debug, "[INFO] .. Evaluating PostFix"); // =A1+B1*C1-PROMEDIO(A1:C1) //A	B	C	D	E	 1| 	5.0	3.0	3.65	
        VisitorFormula visitor = new VisitorFormula(debug);
        for (Term term : formula) {
            print(debug, "\t[evaluatePostFix] using term " + term.toString());
            term.acceptVisitor(visitor);
        }
        return visitor.getResult();
    }

    /**
     * Uses recursivity to compute all dependencies and its tree
     * @param cellcoordinate Cell coordinates
     * @throws edu.upc.etsetb.arqsoft.spreadsheet.exceptions.CircularDependencies Exception raised when exists two formula A and B, and
     * A depends on B and B depends on A.
     */
    public void circularDependencies(CellCoordinate cellcoordinate) throws CircularDependencies {
        HashMap<String, ArrayList> map = new HashMap<String, ArrayList>();
        //System.out.println("___________CIRCULAR DEPENDENCIES___________");
        map.put(cellcoordinate.toString(), null);
        map.put(cellcoordinate.toString(), circularDependency(cellcoordinate, map));
        //System.out.println(map);
        //System.out.println("___________TREE SEARCH___________");
        TreeSearch search = new TreeSearch(map, cellcoordinate.toString());
    }

    /**
     * Check the circular Dependencies of one formula
     *
     * @param formula
     */
    private ArrayList<String> circularDependency(CellCoordinate cellcoordinate, HashMap<String, ArrayList> map) {
        ArrayList<String> args = new ArrayList<>();
        //System.out.println("MAP IS:");
        //System.out.println(map);
        // Get the Cell from the CellCoordinate
        int column = cellcoordinate.getColumn();
        int row = cellcoordinate.getRow();
        Cell cell = (SpreadSheet.spreadsheet[row - 1][column]);
        //System.out.println("Cell we are checking is " + cell.toString());

        // There are only dependencies if Cell is Formula
        if (cell.getType_of_content() == TypeOfContent.FORMULA) {
            ContentFormula content = (ContentFormula) cell.getContent();
            //System.out.println(content.getArguments());

            // Search for Dependencies of the coordinate
            for (Argument arg : content.getArguments()) {
                if (arg.isType().equals("Argument")) {
                    if (arg instanceof ArgumentRange) {
                        arg = (ArgumentRange) arg;
                        //System.out.println("\tArgument Range");
                        //System.out.println(arg.getReferences());
                    } else {
                        arg = (ArgumentIndividual) arg;
                        //System.out.println("\tArgument Individual");
                    }

                    for (CellCoordinate coord : arg.getReferences()) {
                        //System.out.println("Depends on " + coord.toString());
                        args.add(coord.toString());
                        if (!map.containsKey(coord.toString())) {

                            ArrayList<String> array = circularDependency(coord, map);
                            if (array != null) {
                                map.put(coord.toString(), array);

                            }
                        }
                    }
                }
            }
            return args;
        }
        return null;
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
        try {
            List<Term> terms = parser.parseFormula(difficult);
        } catch (GrammarErrorFormula ex) {
            System.out.print(ex.getMessage());
        }
    }

}
