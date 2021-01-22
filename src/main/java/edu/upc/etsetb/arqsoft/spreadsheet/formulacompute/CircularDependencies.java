/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascar�
 */
package edu.upc.etsetb.arqsoft.spreadsheet.formulacompute;

import edu.upc.etsetb.arqsoft.spreadsheet.model.Cell;
import edu.upc.etsetb.arqsoft.spreadsheet.model.CellCoordinate;
import edu.upc.etsetb.arqsoft.spreadsheet.model.ContentFormula;
import edu.upc.etsetb.arqsoft.spreadsheet.model.SpreadSheet;
import edu.upc.etsetb.arqsoft.spreadsheet.model.TypeOfContent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * The @author of SpreadSheet is estev
 */
public class CircularDependencies {

    boolean debug = false;

    public CircularDependencies() {
    }

    /**
     * Uses recursivity to compute all dependencies and its tree
     *
     * @param cellcoordinate Cell coordinates
     * @throws
     * edu.upc.etsetb.arqsoft.spreadsheet.exceptions.CircularDependenciesException
     * Exception raised when exists two formula A and B, and A depends on B and
     * B depends on A.
     */
    public void checkDependencies(CellCoordinate cellcoordinate) throws edu.upc.etsetb.arqsoft.spreadsheet.exceptions.CircularDependenciesException {
        HashMap<String, ArrayList> map = new HashMap<String, ArrayList>();
        //System.out.println("___________CIRCULAR DEPENDENCIES___________");
        map.put(cellcoordinate.toString(), null);
        map.put(cellcoordinate.toString(), circularDependency(cellcoordinate, map));
        //System.out.println(map);
        //System.out.println("___________TREE SEARCH___________");
        TreeSearch search = new TreeSearch(map, cellcoordinate.toString());
    }

    /**
     * Check the circular Dependencies of one formula
     *
     * @param formula
     */
    private ArrayList<String> circularDependency(CellCoordinate cellcoordinate, HashMap<String, ArrayList> map) {
        ArrayList<String> args = new ArrayList<>();
        //System.out.println("MAP IS:");
        //System.out.println(map);
        // Get the Cell from the CellCoordinate
        int column = cellcoordinate.getColumn();
        int row = cellcoordinate.getRow();
        Cell cell = (SpreadSheet.spreadsheet[row - 1][column]);
        //System.out.println("Cell we are checking is " + cell.toString());

        // There are only dependencies if Cell is Formula
        if (cell.getType_of_content() == TypeOfContent.FORMULA) {
            ContentFormula content = (ContentFormula) cell.getContent();
            //System.out.println(content.getArguments());

            // Search for Dependencies of the coordinate
            for (Argument arg : content.getArguments()) {
                if (arg.isType().equals("Argument")) {
                    if (arg instanceof ArgumentRange) {
                        arg = (ArgumentRange) arg;
                        //System.out.println("\tArgument Range");
                        //System.out.println(arg.getReferences());
                    } else {
                        arg = (ArgumentIndividual) arg;
                        //System.out.println("\tArgument Individual");
                    }

                    for (CellCoordinate coord : arg.getReferences()) {
                        //System.out.println("Depends on " + coord.toString());
                        args.add(coord.toString());
                        if (!map.containsKey(coord.toString())) {

                            ArrayList<String> array = circularDependency(coord, map);
                            if (array != null) {
                                map.put(coord.toString(), array);

                            }
                        }
                    }
                }
            }
            return args;
        }
        return null;
    }
}
