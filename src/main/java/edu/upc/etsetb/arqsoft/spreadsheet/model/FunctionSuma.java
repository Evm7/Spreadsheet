/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Function;
import java.util.List;

/**
 * Class of the Suma Function which implements the Function Interface
 * @author estev
 */
public class FunctionSuma implements Function {

    /**
     * Constructor of the Function PROMEDIO
     */
    public FunctionSuma() {
    }

    /**
     * Parses the Arguments of the Functions
     *
     * @return
     * @throws NoParseableArguments
     */
    @Override
    public Double[] parseArguments() throws NoParseableArguments {
        throw new NoParseableArguments("Not supported yet.");
    }

    /**
     * Computes the Suma Function to all the operands passed as parameters.
     * Return the sum of the Values of the operands passed as parameters.
     *
     * @param args
     * @return
     */
    @Override
    public OperandNumber computeFunction(OperandFunction args) {
        List<OperandNumber> values = args.getValue();
        Double result = new Double(0);
        for (OperandNumber v : values) {
            result = result + (Double) (v.getValue());
        }
        return new OperandNumber(result);
    }

}
