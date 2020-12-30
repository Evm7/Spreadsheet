/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.formulacompute;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Function;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.Value;
import java.util.List;

/**
 * Class of the Promedio Function which implements the Function Interface
 * @author estev
 */
public class FunctionPromedio implements Function{
    
    /**
     * Constructor of the Function PROMEDIO
     */
    public FunctionPromedio() {
    }

    /**
     * Computes the Promedio Function to all the operands passed as parameters.
     * Return the arithmetic mean of the Values of the operands passed as parameters.
     * @param args
     * @return
     */
    @Override
    public OperandNumber computeFunction(OperandFunction args) {
        List<OperandNumber> values = args.getValue();
        Double result = new Double(0);
        for (OperandNumber v: values){
            result = result + (Double) (v.getValue());
        }
        return new OperandNumber(result/values.size());
    }
    
    
    
}
