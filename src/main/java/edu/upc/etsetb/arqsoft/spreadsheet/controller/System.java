/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.controller;

import edu.upc.etsetb.arqsoft.spreadsheet.model.Cell;
import edu.upc.etsetb.arqsoft.spreadsheet.model.SpreadSheet;
import edu.upc.etsetb.arqsoft.spreadsheet.view.View;



/**
 *
 * @author estev
 */
public class System {

    private SpreadSheet model;
    private View view;
    private int max_length;
    private String name;

    public System() {
        this.view = new View();
        name = this.view.askQuestion("What is your name?");
        max_length = Integer.parseInt(this.view.askQuestion("How many cells to start?"));
        model = new SpreadSheet(name, max_length);
        this.view.display("Let's start, " + name + " :");
        this.view.printTabloid(model);
    }
    
    public void run(){
        int option;
        do {            
            option = showMenu();
        } while (option!=3);
    }

    private int showMenu() {
        String option = this.view.askQuestion("What do you want to do now?:\n\t1-Add Value\n\t2-Save as text in csv\n\t3-Exit");
        switch (option) {
            case "1":
                addValue();
                this.view.printTabloid(model);
                break;
            case "2":
                save();
                break;
            case "3":
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
        int column = getIntColumn(position[0]);
        int row = Integer.parseInt(position[1]);
        Cell cell = getCell(column, row);
        String value;
        if (cell == null || cell.getType_of_content().equals("None")) {
            value = this.view.askQuestion("Which value do you want to introduce in [" + position[0] + row + "] ?");
            model.createCell(row, column, value);
        } else {
            value = this.view.askQuestion("Do you want to modify cell in [" + position[0] + row + "] : " + cell.getSource() + "? [y/n]");
            if (value.equals("n")) {
                return;
            } else {
                value = this.view.askQuestion("Which value do you want to introduce?");
                model.createCell(row, column, value);
            }
        }
    }
    
    private int getIntColumn(String column){
        int column_num=0;
        
        for (int i = 0; i < column.length(); i++) {
            column_num += Math.pow(('Z' - 'A' + 1), i) * (column.charAt(column.length()-1-i) - 'A' + 1);

        }
        return column_num;
    }
    
    private String getStrColumn(int number) {
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
    
    private void save(){
        this.view.display("Saving ...");
    }
    
    private void exit(){
        this.view.display("See you, "+ this.name+"!");
    }

    private Cell getCell(int column, int raw) {
        return (this.model.checkEmpty(column, raw));
    }

}
