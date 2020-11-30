/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author estev
 */
public class ArgumentIndividual extends Argument {

    public ArgumentIndividual(String arg) {
        super(arg);     // One the argument refers to individial cell --> ex: A1

    }

    @Override
    public OperandNumber getValue() {
        CellCoordinate coordinate = super.parsePlace(this.arg);
        CellValue value = super.getCellValue(coordinate);
        return new OperandNumber((Double) value.getValue());
    }

    @Override
    public String getSource() {
        return arg;
    }

    @Override
    public List<CellCoordinate> getReferences(){
        List<CellCoordinate> references = new ArrayList<>();
        references.add(parsePlace(this.arg));
        return references;
    }

}
