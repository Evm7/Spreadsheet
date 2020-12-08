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
 * Class of the Max Function which implements the Function Interface
 * @author estev
 */
public class FunctionMax implements Function{
    
    /**
     * Constructor of the Function MAX
     */
    public FunctionMax() {
    }
    
    /**
     * Parses the Arguments of the Functions
     * @return
     * @throws NoParseableArguments
     */
    @Override
    public Double[] parseArguments() throws NoParseableArguments{
        throw new NoParseableArguments("Not supported yet."); 
    }

    /**
     * Computes the Max Function to all the operands passed as parameters.
     * Return the Maximum Value of all the Values of the operands passed as parameters.
     * @param args
     * @return
     */
    @Override
    public OperandNumber computeFunction(OperandFunction args) {
        List<OperandNumber> values = args.getValue();
        OperandNumber result = values.get(0);
        for (OperandNumber v: values){
            if ((Double) result.getValue() < (Double) v.getValue()){
                result = v;
            }
        }
        return result;
    }
    
    
}
