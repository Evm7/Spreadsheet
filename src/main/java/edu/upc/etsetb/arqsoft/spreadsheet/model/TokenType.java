/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

/**
 *
 * @author estev
 */
public enum TokenType {
    FORMULA,
    OPEN_BRACKET,
    CLOSE_BRACKET,
    PLUS,
    MINUS,
    MULT,
    DIVIDE,
    RAISED,
    REAL_NUMBER,
    CELL,
    WHITE_SPACE,
    EQUAL,
    SEMICOLON,
    COLON;

    @Override
    public String toString() {
        switch (this.ordinal()) {
            case 0:
                return "FORMULA";
            case 1:
                return "(";
            case 2:
                return ")";
            case 3:
                return "+";
            case 4:
                return "-";
            case 5:
                return "*";
            case 6:
                return "/";
            case 7:
                return "^";
            case 8:
                return this.name();
            case 9:
                return "cell";
            case 10:
                return "SPACE";
            case 11:
                return "=";
            case 12:
                return ";";
            case 13:
                return ";";
            default:
                return "null";
        }
    }
}
