/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.entities;

import edu.upc.etsetb.arqsoft.spreadsheet.model.NoParseableArguments;
import edu.upc.etsetb.arqsoft.spreadsheet.formulacompute.OperandFunction;
import edu.upc.etsetb.arqsoft.spreadsheet.formulacompute.OperandNumber;

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
