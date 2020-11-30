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
public class ArgumentFunction extends Argument {

    public ArgumentFunction(String arg) {
        super(arg);    // Example:  different Arguments separated by ; --> 3;4;5;A1:3B

    }

    @Override
    public OperandFunction getValue() {
        String[] terms = this.arg.split(";");
        List<Term> function = new LinkedList<Term>();

        for (String term : terms) {
            CellCoordinate coordinate = super.parsePlace(term);
            CellValue value = super.getCellValue(coordinate);
            function.add(new OperandNumber((Double) value.getValue()));
        }
        return new OperandFunction(function);
    }

    @Override
    public String getSource() {
        return arg;
    }

}
