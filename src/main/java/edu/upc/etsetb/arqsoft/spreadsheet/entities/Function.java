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
 * Interface with Function of the SpreadSheet
 *
 * @author estev
 */
public interface Function {

    /**
     * Parses the Arguments of the Function
     *
     * @return Array with Arguments as Doubles
     * @throws NoParseableArguments
     */
    Double[] parseArguments() throws NoParseableArguments;

    /**
     * Computes the Function when
     *
     * @param args OperandFunction with the Arguments for the Function
     * @return OperandNumber with the result of the Function
     */
    OperandNumber computeFunction(OperandFunction args);
}
