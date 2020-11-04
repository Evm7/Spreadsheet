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
    private Cell[][] spreadsheet;
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
    
    public Cell getCell(int row, int column) {
        return this.spreadsheet[row][column];
    }
    
    private void initializeSpreadSheet(int length) {
        this.spreadsheet = new Cell[length][length];
        for (int row = 0; row < length; row++) {
            for (int column = 0; column < length; column++) {
                this.spreadsheet[column][row] = new Cell(column, row, TypeOfContent.EMPTY);
            }
        }
        this.max_raw = length;
        this.max_column = length;
    }
    
    public Cell createCell(int row, int column, String content) {
        System.out.println("We are creating a cell "+ content+ " in column "+ column+" and row " +row);
        Cell cell = null;
        complete_cells(row, column);
        if (content.startsWith("=")) {
            try {
                cell = new CellFormula(row, column, content);
            } catch (Error e) {
                System.out.println("Error " + e.toString());
            }
        } else {
            
            try {
                Float num = Float.parseFloat(content);
                cell = new CellNumber(row, column, num);
            } catch (NumberFormatException e) {
                cell = new CellText(row, column, content);
            }
        }
        addCell(cell, column, row);
        cell.show();
        return cell;
        
    }
    
    private void addCell(Cell cell, int column, int row) {
        this.spreadsheet[column-1][row-1] = cell;
    }
    
    public Cell checkEmpty(int column, int raw) {
        try {
            Cell cell = this.spreadsheet[column][raw];
            return cell;
        } catch (ArrayIndexOutOfBoundsException ex) {
            return null;
        }
    }
    
    private void complete_cells(int num_row, int num_column) {
        if ((this.max_column < num_column) || (this.max_raw < num_row)){
            Cell[][] copy = this.spreadsheet.clone();
            int new_size_column =Math.max(num_column, this.max_column);
            int new_size_row =Math.max(num_row, this.max_raw);

            System.out.println("The new size column is the new size col: "+new_size_column+" + row:"+ new_size_row );
                    
            this.spreadsheet = new Cell [new_size_column][new_size_row];
            for (int row = 0; row < new_size_row; row++) {
                for (int column = 0; column < new_size_column; column++) {
                    if (this.max_column > column){
                        this.spreadsheet[column][row] = copy[column][row];
                    }
                    else{
                        this.spreadsheet[column][row] = new Cell(column, row, TypeOfContent.EMPTY);
                    }
                }
            }
            this.max_column = new_size_column;
            this.max_raw = new_size_row;
        }
    }
}
