/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

/**
 * Class which manages the Coordinates of the Cells
 *
 * @author estev
 */
public class CellCoordinate {

    /**
     * Column is a list of Chars in abecedary way. Ex: A, B, .., Z, AA, AB, ...
     * Refers to the x Position of the Cell in the SpreadSheet
     */
    protected char[] column;

    /**
     * Row is an integer. Refers to the y Position of the Cell in the
     * SpreadSheet
     */
    protected int row;

    /**
     * Constructor of the Cell Coordinate with Abecedary way
     *
     * @param column
     * @param row
     */
    public CellCoordinate(char[] column, int row) {
        this.column = column;
        this.row = row;
    }

    /**
     * COnstructor of the Cell Coordinate with Numbers.
     *
     * @param column
     * @param row
     */
    public CellCoordinate(int column, int row) {
        this.column = intToChar(column);
        this.row = row;
    }

    /**
     * Converts the reference of the X Position of the Cell from number to
     * Abecedary list of Char
     *
     * @param column
     * @return
     */
    public char[] intToChar(int column) {
        int number_of_letters = column + 1;
        String col = "";
        int modulo;
        while (number_of_letters > 0) {
            modulo = (number_of_letters - 1) % ('Z' - 'A');
            col = String.valueOf((char) ('A' + modulo) + col);
            number_of_letters = (int) ((number_of_letters - modulo) / ('Z' - 'A'));
        }
        return col.toCharArray();
    }

    /**
     * Formats the Cell Coordinate as a visual String [column, row]
     *
     * @return
     */
    @Override
    public String toString() {
        return "[" + new String(column) + "," + row + "]";
    }

    /**
     * Gets the column as int
     *
     * @return
     */
    public int getColumn() {
        String column = new String(this.column);
        int column_num = 0;
        for (int i = 0; i < column.length(); i++) {
            column_num += Math.pow(('Z' - 'A' + 1), i) * (column.charAt(column.length() - 1 - i) - 'A' + 1);
        }
        return column_num - 1;
    }

    /**
     * Gets the row as int.
     *
     * @return
     */
    public int getRow() {
        return row;
    }
}
