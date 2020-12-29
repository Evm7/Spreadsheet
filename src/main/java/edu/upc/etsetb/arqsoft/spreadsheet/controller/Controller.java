/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.controller;

import edu.upc.etsetb.arqsoft.spreadsheet.model.Cell;
import edu.upc.etsetb.arqsoft.spreadsheet.exceptions.CircularDependencies;
import edu.upc.etsetb.arqsoft.spreadsheet.exceptions.GrammarErrorFormula;
import edu.upc.etsetb.arqsoft.spreadsheet.model.SpreadSheet;
import edu.upc.etsetb.arqsoft.spreadsheet.model.TypeOfContent;
import edu.upc.etsetb.arqsoft.spreadsheet.view.View;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private int max_length = 0;
    private String name;

    /**
     * Class in charge of operating the model depending on the Inputs from the
     * user obtained from teh view. It is the main class, which initialize View
     * and Model and manage the whole Project.
     */
    public Controller() {
        this.view = new View();
        name = this.view.askQuestion("What is your name?");
        try {
            model = new SpreadSheet(name, max_length);
        } catch (CircularDependencies ex) {
            this.view.display("Error should never ocurr here: " + ex.getMessage());
        } catch (GrammarErrorFormula ex) {
            this.view.display("Error should never ocurr here: " + ex.getMessage());
        }
    }

    /**
     * Loop iteration that shows the menu whereas user does not exit.
     */
    public void run() {
        String option;
        do {
            option = showMenu();
        } while (option != "Exit");
    }

    /**
     * Ask through view class the next step for the user, and calls the model to
     * do the answered step.
     */
    private String showMenu() {
        String option = this.view.askQuestion("What do you want to do now?:\n\t1-Read commands from File (command RF)\n\t2-Create a New Spreadsheet(command C)\n\t3-Edit Cell (command E)\n\t4-Load a Spreadsheet from a file (command L)\n\t5-Save the Spreadsheet to a file (command S)\n\t6-Exit (command Exit)");
        switch (option) {
            case "1":
            case "RF":
                readCommandsFromFile();
                break;
            case "2":
            case "C":
                createNewSpreadSheet();
                break;
            case "3":
            case "E":
                addContent();
                break;
            case "4":
            case "L":
                importSpreadSheet();
                break;
            case "5":
            case "S":
                exportSpreadSheet();
                break;
            case "6":
            case "Exit":
                exit();
                return "Exit";
            default:
                // code block
                this.view.printTabloid(model);
                this.view.display("Error in menu option selection. Please try again.");
                return "0";
        }
        this.view.printTabloid(model);

        return option;
    }

    /**
     * This command shall instruct the program to read the rest of the commands
     * from a text file instead the keyboard. This command shall have one
     * argument: the text file pathname.
     */
    private void readCommandsFromFile() {

    }

    /**
     * This command shall create a new empty SpreadSheet
     */
    private void createNewSpreadSheet() {
        max_length = Integer.parseInt(this.view.askQuestion("How many cells to start?"));
        try {
            model = new SpreadSheet(name, max_length);
        } catch (CircularDependencies ex) {
            this.view.display("Error should never ocurr here: " + ex.getMessage());
        } catch (GrammarErrorFormula ex) {
            this.view.display("Error should never ocurr here: " + ex.getMessage());
        }
        this.view.display("Let's start, " + name + " :");
    }

    /**
     * Edits the value of a cell or insert if none. Asks for the coordinates and
     * content of the cell and calls model to compute the internal next steps.
     */
    private void addContent() {
        if (this.max_length == 0) {
            createNewSpreadSheet();
        }
        String[] position = this.view.askQuestion("Where do you want your Content? (column-raw)").split("-");
        int column, row;
        try {
            column = getIntColumn(position[0]);
            row = Integer.parseInt(position[1]);
        } catch (IndexOutOfBoundsException ex) {
            view.display("Content was not correctly introduced");
            return;
        }
        Cell cell = getCell(column, row);
        String value;
        if (cell == null || (cell.getType_of_content() == TypeOfContent.EMPTY)) {
            value = this.view.askQuestion("Which content do you want to introduce in [" + position[0] + row + "] ?");
            try {
                model.createCell(column, row, value);
            } catch (CircularDependencies ex) {
                this.view.display("Error: " + ex.getMessage());
                model.removeCell(column, row);
            } catch (GrammarErrorFormula ex) {
                this.view.display("Error: " + ex.getMessage());
                model.removeCell(column, row);
            }
        } else {
            value = this.view.askQuestion("Do you want to modify cell in [" + position[0] + row + "] : " + cell.printValue() + "? [y/n]");
            if (value.equals("n")) {
                return;
            } else {
                value = this.view.askQuestion("Which content do you want to introduce?");
                try {
                    model.editCell(column, row, value);
                } catch (CircularDependencies ex) {
                    this.view.display("Error: " + ex.getMessage());
                    try {
                        model.editCell(column, row, cell.getStringContent());
                    } catch (CircularDependencies ex1) {
                        this.view.display("EDIT_CELL: Error should never ocurr here: " + ex.getMessage());
                    } catch (GrammarErrorFormula ex1) {
                        this.view.display("EDIT_CELL: Error should never ocurr here: " + ex.getMessage());
                    }
                } catch (GrammarErrorFormula ex) {
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
    private Cell getCell(int column, int row) {
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
        try {
            this.model.importSpreadSheet(file);
        } catch (CircularDependencies ex) {
            this.view.display("Circular dependencies sError in the import: " + ex.getMessage());
        } catch (GrammarErrorFormula ex) {
            this.view.display("Grammar Error in a formula when import: " + ex.getMessage());
        }
    }

    /**
     * Function to reproduce all the steps when user chose to exit the program.
     */
    private void exit() {
        this.view.display("See you, " + this.name + "!");
    }

}
