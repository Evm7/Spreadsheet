/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Term;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author estev
 */
public class Argument implements Term {

    String arg;

    public Argument(String arg) {
        this.arg = arg;
    }

    @Override
    public String print() {
        return arg;
    }

    public Term getValue() {
        return new OperandNumber(Double.parseDouble(arg));
    }

    public String getSource() {
        return arg;
    }

    @Override
    public String isType() {
        return "Argument";
    }
    
    public List<CellCoordinate> getReferences(){
        List<CellCoordinate> references = new ArrayList<>();
        references.add(parsePlace(this.arg));
        return references;
    }

    protected CellCoordinate parsePlace(String parsing) {
        String row = parsing.replaceAll("[a-zA-Z]", "");
        String col = parsing.replaceAll("[0-9]", "");
        CellCoordinate coordiante = new CellCoordinate(col.toCharArray(), Integer.parseInt(row));
        return coordiante;
    }  
    
    protected CellValue getCellValue(CellCoordinate coordinate){
        int column = coordinate.getColumn();
        int row = coordinate.getRow();
        return (SpreadSheet.spreadsheet[row-1][column]).value; 
    }

}
