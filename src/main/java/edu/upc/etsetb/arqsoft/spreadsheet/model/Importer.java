/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author estev
 */
public class Importer {

    int max_row;
    int max_col;

    /**
     *
     */
    public Importer() {
        max_row = 0;
        max_row = 0;
    }

    /**
     *
     * @return
     */
    public int[] getDimensions() {
        int[] max = new int[2];
        max[0] = max_row;
        max[1] = max_row;
        return max;
    }

    /**
     *
     * @param file
     * @return
     */
    public List<CellImpl[]> importSpreadSheet(File file) {
        ArrayList<CellImpl[]> spreadsheet = new ArrayList<>();
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

    private CellImpl[] createRowfromLine(String line, int row) {
        String[] cells_content = line.split(";");
        CellImpl[] cells_row = new CellImpl[cells_content.length];
        int column = 0;
        for (String cell_content : cells_content) {
            cells_row[column] = createRowfromContent(column, row, cell_content);
            column++;
        }
        max_col = Math.max(column, max_col);
        return cells_row;
    }

    private CellImpl createRowfromContent(int column, int row, String content) {
        content.replaceAll(",", ";");
        return new CellImpl(column, row, content, false);
    }

}
