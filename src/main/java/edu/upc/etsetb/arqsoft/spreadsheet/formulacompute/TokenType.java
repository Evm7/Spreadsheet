/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.formulacompute;

/**
 * Class that contains all the possible Types of Tokens
 * @author estev
 */
public enum TokenType {

    /**
     * When found: SUMA, MAX, MIN, PROMEDIO
     */
    FORMULA,

    /**
     * When found: (
     */
    OPEN_BRACKET,

    /**
     * When found: )
     */
    CLOSE_BRACKET,

    /**
     * When found:  +
     */
    PLUS,

    /**
     * When found: -
     */
    MINUS,

    /**
     * When found: *
     */
    MULT,

    /**
     * When found: /
     */
    DIVIDE,

    /**
     * When found: ^
     */
    RAISED,

    /**
     * When found any number
     */
    REAL_NUMBER,

    /**
     * When found: CHARNUMBER --> A1
     */
    CELL,

    /**
     * When found: " "
     */
    WHITE_SPACE,

    /**
     * When found: =
     */
    EQUAL,

    /**
     * When found: ;
     */
    SEMICOLON,

    /**
     * When found two cells --> A1:A2
     */
    RANGE,

    /**
     * When found: ":"
     */
    COLON;

    /**
     * Assign the representation of the Token Type, used for Debugging.
     * @return
     */
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
                return this.name();
            case 14:
                return ":";
            default:
                return "null";
        }
    }
}
