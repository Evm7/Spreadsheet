/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Term;
import java.util.ArrayList;
import java.util.List;

/**
 * An Argument is a reference to one or more Cell/s of the SpreadSheet. Argument
 * Class which implements Term. SuperClass of all arguments that refer to
 * another Cell.
 *
 * @author estev
 */
public class Argument implements Term {

    String arg;

    /**
     * Constructor of Argument, which needs the content parsed in the Formula
     *
     * @param arg
     */
    public Argument(String arg) {
        this.arg = arg;
    }

    /**
     * Prints the content of the argument.
     *
     * @return
     */
    @Override
    public String print() {
        return arg;
    }

    /**
     * Gets the Value of the Cell Corresponding to the argument and transorm
     * into Term
     *
     * @return
     */
    public Term getValue() {
        return new OperandNumber(Double.parseDouble(arg));
    }

    /**
     * Return the Type of Argument: ArgumentIndividual | ArgumentRange
     *
     * @return
     */
    @Override
    public String isType() {
        return "Argument";
    }

    /**
     * Get the List of Coordinates of the cells referenced in the Argument.
     *
     * @return
     */
    public List<CellCoordinate> getReferences() {
        List<CellCoordinate> references = new ArrayList<>();
        references.add(parsePlace(this.arg));
        return references;
    }

    /**
     * Obtain a Coordintate from an Individual Argument: A1 --> Column 1, Row 1
     *
     * @param parsing
     * @return
     */
    protected CellCoordinate parsePlace(String parsing) {
        String row = parsing.replaceAll("[a-zA-Z]", "");
        String col = parsing.replaceAll("[0-9]", "");
        CellCoordinate coordiante = new CellCoordinate(col.toCharArray(), Integer.parseInt(row));
        return coordiante;
    }

    /**
     *
     * @param coordinate
     * @return
     */
    protected CellValue getCellValue(CellCoordinate coordinate) {
        int column = coordinate.getColumn();
        int row = coordinate.getRow();
        return (SpreadSheet.spreadsheet[row - 1][column]).value;
    }

}
