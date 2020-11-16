/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Operator;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.Argument;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.Function;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.Operant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javafx.util.Pair;

/**
 *
 * @author estev
 */
public class ContentFormula extends CellContent {

    private String formula;
    private Pair<Argument[], Operator[]>[] pairs;

    public ContentFormula(String formula) {
        super(TypeOfContent.FORMULA, formula);
        this.formula = formula;
        this.pairs = new FormulaParser().parseFormula(formula);
    }

    private boolean checkFormula() {
        return true;
    }

    public void setPairs(Pair<Argument[], Operator[]>[] pairs) {
        this.pairs = pairs;
    }

    public Pair<Argument[], Operator[]>[] getPairs() {
        return pairs;
    }
    
     

}
