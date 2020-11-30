/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.controller;

import edu.upc.etsetb.arqsoft.spreadsheet.model.CellImpl;
import edu.upc.etsetb.arqsoft.spreadsheet.model.SpreadSheet;
import edu.upc.etsetb.arqsoft.spreadsheet.model.TypeOfContent;
import edu.upc.etsetb.arqsoft.spreadsheet.view.View;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author estev
 */
public class Controller {

    private SpreadSheet model;
    private View view;
    private int max_length;
    private String name;

    public Controller() {
        this.view = new View();
        name = this.view.askQuestion("What is your name?");
        max_length = Integer.parseInt(this.view.askQuestion("How many cells to start?"));
        model = new SpreadSheet(name, max_length);
        this.view.display("Let's start, " + name + " :");
        this.view.printTabloid(model);
    }

    public void run() {
        int option;
        do {
            option = showMenu();
        } while (option != 4);
    }

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
        cell.show();
        String value;
        if (cell == null || (cell.getType_of_content() == TypeOfContent.EMPTY)) {
            value = this.view.askQuestion("Which value do you want to introduce in [" + position[0] + row + "] ?");
            model.createCell(column, row, value);
        } else {
            value = this.view.askQuestion("Do you want to modify cell in [" + position[0] + row + "] : " + cell.printValue() + "? [y/n]");
            if (value.equals("n")) {
                return;
            } else {
                value = this.view.askQuestion("Which value do you want to introduce?");
                model.createCell(column, row, value);
            }
        }
    }

    private int getIntColumn(String column) {
        int column_num = 0;
        for (int i = 0; i < column.length(); i++) {
            column_num += Math.pow(('Z' - 'A' + 1), i) * (column.charAt(column.length() - 1 - i) - 'A' + 1);
        }
        return column_num - 1;
    }

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

    private void exit() {
        this.view.display("See you, " + this.name + "!");
    }

    private CellImpl getCell(int column, int raw) {
        return (this.model.checkEmpty(column, raw - 1));
    }

}
