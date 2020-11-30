/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Term;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author estev
 */
public class ArgumentRange extends Argument {

    public ArgumentRange(String arg) {
        super(arg);// When the argument refers to more than one cell in a range --> ex:  A1:B3
    }

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

    private List<CellCoordinate> inBetween(CellCoordinate top, CellCoordinate bottom) {

        List<CellCoordinate> coordinates = new LinkedList<>();
        for (int i = bottom.getColumn(); i < top.getColumn(); i++) {
            for (int j = bottom.getRow(); j < top.getRow(); j++) {
                  coordinates.add(new CellCoordinate(i, j));
            }
        }
        
        return coordinates;
    }

    @Override
    public String getSource() {
        return arg;
    }

}
