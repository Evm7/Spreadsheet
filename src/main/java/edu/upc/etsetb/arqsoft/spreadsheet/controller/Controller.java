/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.controller;

import edu.upc.etsetb.arqsoft.spreadsheet.model.CellImpl;
import edu.upc.etsetb.arqsoft.spreadsheet.model.DoubleDependenciesException;
import edu.upc.etsetb.arqsoft.spreadsheet.model.SpreadSheet;
import edu.upc.etsetb.arqsoft.spreadsheet.model.TypeOfContent;
import edu.upc.etsetb.arqsoft.spreadsheet.view.View;
import java.io.File;
import java.io.IOException;

/**
 * Contrains the Controller of the program which manages the View and the Model.
 * Operates into the model depending of the answer that View obtains from the
 * user.
 *
 * @author estev
 */
public class Controller {

    private SpreadSheet model;
    private View view;
    private int max_length;
    private String name;

    /**
     * Class in charge of operating the model depending on the Inputs from the
     * user obtained from teh view. It is the main class, which initialize View
     * and Model and manage the whole Project.
     */
    public Controller() {
        this.view = new View();
        name = this.view.askQuestion("What is your name?");
        max_length = Integer.parseInt(this.view.askQuestion("How many cells to start?"));
        model = new SpreadSheet(name, max_length);
        this.view.display("Let's start, " + name + " :");
        this.view.printTabloid(model);
    }

    /**
     * Loop iteration that shows the menu whereas user does not exit.
     */
    public void run() {
        int option;
        do {
            option = showMenu();
        } while (option != 4);
    }

    /**
     * Ask through view class the next step for the user, and calls the model to
     * do the answered step.
     */
    private int showMenu() {
        String option = this.view.askQuestion("What do you want to do now?:\n\t1-Edit Cell\n\t2-Import Spreadsheet\n\t3-Export Spreadsheet\n\t4-Exit");
        switch (option) {
            case "1":
                addValue();
                this.view.printTabloid(model);
                break;
            case "2":
                importSpreadSheet();
                break;
            case "3":
                exportSpreadSheet();
                break;
            case "4":
                exit();
                break;
            default:
                // code block
                this.view.display("Error in menu option selection. Please try again.");
                return 0;
        }
        return Integer.parseInt(option);
    }

    /**
     * Edits the value of a cell or insert if none. Asks for the coordinates and
     * content of the cell and calls model to compute the internal next steps.
     */
    private void addValue() {
        String[] position = this.view.askQuestion("Where do you want your value? (column-raw)").split("-");
        int column, row;
        try {
            column = getIntColumn(position[0]);
            row = Integer.parseInt(position[1]);
        } catch (IndexOutOfBoundsException ex) {
            view.display("Value was not correctly introduced");
            return;
        }
        CellImpl cell = getCell(column, row);
        String value;
        if (cell == null || (cell.getType_of_content() == TypeOfContent.EMPTY)) {
            value = this.view.askQuestion("Which value do you want to introduce in [" + position[0] + row + "] ?");
            try {
                model.createCell(column, row, value);
            } catch (DoubleDependenciesException ex) {
                this.view.display("Error: " + ex.getMessage());
            }
        } else {
            value = this.view.askQuestion("Do you want to modify cell in [" + position[0] + row + "] : " + cell.printValue() + "? [y/n]");
            if (value.equals("n")) {
                return;
            } else {
                value = this.view.askQuestion("Which value do you want to introduce?");
                try {
                    model.createCell(column, row, value);
                } catch (DoubleDependenciesException ex) {
                    this.view.display("Error: " + ex.getMessage());
                }
            }
        }
    }

    /**
     * Get the Cell from the SpreadSheet located in coordinates passed as
     * argument.
     *
     * @param column: int with the column coordinate
     * @param row: int with the row coordinate
     *
     * @return the cell or Null if does not exists.
     */
    private CellImpl getCell(int column, int row) {
        return (this.model.checkEmpty(column, row - 1));
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

    /**
     * Exports the SpreadSheet to a S2V file. Asks for the path to save the file
     * to. Manage errors if path does not exist.
     */
    private void exportSpreadSheet() {
        this.view.display("Exporting Spreadhseet to S2V file ...");
        String path_file;
        File file;
        do {
            path_file = this.view.askQuestion("Which is the path to save the file to? Introduce 'Exit' to return to menu");
            if (path_file.equals("Exit")) {
                return;
            }

            try {
                file = new File(path_file);
                if (file.createNewFile()) {
                    this.view.display("File created: " + file.getName());
                    break;
                } else {
                    if ((this.view.askQuestion("File already exists. Do you want to overwrite the file? [Y/N]")).equals("Y")) {
                        break;
                    }
                }
            } catch (IOException e) {
                this.view.display("Path introduce does not exist. Can not export file.");
            }
        } while (true);
        this.model.exportSpreadSheet(file);
    }

    /**
     * Imports an SpreadSheet from a S2V file. Asks for the path to import the
     * file from. Manage errors if path does not exist.
     */
    private void importSpreadSheet() {
        this.view.display("Import Spreadsheet from S2V file ...");
        String path_file;
        File file;
        do {
            path_file = this.view.askQuestion("Which is the path to the file to import? Introduce 'Exit' to return to menu");
            if (path_file.equals("Exit")) {
                return;
            }

            file = new File(path_file);
            if (file.exists()) {
                break;
            } else {
                this.view.display("Path introduced does not exist. Can not import file.");
            }
        } while (true);
        this.model.importSpreadSheet(file);
    }

    /**
     * Function to reproduce all the steps when user chose to exit the program.
     */
    private void exit() {
        this.view.display("See you, " + this.name + "!");
    }

}
