/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import edu.upc.etsetb.arqsoft.spreadsheet.exceptions.CircularDependencies;
import edu.upc.etsetb.arqsoft.spreadsheet.exceptions.GrammarErrorFormula;
import edu.upc.etsetb.arqsoft.spreadsheet.formulacompute.FormulaEvaluator;
import java.io.File;
import java.util.List;

/**
 *
 * @author estev
 *
 * IT IS IMPORTANT TOT TAKE INTO ACCOUNT THAT SPREADSHEET IS A MATTRIX THAT GOES
 * FROM [0-->MAX_RAW-1][0--> MAX_COLUMN-1] However, when creating the cells, a
 * cell [0][0] have the position A1. A0 does no exists. Therefore, in order to
 * do the creation of cells, remind that SpreadSheet.Row = Cell.Row-1
 */
public class SpreadSheet {

    private String name;

    /**
     *
     */
    public static Cell[][] spreadsheet;   // [row][column]
    private int max_column;
    private int max_row;

    /**
     *
     */
    public static FormulaEvaluator parser = new FormulaEvaluator();
    private final Importer importer;
    private final Exporter exporter;

    /**
     *
     * @param name
     * @param length
     */
    public SpreadSheet(String name, int length) throws CircularDependencies, GrammarErrorFormula {
        this.name = name;
        initializeSpreadSheet(length);
        importer = new Importer();
        exporter = new Exporter();

    }

    /**
     *
     * @return
     */
    public int[] getMaxLength() {
        int[] max = new int[2];
        max[0] = max_column;
        max[1] = max_row;
        return max;
    }

    /**
     *
     * @param column
     * @param row
     * @return
     */
    public Cell getCell(int column, int row) {
        return this.spreadsheet[row][column];
    }

    private void initializeSpreadSheet(int length) throws CircularDependencies, GrammarErrorFormula {
        this.spreadsheet = new Cell[length][length];
        for (int row = 0; row < length; row++) {
            for (int column = 0; column < length; column++) {
                this.spreadsheet[row][column] = new Cell(column, row + 1, "");
            }
        }
        this.max_row = length;
        this.max_column = length;
    }

    /**
     *
     * @param column
     * @param row
     * @param content
     * @return
     * @throws CircularDependencies
     */
    public Cell createCell(int column, int row, String content) throws CircularDependencies, GrammarErrorFormula {
        complete_cells(column, row);
        return editCell(column, row, content);
    }
    
    public Cell removeCell(int column, int row){
        return this.spreadsheet[row-1][column];

    }

    public Cell editCell(int column, int row, String content) throws CircularDependencies, GrammarErrorFormula {
        Cell cell = getCell(column, row-1);
        cell.updateCell(content, true);
        return cell;
    }
    
    /**
     *
     * @param column
     * @param row
     * @return
     */
    public Cell checkEmpty(int column, int row) {
        try {
            Cell cell = this.spreadsheet[row][column];
            return cell;
        } catch (ArrayIndexOutOfBoundsException ex) {
            return null;
        }
    }

    private void complete_cells(int num_column, int num_row) throws CircularDependencies, GrammarErrorFormula {
        if ((this.max_column < num_column) || (this.max_row < num_row)) {
            Cell[][] copy = this.spreadsheet.clone();
            int new_size_column = Math.max(num_column + 1, this.max_column);
            int new_size_row = Math.max(num_row, this.max_row);

            this.spreadsheet = new Cell[new_size_row][new_size_column];
            for (int row = 0; row < new_size_row; row++) {
                for (int column = 0; column < new_size_column; column++) {
                    if (this.max_column > column) {
                        if (this.max_row > row) {
                            this.spreadsheet[row][column] = copy[row][column];
                        } else {
                            this.spreadsheet[row][column] = new Cell(column, row, "");

                        }
                    } else {
                        this.spreadsheet[row][column] = new Cell(column, row, "");
                    }
                }
            }
            this.max_column = new_size_column;
            this.max_row = new_size_row;
        }
    }

    /**
     *
     * @param file
     */
    public void importSpreadSheet(File file) throws CircularDependencies, GrammarErrorFormula {
        List<Cell[]> imported = importer.importSpreadSheet(file);
        int[] dim = importer.getDimensions();
        this.max_column = dim[0];
        this.max_row = dim[1];
        this.spreadsheet = new Cell[max_column][max_row];
        for (int row = 0; row < max_row; row++) {
            Cell[] row_cells = imported.get(row);
            for (int column = 0; column < max_column; column++) {
                if (column < row_cells.length) {
                    System.out.println("Column : "+ column+ " row " + row+ " is " +  row_cells[column].getStringContent());
                    this.spreadsheet[row][column] = row_cells[column];
                } else {
                    this.spreadsheet[row][column] = new Cell(column, row, "");
                }
            }
        }
        updateSpreadSheet();
    }
    
    private void updateSpreadSheet() throws CircularDependencies{
        for (Cell[] cells : spreadsheet) {
            for (Cell cell : cells) {
                System.out.println(cell.toString());
                cell.recomputeValue(true);
            }
        }
    }

    /**
     *
     * @param file
     */
    public void exportSpreadSheet(File file) {
        exporter.exportSpreadSheet(file, this.spreadsheet);
    }
}
