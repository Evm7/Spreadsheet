/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author estev
 */
public class Exporter {

    public Exporter() {
    }

    public void exportSpreadSheet(File file, CellImpl[][] spreadsheet) {
        try {
            FileWriter myWriter = new FileWriter(file);
            String line = "";
            for (CellImpl[] row : spreadsheet) {
                line = convertRowToLine(row);
                myWriter.write(line);
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    private String convertRowToLine(CellImpl[] row) {
        String line = "";
        String content;
        for (CellImpl cellImpl : row) {
            content = cellImpl.getContent();
            content = content.replaceAll(";", ",");
            line = line + content + ";";
        }
        return line;
    }

}
