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
public class CellCoordinate {

    /**
     *
     */
    protected char[] column;

    /**
     *
     */
    protected int row;

    /**
     *
     * @param column
     * @param row
     */
    public CellCoordinate(char[] column, int row) {
        this.column = column;
        this.row = row;
    }
    
    /**
     *
     * @param column
     * @param row
     */
    public CellCoordinate(int column, int row) {
        this.column = intToChar(column);
        this.row = row;
    }
    
    /**
     *
     * @param column
     * @return
     */
    public char[] intToChar(int column) {
        int number_of_letters = column+1;
        String col = "";
        int modulo;
        while (number_of_letters > 0) {
            modulo = (number_of_letters-1) % ('Z'-'A');
            col = String.valueOf((char)('A' + modulo) + col);
            number_of_letters = (int) ((number_of_letters - modulo) / ('Z'-'A'));
        }
        return col.toCharArray();
    }
    
    /**
     *
     * @return
     */
    public String print(){
        return "["+ new String(column)+","+row+"]";
    }
    
    /**
     *
     * @return
     */
    public int getColumn(){
        String column = new String(this.column);
        int column_num=0;
        for (int i = 0; i < column.length(); i++) {
            column_num += Math.pow(('Z' - 'A' + 1), i) * (column.charAt(column.length()-1-i) - 'A' + 1);
        }
        return column_num-1;
    }
    
    /**
     *
     * @return
     */
    public int getRow(){
        return row;
    }
}
