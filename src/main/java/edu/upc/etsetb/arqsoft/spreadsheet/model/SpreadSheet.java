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
 * FROM [0 -- MAX_RAW-1][0 -- MAX_COLUMN-1] However, when creating the cells, a
 * cell [0][0] have the position A1. A0 does no exists. Therefore, in order to
 * do the creation of cells, remind that SpreadSheet.Row = Cell.Row-1
 */
public class SpreadSheet {

    private String name;
    private boolean debugging = false;

    public static Cell[][] spreadsheet;   // [row][column]
    private int max_column;
    private int max_row;

    public static FormulaEvaluator parser = new FormulaEvaluator();
    private final Importer importer;
    private final Exporter exporter;

    /**
     * Constructor of the Spreadsheet given a name and its Lenght.Initialize the
     * Cells as Empty to the given lenght
     *
     * @param name Spreadsheet name
     * @param length Spreadsheet length
     * @throws
     * edu.upc.etsetb.arqsoft.spreadsheet.exceptions.CircularDependencies Exception raised when exists two formula A and B, and A depends on B and B depends on A.
     * @throws edu.upc.etsetb.arqsoft.spreadsheet.exceptions.GrammarErrorFormula Raised when an incorrect String param is introduced
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
     * @param debug Debugging value
     */
    public void setDebugger(boolean debug) {
        debugging = debug;
        parser.setDebugger(debug);
    }

    /**
     * Gets the length of the columns and rows as a array of integers
     *
     * @return Spreadsheet length
     */
    public int[] getMaxLength() {
        int[] max = new int[2];
        max[0] = max_column;
        max[1] = max_row;
        return max;
    }

    /**
     * Obtain a Cell from the SpreadSheet from a given row and column
     *
     * @param column Cell column
     * @param row Cell row
     * @return Cell from the Spreadsheet
     */
    public Cell getCell(int column, int row) {
        return SpreadSheet.spreadsheet[row][column];
    }

    /**
     * Initialize the Spreadsheet to the given length
     *
     * @param length Spreadsheet length
     * @throws CircularDependencies Exception raised when exists two formula A and B, and A depends on B and B depends on A.
     * @throws GrammarErrorFormula Raised when an incorrect String param is introduced
     */
    private void initializeSpreadSheet(int length) throws CircularDependencies, GrammarErrorFormula {
        SpreadSheet.spreadsheet = new Cell[length][length];
        for (int row = 0; row < length; row++) {
            for (int column = 0; column < length; column++) {
                SpreadSheet.spreadsheet[row][column] = new Cell(column, row + 1, "");
            }
        }
        this.max_row = length;
        this.max_column = length;
    }

    /**
     * Creates a Cell froma given coordinate and content
     *
     * @param column Cell column
     * @param row Cell row
     * @param content Cell content
     * @return Created cell
     * @throws CircularDependencies Exception raised when exists two formula A and B, and A depends on B and B depends on A.
     * @throws edu.upc.etsetb.arqsoft.spreadsheet.exceptions.GrammarErrorFormula Raised when an incorrect String param is introduced
     */
    public Cell createCell(int column, int row, String content) throws CircularDependencies, GrammarErrorFormula {
        complete_cells(column, row);
        return editCell(column, row, content);
    }

    /**
     * Remove some cell from the Spreadsheet given its coordinates
     *
     * @param column Cell column
     * @param row Cell row
     * @return Cell removed
     */
    public Cell removeCell(int column, int row) {
        return SpreadSheet.spreadsheet[row - 1][column];
    }

    /**
     * Edits the content of a cell
     *
     * @param column Cell column
     * @param row Cell row
     * @param content Cell content
     * @return Edited cell
     * @throws CircularDependencies Exception raised when exists two formula A and B, and A depends on B and B depends on A.
     * @throws GrammarErrorFormula Raised when an incorrect String param is introduced
     */
    public Cell editCell(int column, int row, String content) throws CircularDependencies, GrammarErrorFormula {
        Cell cell = getCell(column, row - 1);
        cell.updateCell(content, true);
        return cell;
    }

    /**
     * Check whether a cell is out of the coordinates of the Spreadsheet and
     * return Null if so
     *
     * @param column Cell column
     * @param row Cell row
     * @return Null or the checked cell
     */
    public Cell checkEmpty(int column, int row) {
        try {
            Cell cell = SpreadSheet.spreadsheet[row][column];
            return cell;
        } catch (ArrayIndexOutOfBoundsException ex) {
            return null;
        }
    }

    /**
     * Fill up the remaining cells as empty to be able to be visualized and
     * accessed to.
     *
     * @param num_column Number of columns on the Spreadsheet
     * @param num_row Number of rows on the Spreadsheet
     * @throws CircularDependencies Exception raised when exists two formula A and B, and A depends on B and B depends on A.
     * @throws GrammarErrorFormula Raised when an incorrect String param is introduced
     */
    private void complete_cells(int num_column, int num_row) throws CircularDependencies, GrammarErrorFormula {
        if ((this.max_column < num_column) || (this.max_row < num_row)) {
            Cell[][] copy = SpreadSheet.spreadsheet.clone();
            int new_size_column = Math.max(num_column + 1, this.max_column);
            int new_size_row = Math.max(num_row, this.max_row);

            SpreadSheet.spreadsheet = new Cell[new_size_row][new_size_column];
            for (int row = 0; row < new_size_row; row++) {
                for (int column = 0; column < new_size_column; column++) {
                    if (this.max_column > column) {
                        if (this.max_row > row) {
                            SpreadSheet.spreadsheet[row][column] = copy[row][column];
                        } else {
                            SpreadSheet.spreadsheet[row][column] = new Cell(column, row, "");

                        }
                    } else {
                        SpreadSheet.spreadsheet[row][column] = new Cell(column, row, "");
                    }
                }
            }
            this.max_column = new_size_column;
            this.max_row = new_size_row;
        }
    }

    /**
     * Used to import the SpreadSheet from a file to the fiven model First adds
     * all the contents to the cells and computes the value f all cells which
     * are not formulas. Then update the SpreadSheet to compute recursively the
     * values of the remaining ones.
     *
     * @param file File containing the spreadsheet to import
     * @throws
     * edu.upc.etsetb.arqsoft.spreadsheet.exceptions.CircularDependencies Exception raised when exists two formula A and B, and A depends on B and B depends on A.
     * @throws edu.upc.etsetb.arqsoft.spreadsheet.exceptions.GrammarErrorFormula Raised when an incorrect String param is introduced
     */
    public void importSpreadSheet(File file) throws CircularDependencies, GrammarErrorFormula {
        List<Cell[]> imported = importer.importSpreadSheet(file);
        int[] dim = importer.getDimensions();
        this.max_column = dim[0];
        this.max_row = dim[1];
        SpreadSheet.spreadsheet = new Cell[max_column][max_row];
        for (int row = 0; row < max_row; row++) {
            Cell[] row_cells = imported.get(row);
            for (int column = 0; column < max_column; column++) {
                if (column < row_cells.length) {
                    SpreadSheet.spreadsheet[row][column] = row_cells[column];
                } else {
                    SpreadSheet.spreadsheet[row][column] = new Cell(column, row, "");
                }
            }
        }
        updateSpreadSheet();
    }

    /**
     * Function used to recompute the value of all the cells in the SpreadSheet.
     * Used to compute all the formulas after importing a file.
     *
     * @throws CircularDependencies Exception raised when exists two formula A and B, and A depends on B and B depends on A.
     */
    private void updateSpreadSheet() throws CircularDependencies {
        for (Cell[] cells : spreadsheet) {
            for (Cell cell : cells) {
                cell.recomputeValue(true);
            }
        }
    }

    /**
     * Export the SpreadSheet to a given file
     * @param file File to export the spreadsheet
     */
    public void exportSpreadSheet(File file) {
        exporter.exportSpreadSheet(file, this.spreadsheet);
    }

    /**
     * ADD FOR DEBUGGING AND TESTING, NOT USED OTHERWISE*
     * @param cellCoord Cell coordinate as String
     * @param content Cell content
     * @throws edu.upc.etsetb.arqsoft.spreadsheet.exceptions.ContentException Raised when cell contains an incorrect content
     * @throws edu.upc.etsetb.arqsoft.spreadsheet.exceptions.BadCoordinateException Raised when introduced an incorrect coordinates
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

    /**
     * Parses the coordinate of a cell to obtain the row and column
     * @param position Cell coordinate as String
     * @return Cell coordinates
     * @throws BadCoordinateException 
     */
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

    // FIRST ADDED 2
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
