/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.formulacompute;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jdk.nashorn.internal.runtime.ParserException;

/**
 * Class used to tokenize a formula depending on the TokenTypes.
 *
 * @author estev
 */
public class Tokenizer {

    private LinkedList<TokenInfo> tokenInfos;
    private LinkedList<Token> tokens;

    /**
     * Initialize the Lists that will contain the information of the token and
     * the tokens itself.
     */
    public Tokenizer() {
        tokenInfos = new LinkedList<TokenInfo>();
        tokens = new LinkedList<Token>();

    }

    /**
     * Used to add a new Token parsed into tokenInfos
     *
     * @param regex Part of the Formula Content that is used to create the Token
     * @param token Type of the Token: TokenType Class
     * @param precedence integer which indicates the weight or precedence to
     * then compute the Shaunting Yard ALgorithm
     */
    public void add(String regex, TokenType token, int precedence) {
        tokenInfos.add(new TokenInfo(Pattern.compile("^(" + regex + ")"), token, precedence));
    }

    /**
     * Tokenizes an string through matching regex
     *
     * @param str
     */
    public void tokenize(String str) {
        String s = new String(str);
        s = s.replaceAll(" ", "");
        tokens.clear();
        while (!s.equals("")) {
            boolean match = false;
            for (TokenInfo info : tokenInfos) {
                Matcher m = info.regex.matcher(s);
                if (m.find()) {
                    match = true;

                    String tok = m.group().trim();
                    tokens.add(new Token(info.token, tok, info.precedence));

                    s = m.replaceFirst("");
                    break;

                }
            }
            if (!match) {
                throw new ParserException("Unexpected character in input: " + s);
            }
        }
    }

    /**
     * Used to Create a Token from its type, sequence and precedence.
     *
     * @param token
     * @param sequence
     * @param precedence
     * @return
     */
    public Token createToken(TokenType token, String sequence, int precedence) {
        return new Token(token, sequence, precedence);
    }

    /**
     * Get the List of tokens
     *
     * @return
     */
    public LinkedList<Token> getTokens() {
        return tokens;

    }

    /**
     * Internal Class of Token, contains the type of the token, the String
     * sequence referenced by the token and its precedence.
     */
    public class Token {

        /**
         * Type of the Token
         */
        public TokenType token;

        /**
         * Reference of the String which creates the Token
         */
        public String sequence;

        /**
         * Weight of the Token
         */
        public int precedence;

        /**
         * Constructor of the Token
         *
         * @param token
         * @param sequence
         * @param precedence
         */
        public Token(TokenType token, String sequence, int precedence) {
            super();
            this.token = token;
            this.sequence = sequence;
            this.precedence = precedence;
        }

        /**
         * Modifies the Sequence of the Token.
         *
         * @param sequence
         */
        public void modifySequence(String sequence) {
            this.sequence = sequence;
        }

        /**
         * Updates the whole token
         *
         * @param token
         * @param sequence
         * @param precedence
         */
        public void update(TokenType token, String sequence, int precedence) {
            this.token = token;
            this.sequence = sequence;
            this.precedence = precedence;
        }
    }

    /**
     * Contains the information of the general tokens. Pattern that needs to
     * follow a sequence to be considered a TokenType, and precedence assign to
     * that TokenType.
     */
    private class TokenInfo {

        public final Pattern regex;
        public final TokenType token;
        public final int precedence;

        public TokenInfo(Pattern regex, TokenType token, int precedence) {
            super();
            this.regex = regex;
            this.token = token;
            this.precedence = precedence;
        }
    }

}
