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
public class SpreadSheet {
    
    private String name;
    private CellImpl[][] spreadsheet;
    private int max_column;
    private int max_raw;
    
    public SpreadSheet(String name, int length) {
        this.name = name;
        initializeSpreadSheet(length);
    }
    
    public int[] getMaxLength() {
        int[] max = new int[2];
        max[0] = max_column;
        max[1] = max_raw;
        return max;
    }
    
    public CellImpl getCell(int row, int column) {
        return this.spreadsheet[row][column];
    }
    
    private void initializeSpreadSheet(int length) {
        this.spreadsheet = new CellImpl[length][length];
        for (int row = 0; row < length; row++) {
            for (int column = 0; column < length; column++) {
                this.spreadsheet[column][row] = new CellImpl(column, row, "");
            }
        }
        this.max_raw = length;
        this.max_column = length;
    }
    
    public CellImpl createCell(int row, int column, String content) {
        System.out.println("We are creating a cell "+ content+ " in column "+ column+" and row " +row);
        complete_cells(row, column);
        CellImpl cell = new CellImpl(row, column, content);
        addCell(cell, column, row);
        cell.show();
        return cell;
    }
    
    private void addCell(CellImpl cell, int column, int row) {
        this.spreadsheet[column-1][row-1] = cell;
    }
    
    public CellImpl checkEmpty(int column, int raw) {
        try {
            CellImpl cell = this.spreadsheet[column][raw];
            return cell;
        } catch (ArrayIndexOutOfBoundsException ex) {
            return null;
        }
    }
    
    private void complete_cells(int num_row, int num_column) {
        if ((this.max_column < num_column) || (this.max_raw < num_row)){
            CellImpl[][] copy = this.spreadsheet.clone();
            int new_size_column =Math.max(num_column, this.max_column);
            int new_size_row =Math.max(num_row, this.max_raw);

            System.out.println("The new size column is the new size col: "+new_size_column+" + row:"+ new_size_row );
                    
            this.spreadsheet = new CellImpl [new_size_column][new_size_row];
            for (int row = 0; row < new_size_row; row++) {
                for (int column = 0; column < new_size_column; column++) {
                    if (this.max_column > column){
                        this.spreadsheet[column][row] = copy[column][row];
                    }
                    else{
                        this.spreadsheet[column][row] = new CellImpl(column, row, "");
                    }
                }
            }
            this.max_column = new_size_column;
            this.max_raw = new_size_row;
        }
    }
}
