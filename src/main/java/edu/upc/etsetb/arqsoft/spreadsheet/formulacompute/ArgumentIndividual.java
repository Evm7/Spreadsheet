/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.formulacompute;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Visitor;
import edu.upc.etsetb.arqsoft.spreadsheet.model.CellCoordinate;
import edu.upc.etsetb.arqsoft.spreadsheet.model.CellValue;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * SubClass of Argument which refers to an Individual Cell. Ex: A1
 *
 * @author estev
 */
public class ArgumentIndividual extends Argument {

    /**
     * Constructor of Argument Individual
     *
     * @param arg
     */
    public ArgumentIndividual(String arg) {
        super(arg);

    }

    @Override
    public String toString() {
        return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }
    
    

    /**
     * Obtains the Value of the Cell the Individual Argument is refering to
     *
     * @return
     */
    @Override
    public OperandNumber getValue() {
        CellCoordinate coordinate = super.parsePlace(this.arg);
        CellValue value = super.getCellValue(coordinate);
        return new OperandNumber((Double) value.getValue());
    }

    /**
     * Obtains the coordinate of the cell refered by the Argument Individual
     *
     * @return The list of CellCoordinates just contains one CellCoordinate
     */
    @Override
    public List<CellCoordinate> getReferences() {
        List<CellCoordinate> references = new ArrayList<>();
        references.add(parsePlace(this.arg));
        return references;
    }

    @Override
    public void acceptVisitor(Visitor v) {
        v.visitArgument(this);
    }

}
