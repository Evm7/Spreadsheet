/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    public static CellImpl[][] spreadsheet;   // [row][column]
    private int max_column;
    private int max_row;
    public static FormulaEvaluator parser = new FormulaEvaluator();
    private final Importer importer;
    private final Exporter exporter;
    private Map<CellCoordinate, List<CellCoordinate>> references;
    private List<CellCoordinate> just_updated;

    public SpreadSheet(String name, int length) {
        this.name = name;
        initializeSpreadSheet(length);
        importer = new Importer();
        exporter = new Exporter();
        references = new HashMap<>();
        just_updated = new ArrayList<>();
    }

    public int[] getMaxLength() {
        int[] max = new int[2];
        max[0] = max_column;
        max[1] = max_row;
        return max;
    }

    public CellImpl getCell(int column, int row) {
        return this.spreadsheet[row][column];
    }

    private void initializeSpreadSheet(int length) {
        this.spreadsheet = new CellImpl[length][length];
        for (int row = 0; row < length; row++) {
            for (int column = 0; column < length; column++) {
                this.spreadsheet[row][column] = new CellImpl(column, row + 1, "");
            }
        }
        this.max_row = length;
        this.max_column = length;
    }

    public CellImpl createCell(int column, int row, String content) throws DoubleDependenciesException {
        complete_cells(column, row);
        CellImpl cell = new CellImpl(column, row, content);
        addCell(cell, column, row - 1);
        return cell;
    }

    private void addCell(CellImpl cell, int column, int row) throws DoubleDependenciesException {
        CellImpl previous = checkEmpty(column, row);
        if (previous != null && (previous.getType_of_content() == TypeOfContent.FORMULA)) {
            this.references.remove(previous.coordinates);
        }
        this.spreadsheet[row][column] = cell;
        addToMap(cell);
        just_updated = new ArrayList<>();

        try {
            updateSpreadSheet(cell);
        } catch (DoubleDependenciesException ex) {
            addCell(previous, column, row);
            throw ex;
        }
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
            int new_size_column = Math.max(num_column+1, this.max_column);
            int new_size_row = Math.max(num_row, this.max_row);

            this.spreadsheet = new CellImpl[new_size_row][new_size_column];
            for (int row = 0; row < new_size_row; row++) {
                for (int column = 0; column < new_size_column; column++) {
                    if (this.max_column > column) {
                        if (this.max_row > row) {
                            this.spreadsheet[row][column] = copy[row][column];
                        } else {
                            this.spreadsheet[row][column] = new CellImpl(column, row, "");

                        }
                    } else {
                        this.spreadsheet[row][column] = new CellImpl(column, row, "");
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
                    this.spreadsheet[row][column] = row_cells[column];
                } else {
                    this.spreadsheet[row][column] = new CellImpl(column, row, "");
                }
            }
        }
    }

    public void exportSpreadSheet(File file) {
        exporter.exportSpreadSheet(file, this.spreadsheet);
    }

    private void addToMap(CellImpl cell) {
        if (cell.cellcontent instanceof ContentFormula) {
            List<Argument> arguments = (((ContentFormula) cell.cellcontent).getArguments());
            List<CellCoordinate> references_cell = new ArrayList<>();
            for (Argument argument : arguments) {
                references_cell.addAll(argument.getReferences());
            }

            references.put(cell.coordinates, references_cell);
        }
    }

    private void updateSpreadSheet(CellImpl edited) throws DoubleDependenciesException {
        if (just_updated.contains(edited.coordinates)) {
            throw new DoubleDependenciesException("Error when updating, cell " + edited.coordinates.print() + " depends on itself.");
        }
        just_updated.add(edited.coordinates);
        // Check if any formula depend on that edited cell
        for (Map.Entry<CellCoordinate, List<CellCoordinate>> entry : references.entrySet()) {
            List<CellCoordinate> value = entry.getValue();
            if (value.contains(edited.coordinates)) {
                CellCoordinate key = entry.getKey();
                CellImpl updated = this.spreadsheet[key.getRow()][key.getColumn()];
                updated.recomputeValue();
                updateSpreadSheet(updated);
            }
        }
    }

    private void doubleDependencies(CellCoordinate toUpdate) {

    }
}
