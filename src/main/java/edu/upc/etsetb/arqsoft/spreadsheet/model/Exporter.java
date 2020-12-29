/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Class which manages the exporting of the SpreadSheet
 * @author estev
 */
public class Exporter {

    /**
     * Empty constructor for the Exporter.
     */
    public Exporter() {
    }

    /**
     * Function used to export SpreadSheet to a file passed as a parameter.
     * Format used is C2v.
     * Iterates through all the rows and transform each to a String S2V line. 
     * Then groups and creates file.
     * @param file in which the SpreadSheet is saved
     * @param spreadsheet Matrix of Cells
     */
    public void exportSpreadSheet(File file, Cell[][] spreadsheet) {
        try {
            FileWriter myWriter = new FileWriter(file);
            String line = "";
            for (Cell[] row : spreadsheet) {
                line = convertRowToLine(row);
                myWriter.write(line+"\n");
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    /**
     * Converts a Row to a String S2V line
     * @param row Array of Cells of one row of the SpreadSheet
     */
    private String convertRowToLine(Cell[] row) {
        String line = "";
        String content;
        for (Cell cellImpl : row) {
            content = cellImpl.getStringContent();
            content = content.replaceAll(";", ",");
            line = line + content + ";";
        }
        return line;
    }

}
