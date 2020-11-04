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
public class Cell {

    /**
     * @param args the command line arguments
     */
    protected int column;
    protected int row;
    protected TypeOfContent type_of_content;

    public Cell(int row, int column, TypeOfContent type_of_content) {
        this.column = column;
        this.row = row;
        this.type_of_content = type_of_content;
    }

    public String getColumn() {
        int number_of_letters = this.column;
        String col = "";
        int modulo;

        while (number_of_letters > 0) {
            modulo = (number_of_letters - 1) % ('Z'-'A');
            col = String.valueOf((char)('A' + modulo) + col);
            number_of_letters = (int) ((number_of_letters - modulo) / ('Z'-'A'));
        }
        return col;
    }

    public TypeOfContent getType_of_content() {
        return type_of_content;
    }

    public void setType_of_content(TypeOfContent type_of_content) {
        this.type_of_content = type_of_content;
    }

    public void show() {
        System.out.println("Cell [" + getColumn() + this.row + "] (" + this.type_of_content + ")");
    }

    public String printValue() {
        return "_";
    }

    public String getSource() {
        return "_";
    }
}
