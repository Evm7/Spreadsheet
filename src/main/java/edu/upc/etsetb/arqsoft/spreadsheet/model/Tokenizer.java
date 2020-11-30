/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jdk.nashorn.internal.runtime.ParserException;

/**
 *
 * @author estev
 */
public class Tokenizer {

    private LinkedList<TokenInfo> tokenInfos;
    private LinkedList<Token> tokens;

    public Tokenizer() {
        tokenInfos = new LinkedList<TokenInfo>();
        tokens = new LinkedList<Token>();

    }

    public void add(String regex, TokenType token, int precedence) {
        tokenInfos.add(new TokenInfo(Pattern.compile("^(" + regex + ")"), token, precedence));
    }

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

    public Token createToken(TokenType token, String sequence, int precedence) {
        return new Token(token, sequence, precedence);
    }

    public LinkedList<Token> getTokens() {
        return tokens;

    }

    public class Token {

        public TokenType token;
        public String sequence;
        public int precedence;

        public Token(TokenType token, String sequence, int precedence) {
            super();
            this.token = token;
            this.sequence = sequence;
            this.precedence = precedence;
        }

        public void modifySequence(String sequence) {
            this.sequence = sequence;
        }

        public void update(TokenType token, String sequence, int precedence) {
            this.token = token;
            this.sequence = sequence;
            this.precedence = precedence;
        }
    }

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
