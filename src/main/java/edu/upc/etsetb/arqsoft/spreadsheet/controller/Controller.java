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
import edu.upc.etsetb.arqsoft.spreadsheet.exceptions.ReadCommandException;
import edu.upc.etsetb.arqsoft.spreadsheet.model.SpreadSheet;
import edu.upc.etsetb.arqsoft.spreadsheet.model.TypeOfContent;
import edu.upc.etsetb.arqsoft.spreadsheet.view.View;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Contains the Controller of the program which manages the View and the Model.
 * Operates into the model depending of the answer that View obtains from the
 * user.
 *
 * @author estev
 */
public class Controller {

    private SpreadSheet model;
    private View view;
    private int max_length = 5;
    private String name;

    /**
     * Class in charge of operating the model depending on the Inputs from the
     * user obtained from teh view.It is the main class, which initialize View
     * and Model and manage the whole Project.
     *
     * @param bln
     */
    public Controller(boolean testing) {
        this.view = new View();
        if (testing) {
            name = "Tester";
        } else {
            name = this.view.askQuestion("What is your name?");
        }
        try {
            model = new SpreadSheet(name, max_length);
        } catch (CircularDependencies | GrammarErrorFormula ex) {
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
        } while (!"Exit".equals(option));
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
                createNewSpreadSheet(false);
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
                this.view.printTabloid(model);
                this.view.display("Error in menu option selection. Please try again.");
                return "0";
        }
        this.view.printTabloid(model);

        return option;
    }

    /**
     * Instruct the program to read the rest of the commands from a text file
     * instead the keyboard. This command shall obtain the file to read commands
     * from and call readCommands for the process.
     */
    private void readCommandsFromFile() {
        this.view.display("Read commands from file ...");
        String path_file;
        File file;
        do {
            path_file = this.view.askQuestion("Which is the path of the file to read commands From? Introduce 'Exit' to return to menu");
            if (path_file.equals("Exit")) {
                return;
            }

            try {
                file = processArgumentFile(path_file);
                break;
            } catch (FileNotFoundException ex) {
                this.view.display(ex.getMessage());
            }
        } while (true);

        readCommands(file);
    }

    /**
     * Used to read the commands of a given file
     *
     * @param file
     */
    private void readCommands(File file) {
        Scanner scanner;
        try {
            scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                processCommand(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException ex) {
            this.view.display("Path introduced does not exist. Can not read file.");
        } catch (CircularDependencies | GrammarErrorFormula | ReadCommandException ex) {
            this.view.display(ex.getMessage());
        } catch (IOException ex) {
            this.view.display("There is an error on the coordinates when adding cell");
        }
    }

    /**
     * Processes the commands that can be found in the Read File. Commands can
     * be of differnet types: RF <text_file_pathname>
     * C
     * E <cell coordinate> <new cell content>
     * L <SV2 file pathname>
     * S <SV2 file pathname>
     *
     * @param command
     * @throws FileNotFoundException
     * @throws CircularDependencies
     * @throws GrammarErrorFormula
     * @throws IOException
     * @throws ReadCommandException
     *
     */
    private void processCommand(String command) throws FileNotFoundException, CircularDependencies, GrammarErrorFormula, IOException, ReadCommandException {
        command = command.toUpperCase();
        String error = "Commands can be of differnet types:\n"
                + "    RF <text_file_pathname>\n"
                + "    C\n"
                + "    E <cell coordinate> <new cell content>\n"
                + "    L <SV2 file pathname>\n"
                + "    S <SV2 file pathname>";
        if (!command.contains(" ")) {
            if (command.equals("C")) {
                createNewSpreadSheet(false);
            } else {
                this.view.display("Error in the format of the command." + error);
            }
        } else {
            String[] sent = command.split(" ");
            File file;
            switch (sent[0]) {
                case "RF":
                    file = processArgumentFile(sent[1]);
                    readCommands(file);
                    break;
                case "L":
                    file = processArgumentFile(sent[1]);
                    this.model.importSpreadSheet(file);
                    break;
                case "S":
                    file = new File(sent[1]);
                    file.createNewFile();
                    this.model.exportSpreadSheet(file);
                    break;

                case "E":
                    String[] position = parsePosition(sent[1]);
                    int column = getIntColumn(position[0]);
                    int row = Integer.parseInt(position[1]);
                    Cell cell = getCell(column, row);
                    if (cell == null || (cell.getType_of_content() == TypeOfContent.EMPTY)) {
                        model.createCell(column, row, sent[2]);
                    } else {
                        model.editCell(column, row, sent[2]);
                    }
                    break;

                default:
                    throw new ReadCommandException("Error in the format of command introduced.");
            }
        }
    }

    /**
     * This function checks whether a given path of the file does exist and can
     * be used as input.
     *
     * @param path_file
     * @return File if found
     * @throws FileNotFoundException
     */
    private File processArgumentFile(String path_file) throws FileNotFoundException {
        File file = new File(path_file);
        if (file.exists()) {
            return file;
        } else {
            throw new FileNotFoundException("Path introduced does not exist. Can not read file.");
        }
    }

    /**
     * This command shall create a new empty SpreadSheet
     *
     * @param tester set to True if the new SpreadSheet will be used in
     * debugging mode, which outputs some visualization of the process
     * @return SpreadSheet created
     */
    public SpreadSheet createNewSpreadSheet(boolean tester) {
        if (tester) {
            max_length = 30;
        } else {
            max_length = Integer.parseInt(this.view.askQuestion("How many cells to start?"));
        }
        try {
            model = new SpreadSheet(name, max_length);
            model.setDebugger(tester);
        } catch (CircularDependencies | GrammarErrorFormula ex) {
            this.view.display("Error should never ocurr here: " + ex.getMessage());
        }
        this.view.display("Let's start, " + name + " :");
        return model;
    }

    /**
     * Edits the value of a cell or insert if none. Asks for the coordinates and
     * content of the cell and calls model to compute the internal next steps.
     */
    private void addContent() {
        String[] position = null;
        while (position == null) {
            String cellCoord = this.view.askQuestion("Where do you want your Content? (column-raw)");
            position = parsePosition(cellCoord);
        }
        int column, row;
        try {
            column = getIntColumn(position[0]);
            row = Integer.parseInt(position[1]);
        } catch (IndexOutOfBoundsException ex) {
            view.display("Coordinate was not correctly introduced");
            return;
        }
        Cell cell = getCell(column, row);
        String content = this.view.askQuestion("Which content do you want to introduce in [" + position[0] + row + "] ?");
        try {

            if (cell == null || (cell.getType_of_content() == TypeOfContent.EMPTY)) {
                model.createCell(column, row, content);
            } else {
                model.editCell(column, row, content);
            }
        } catch (CircularDependencies ex) {
            this.view.display("Error: " + ex.getMessage());
            try {
                model.editCell(column, row, cell.getStringContent());
            } catch (CircularDependencies | GrammarErrorFormula ex1) {
                this.view.display("EDIT_CELL: Error should never ocurr here: " + ex.getMessage());
            }
        } catch (GrammarErrorFormula ex) {
            this.view.display("Error: " + ex.getMessage());
        }

    }

    /**
     * Parses the cell coordinates from a single String. Separates the row and
     * column and check some synta errors
     *
     * @param position
     * @return the column and row processed
     */
    private String[] parsePosition(String position) {
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
            this.view.display("Error in column as contains none alphabetical characters. Cell needs to be specified as Column-Row. Example: A1, A-1, a-1 or a1.");
            return null;
        }
        if (row.matcher(coord[1]).find()) {
            this.view.display("Error in row as contain none numerical characters. Cell needs to be specified as Column-Row. Example: A1, A-1, a-1 or a1.");
            return null;
        }
        return coord;
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

            try {
                file = processArgumentFile(path_file);
                break;
            } catch (FileNotFoundException ex) {
                this.view.display(ex.getMessage());
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
