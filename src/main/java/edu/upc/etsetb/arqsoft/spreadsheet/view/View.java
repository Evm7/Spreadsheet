/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.view;

import edu.upc.etsetb.arqsoft.spreadsheet.model.Cell;
import edu.upc.etsetb.arqsoft.spreadsheet.model.SpreadSheet;
import java.util.Scanner;

/**
 * Class in charge of contacting with the user and obtain the answers.
 *
 * @author estev
 */
public class View {

    private int max_column;
    private int max_raw;
    private Scanner scanner;

    /**
     * Constructor of the View class which initialize the Scanner.
     */
    public View() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Constructor of the View class which initialize the Scanner and sets the
     * max dimensions to display
     *
     * @param max_column Max columns to display
     * @param max_raw Max rows to display
     */
    public View(int max_column, int max_raw) {
        this.scanner = new Scanner(System.in);
        this.max_column = max_column;
        this.max_raw = max_raw;
    }

    /**
     * Establish the Maximum Number of Columns to Display
     *
     * @param max_column Max columns to display
     */
    public void setMax_column(int max_column) {
        this.max_column = max_column;
    }

    /**
     * Establish the Maximum Number of Rows to Display
     *
     * @param max_raw Max rows to display
     */
    public void setMax_raw(int max_raw) {
        this.max_raw = max_raw;
    }

    /**
     * Print the SpreadSheet passed as argument.
     *
     * @param cells Cells to display
     */
    public void printTabloid(SpreadSheet cells) {

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
                    System.out.print(getColumn(j + 1) + "\t");
                } else {
                    System.out.print(cells.getCell(j, i - 1).printValue() + "\t");
                }
            }
            System.out.println();
        }
    }

    /**
     * Print the value of the cell pased as argument
     *
     * @param cell: cell to print its value.
     */
    private void printCell(Cell cell) {
        if (cell == null) {
            System.out.print("" + "\t");
        } else {
            System.out.print(cell.printValue() + "\t");
        }
    }

    /**
     * Ask a question passed as argument to the user. Returns the answer given.
     *
     * @param question String with the question to ask
     * @return String containing the answer introduced
     */
    public String askQuestion(String question) {
        System.out.println(question);
        String answer = scanner.nextLine();  // Read user input
        return answer;
    }

    /**
     * Display the String passed as argument to inform the user.
     *
     * @param display String to display
     */
    public void display(String display) {
        System.out.println(display);
    }

    /**
     * Obtain the Reference of the Column in the Abecedary way.
     *
     * @param number: integer with the number position of the cell.
     * @return column as string in Abecedary (1-- A, 2--B, ...)
     */
    public String getColumn(int number) {
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

    /**
     * Print all the SpreadSheet. Used for debugging
     *
     * @param sheet: SpreadhSheet to print.
     * @return column as string in Abecedary (1--> A, 2-->B, ...)
     */
    private void printaAll(SpreadSheet sheet) {
        int[] max = sheet.getMaxLength();
        int max_col = max[0];
        int max_row = max[1];
        Cell cell;
        for (int i = 0; i < max_col; i++) {
            for (int j = 0; j < max_row; j++) {
                cell = sheet.getCell(i, j);
                System.out.println(" For column " + i + " and row " + j + " we have:");
                cell.show();
            }
        }
    }
}
