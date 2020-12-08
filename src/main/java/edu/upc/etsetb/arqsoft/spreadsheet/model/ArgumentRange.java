/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Term;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * SubClass of Argument which refers to a Range of Cells. Ex: A1:B3
 *
 * @author estev
 */
public class ArgumentRange extends Argument {

    /**
     * Constructor of Argument Range
     *
     * @param arg
     */
    public ArgumentRange(String arg) {
        super(arg);
    }

    /**
     * Obtains the values of the Range Argument is refering to. ArgumentRange
     * can only be used inside a Function, therefore, return an OperandFunction.
     *
     * @return
     */
    @Override
    public OperandFunction getValue() {
        String[] terms = this.arg.split(":");
        List<Term> range = new LinkedList<Term>();

        CellCoordinate coordinateTop = super.parsePlace(terms[0]);
        CellCoordinate coordinateBottom = super.parsePlace(terms[1]);

        List<CellCoordinate> inBetween = inBetween(coordinateTop, coordinateBottom);

        for (CellCoordinate coordinate : inBetween) {
            CellValue value = super.getCellValue(coordinate);
            range.add(new OperandNumber((Double) value.getValue()));
        }
        return new OperandFunction(range);

    }

    /**
     * Obtains the List of Coordintaes that are in the Range between the Top
     * Cell Coordinate and the Bottom Cell Coordinate
     *
     * @param top
     * @param bottom
     * @return
     */
    private List<CellCoordinate> inBetween(CellCoordinate top, CellCoordinate bottom) {

        List<CellCoordinate> coordinates = new LinkedList<>();
        for (int i = bottom.getColumn(); i <= top.getColumn(); i++) {
            for (int j = bottom.getRow(); j <= top.getRow(); j++) {
                coordinates.add(new CellCoordinate(i, j));
            }
        }

        return coordinates;
    }

    /**
     * Obtains the coordinates of the cells refered by the Argument Range
     *
     * @return
     */
    @Override
    public List<CellCoordinate> getReferences() {
        String[] terms = this.arg.split(":");
        List<Term> range = new LinkedList<Term>();

        CellCoordinate coordinateTop = super.parsePlace(terms[0]);
        CellCoordinate coordinateBottom = super.parsePlace(terms[1]);

        return inBetween(coordinateTop, coordinateBottom);
    }
}
