/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.view;

import edu.upc.etsetb.arqsoft.spreadsheet.model.CellImpl;
import edu.upc.etsetb.arqsoft.spreadsheet.model.SpreadSheet;
import java.util.Scanner;



/**
 *
 * @author estev
 */
public class View {

    private int max_column;
    private int max_raw;
    private Scanner scanner;

    public View() {
        this.scanner = new Scanner(System.in);
    }

    public View(int max_column, int max_raw) {
        this.scanner = new Scanner(System.in);
        this.max_column = max_column;
        this.max_raw = max_raw;
    }

    public void setMax_column(int max_column) {
        this.max_column = max_column;
    }

    public void setMax_raw(int max_raw) {
        this.max_raw = max_raw;
    }

    public void printTabloid(SpreadSheet cells) {
        printasu(cells);
                
        int[] max = cells.getMaxLength();
        max_column = max[0];
        max_raw = max[1];
        for (int i = 0; i <= max_raw; i++) {
            // Print the number of the row
            if (i != 0) {
                System.out.print(i + "| \t");
            }
            for (int j = 0; j < max_column; j++) {
                if (i == 0) {
                    if (j == 0) {
                        // Refer correctly the column names
                        System.out.print("\t");
                    }
                    System.out.print(getColumn(j+1) + "\t");
                } else {
                    System.out.print(cells.getCell(j, i-1).printValue() + "\t");
                }
            }
            System.out.println();
        }
    }

    private void printCell(CellImpl cell, boolean lastColumn, boolean lastRow) {
        System.out.print(cell.printValue());
    }

    public String askQuestion(String question) {
        System.out.println(question);
        String answer = scanner.nextLine();  // Read user input
        return answer;
    }

    public void display(String display) {
        System.out.println(display);
    }

    public String getColumn(int number) {
        int number_of_letters = number;
        String col = "";
        int module;

        while (number_of_letters > 0) {
            module = (number_of_letters - 1) % ('Z' - 'A'+1);
            col = String.valueOf((char) ('A' + module) + col);
            number_of_letters = (int) ((number_of_letters - module) / ('Z' - 'A'+1));
        }
        return col;
    }
    
    private void printasu(SpreadSheet sheet){
        int[] max = sheet.getMaxLength();
        int max_col = max[0];
        int max_row = max[1];
        CellImpl cell ;
        for(int i=0; i<max_col; i++){
            for(int j = 0; j< max_row; j++){
                cell  = sheet.getCell(i, j);
                System.out.println(" For column "+ i +" and row " + j+ " we have:");
                cell.show();
            }
        }
    }
}
