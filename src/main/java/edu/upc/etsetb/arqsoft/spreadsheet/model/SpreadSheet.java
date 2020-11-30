/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import java.io.File;
import java.util.List;

/**
 *
 * @author estev
 *
 * IT IS IMPORTANT TOT TAKE INTO ACCOUNT THAT SPREADSHEET IS A MATTRIX THAT GOES
 * FROM [0--> MAX_COLUMN-1][0-->MAX_RAW-1] However, when creating the cells, a
 * cell [0][0] have the position A1. A0 does no exists. Therefore, in order to
 * do the creation of cells, remind that SpreadSheet.Row = Cell.Row-1
 */
public class SpreadSheet {

    private String name;
    public static CellImpl[][] spreadsheet;   // [column][row]
    private int max_column;
    private int max_row;
    public static FormulaEvaluator parser = new FormulaEvaluator();
    private final Importer importer;
    private final Exporter exporter;

    public SpreadSheet(String name, int length) {
        this.name = name;
        initializeSpreadSheet(length);
        importer = new Importer();
        exporter = new Exporter();
    }

    public int[] getMaxLength() {
        int[] max = new int[2];
        max[0] = max_column;
        max[1] = max_row;
        return max;
    }

    public CellImpl getCell(int column, int row) {
        return this.spreadsheet[column][row];
    }
    
    private void initializeSpreadSheet(int length) {
        this.spreadsheet = new CellImpl[length][length];
        for (int row = 0; row < length; row++) {
            for (int column = 0; column < length; column++) {
                this.spreadsheet[column][row] = new CellImpl(column, row + 1, "");
            }
        }
        this.max_row = length;
        this.max_column = length;
    }

    public CellImpl createCell(int column, int row, String content) {
        System.out.println("Creating cell for " + column + " and " + row);
        complete_cells(column, row);
        CellImpl cell = new CellImpl(column, row, content);
        addCell(cell, column, row - 1);
        cell.show();
        return cell;
    }

    private void addCell(CellImpl cell, int column, int row) {
        System.out.println("You are adding to column " + column + " and row " + row);
        this.spreadsheet[column][row] = cell;
    }

    public CellImpl checkEmpty(int column, int raw) {
        try {
            CellImpl cell = this.spreadsheet[column][raw];
            return cell;
        } catch (ArrayIndexOutOfBoundsException ex) {
            return null;
        }
    }

    private void complete_cells(int num_column, int num_row) {
        if ((this.max_column < num_column) || (this.max_row < num_row)) {
            CellImpl[][] copy = this.spreadsheet.clone();
            int new_size_column = Math.max(num_column, this.max_column);
            int new_size_row = Math.max(num_row, this.max_row);

            System.out.println("The new size column is the new size col: " + new_size_column + " + row:" + new_size_row);

            this.spreadsheet = new CellImpl[new_size_column][new_size_row];
            for (int row = 0; row < new_size_row; row++) {
                for (int column = 0; column < new_size_column; column++) {
                    if (this.max_column > column) {
                        this.spreadsheet[column][row] = copy[column][row];
                    } else {
                        this.spreadsheet[column][row] = new CellImpl(column, row, "");
                    }
                }
            }
            this.max_column = new_size_column;
            this.max_row = new_size_row;
        }
    }

    public void importSpreadSheet(File file) {
        List<CellImpl[]> imported = importer.importSpreadSheet(file);
        int[] dim = importer.getDimensions();
        this.max_column = dim[0];
        this.max_row = dim[1];
        this.spreadsheet = new CellImpl[max_column][max_row];
           for (int row = 0; row < max_row; row++) {
               CellImpl[] row_cells = imported.get(row);
                for (int column = 0; column < max_column; column++) {
                    if (this.max_column <= row_cells.length) {
                        this.spreadsheet[column][row] = row_cells[column];
                    } else {
                        this.spreadsheet[column][row] = new CellImpl(column, row, "");
                    }
                }
            }
    }

    public void exportSpreadSheet(File file) {
        exporter.exportSpreadSheet(file, this.spreadsheet);
    }
}
