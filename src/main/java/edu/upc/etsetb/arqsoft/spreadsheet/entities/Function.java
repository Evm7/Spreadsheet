/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.entities;

import edu.upc.etsetb.arqsoft.spreadsheet.model.NoParseableArguments;
import edu.upc.etsetb.arqsoft.spreadsheet.model.OperandFunction;
import edu.upc.etsetb.arqsoft.spreadsheet.model.OperandNumber;

/**
 *
 * @author estev
 */
public interface Function {
    Double[] parseArguments() throws NoParseableArguments;
    OperandNumber computeFormula(OperandFunction args);
}
