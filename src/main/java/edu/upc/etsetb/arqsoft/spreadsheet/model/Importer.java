/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import edu.upc.etsetb.arqsoft.spreadsheet.exceptions.CircularDependenciesException;
import edu.upc.etsetb.arqsoft.spreadsheet.exceptions.GrammarErrorFormula;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class which manages the importing of the SpreadSheet
 *
 * @author estev
 */
public class Importer {

    int max_row;
    int max_col;

    /**
     * Empty constructor for the Importer.
     */
    public Importer() {
        max_row = 0;
        max_row = 0;
    }

    /**
     * Get the Max Columns and Max Rows of the File SpreadSheet
     * @return Spreadsheet dimensions
     */
    public int[] getDimensions() {
        int[] max = new int[2];
        max[0] = max_row;
        max[1] = max_row;
        return max;
    }

    /**
     * Function used to import  SpreadSheet from a file passed as a parameter.Format used is C2v.
     * Iterates through all the lines and transform each to List of Cells (as Row) 
 Then groups and creates file.
     * @param file from which the SpreadSheet is imported
     * @return Spreadsheet object
     * @throws edu.upc.etsetb.arqsoft.spreadsheet.exceptions.GrammarErrorFormula Raised when an incorrect String param is introduced
     */
    public List<Cell[]> importSpreadSheet(File file) throws GrammarErrorFormula {
        ArrayList<Cell[]> spreadsheet = new ArrayList<>();
        max_row = 0;
        max_col = 0;
        int row = 0;
        try {
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                spreadsheet.add(createRowfromLine(data, row));
                row++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        max_row = row;

        return spreadsheet;
    }

    /**
     * Function that creates  a List of Cell (row of SpreadSheet) from a file line
     * 
     * @param line of the File
     * @param row position of the Row in the SpreadSheet
     * @return the Row of the SpreadSheet containing the several Cells in the specific Row
     */
    private Cell[] createRowfromLine(String line, int row) throws GrammarErrorFormula {
        String[] cells_content = line.split(";");
        Cell[] cells_row = new Cell[cells_content.length];
        int column = 0;
        for (String cell_content : cells_content) {
            cells_row[column] = createCellfromContent(column, row, cell_content);
            column++;
        }
        max_col = Math.max(column, max_col);
        return cells_row;
    }

    /**
     * Creates a Cell from the Content in S2V format.
     * @param column x position of the Cell
     * @param row y position of the Cell
     * @param content which is imported from the file for that specific cell
     * @return Cell object
     */
    private Cell createCellfromContent(int column, int row, String content) throws GrammarErrorFormula {
        content = content.replaceAll(",", ";");
        try {
            return new Cell(column, row+1, content, false);
        } catch (CircularDependenciesException ex) {
            System.out.println("Importer ERROR Error should never ocurr here: "+ex.getMessage());
            return null;
        }
    }

}
