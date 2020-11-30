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
        String[] num = parsing.split("?=[a-zA-Z]");
        CellCoordinate coordiante = new CellCoordinate(num[0].toCharArray(), Integer.parseInt(num[1]));
        return coordiante;
    }  
    
    protected CellValue getCellValue(CellCoordinate coordinate){
        int column = coordinate.getColumn();
        int row = coordinate.getRow();
        return (SpreadSheet.spreadsheet[column][row]).value; 
    }

}
