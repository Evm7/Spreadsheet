/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import edu.upc.etsetb.arqsoft.spreadsheet.exceptions.BadCoordinateException;
import edu.upc.etsetb.arqsoft.spreadsheet.exceptions.CircularDependencies;
import edu.upc.etsetb.arqsoft.spreadsheet.exceptions.ContentException;
import edu.upc.etsetb.arqsoft.spreadsheet.exceptions.GrammarErrorFormula;
import edu.upc.etsetb.arqsoft.spreadsheet.exceptions.NoNumberException;
import edu.upc.etsetb.arqsoft.spreadsheet.formulacompute.FormulaEvaluator;
import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

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
    private boolean debugging=false;

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
     * Sets debugging value to True or False, in order to visualize some logs
     * 
     * @param debug
     */
    public void setDebugger(boolean debug){
        debugging=debug;
        parser.setDebugger(debug);
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

    public Cell removeCell(int column, int row) {
        return this.spreadsheet[row - 1][column];

    }

    /**
     * Edits the content of a cell
     * 
     * @param column
     * @param row
     * @param content
     * @return
     * @throws CircularDependencies
     * @throws GrammarErrorFormula
     */
    public Cell editCell(int column, int row, String content) throws CircularDependencies, GrammarErrorFormula {
        Cell cell = getCell(column, row - 1);
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
                    this.spreadsheet[row][column] = row_cells[column];
                } else {
                    this.spreadsheet[row][column] = new Cell(column, row, "");
                }
            }
        }
        updateSpreadSheet();
    }

    private void updateSpreadSheet() throws CircularDependencies {
        for (Cell[] cells : spreadsheet) {
            for (Cell cell : cells) {
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

    /**
     * ADD FOR DEBUGGING AND TESTING, NOT USED OTHERWISE*
     */
    // FIRST ADDED 1
    public void setCellContent(String cellCoord, String content) throws ContentException, BadCoordinateException {
        String[] position = parsePosition(cellCoord);
        int column, row;
        try {
            column = getIntColumn(position[0]);
            row = Integer.parseInt(position[1]);
        } catch (IndexOutOfBoundsException ex) {
            throw new BadCoordinateException("Coordinate was not correctly introduced");
        }
        Cell cell = getCell(column, row);
        try {
            if (cell == null || (cell.getType_of_content() == TypeOfContent.EMPTY)) {
                createCell(column, row, content);

            } else {
                editCell(column, row, content);

            }
        } catch (CircularDependencies | GrammarErrorFormula ex) {
            removeCell(column, row);
            throw new ContentException(ex.getMessage());
        }
    }

    private String[] parsePosition(String position) throws BadCoordinateException {
        String[] coord = new String[2];
        if (position.contains("-")) {
            coord = position.split("-");
        } else {
            coord[0] = position.replaceAll("[0-9]", "");
            coord[1] = position.replaceAll("[a-zA-Z]", "");
        }
        coord[0] = coord[0].toUpperCase();
        Pattern col = Pattern.compile("[^A-Z]");
        Pattern row = Pattern.compile("[^0-9]");

        if (col.matcher(coord[0]).find()) {
            throw new BadCoordinateException("Error in column as contains none alphabetical characters. Cell needs to be specified as Column-Row. Example: A1, A-1, a-1 or a1.");
        }
        if (row.matcher(coord[1]).find()) {
            throw new BadCoordinateException("Error in row as contain none numerical characters. Cell needs to be specified as Column-Row. Example: A1, A-1, a-1 or a1.");
        }
        return coord;
    }

    /**
     * Gets the int number of the coordinate from an String Column
     *
     * @param column : passes the column as an String. Ex: AB
     * @return int refering to the coordinate of the column in the SpreadSheet.
     * Ex: 27
     */
    private int getIntColumn(String column) {
        int column_num = 0;
        for (int i = 0; i < column.length(); i++) {
            column_num += Math.pow(('Z' - 'A' + 1), i) * (column.charAt(column.length() - 1 - i) - 'A' + 1);
        }
        return column_num - 1;
    }

    /**
     * Gets the String Alphabetical column of the coordinate from an int Column
     * Number
     *
     * @param column : passes the column as an int. Ex: 27
     * @return String refering to the coordinate of the column in the
     * SpreadSheet. Ex: AB
     */
    private String getStrColumn(int number) {
        int number_of_letters = number;
        String col = "";
        int module;

        while (number_of_letters > 0) {
            module = (number_of_letters - 1) % ('Z' - 'A' + 1);
            col = String.valueOf((char) ('A' + module) + col);
            number_of_letters = (int) ((number_of_letters - module) / ('Z' - 'A' + 1));
        }
        return col;
    }

    // FIRST ADDED 2

    /**
     * Returns the value of a cell as a Double
     * 
     * @param coord
     * @return
     * @throws BadCoordinateException
     * @throws NoNumberException
     */
    public double getCellContentAsDouble(String coord) throws BadCoordinateException, NoNumberException {
        String[] position = parsePosition(coord);
        int column, row;
        try {
            column = getIntColumn(position[0]);
            row = Integer.parseInt(position[1]);
        } catch (IndexOutOfBoundsException ex) {
            throw new BadCoordinateException("Coordinate was not correctly introduced");
        }
        Cell cell = getCell(column, row - 1);
        System.out.println("_____________ VALUE IS " + cell.value.getValueasDouble() + " _____________");
        return cell.value.getValueasDouble();
    }

    // FIRST ADDED 3

    /**
     * Returns the value of a cell as a String
     * 
     * @param coord
     * @return
     * @throws BadCoordinateException
     */
    public String getCellContentAsString(String coord) throws BadCoordinateException {
        String[] position = parsePosition(coord);
        int column, row;
        try {
            column = getIntColumn(position[0]);
            row = Integer.parseInt(position[1]);
        } catch (IndexOutOfBoundsException ex) {
            throw new BadCoordinateException("Coordinate was not correctly introduced");
        }
        Cell cell = getCell(column, row - 1);
        System.out.println("_____________ VALUE IS " + cell.value.toString() + " _____________");

        return cell.value.toString();
    }
}
