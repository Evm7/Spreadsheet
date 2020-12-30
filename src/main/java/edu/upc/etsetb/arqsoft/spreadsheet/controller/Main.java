/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.controller;

/**
 *
 * @author estev
 */
public class Main {

    /**
     * Main class for the execution of the program.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        Controller controller = new Controller(false);
        controller.run();

    }

}
