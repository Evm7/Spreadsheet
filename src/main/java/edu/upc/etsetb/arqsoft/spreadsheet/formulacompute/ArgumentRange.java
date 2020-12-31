/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.formulacompute;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Term;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.Visitor;
import edu.upc.etsetb.arqsoft.spreadsheet.model.CellCoordinate;
import edu.upc.etsetb.arqsoft.spreadsheet.model.CellValue;
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
     * @param arg Argument
     */
    public ArgumentRange(String arg) {
        super(arg);
    }

    @Override
    public String toString() {
        return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Obtains the values of the Range Argument is refering to. ArgumentRange
     * can only be used inside a Function, therefore, return an OperandFunction.
     *
     * @return Operand Function with the values of the Range Argument is refering to
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
        int columnMin = Math.min(bottom.getColumn(), top.getColumn());
        int columnMax = Math.max(bottom.getColumn(), top.getColumn());
        int rowMin = Math.min(bottom.getRow(), top.getRow());
        int rowMax = Math.max(bottom.getRow(), top.getRow());

        List<CellCoordinate> coordinates = new LinkedList<>();
        for (int i = columnMin; i <= columnMax; i++) {
            for (int j = rowMin; j <= rowMax; j++) {
                coordinates.add(new CellCoordinate(i, j));
            }
        }

        return coordinates;
    }

    /**
     * Obtains the coordinates of the cells refered by the Argument Range
     *
     * @return List of cell coordinates
     */
    @Override
    public List<CellCoordinate> getReferences() {
        String[] terms = this.arg.split(":");
        List<Term> range = new LinkedList<Term>();

        CellCoordinate coordinateTop = super.parsePlace(terms[0]);
        CellCoordinate coordinateBottom = super.parsePlace(terms[1]);

        return inBetween(coordinateTop, coordinateBottom);
    }

    @Override
    public void acceptVisitor(Visitor v) {
        v.visitArgument(this);
    }
}
