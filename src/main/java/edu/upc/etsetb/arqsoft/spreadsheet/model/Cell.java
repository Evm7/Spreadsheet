/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import edu.upc.etsetb.arqsoft.spreadsheet.exceptions.CircularDependencies;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.Value;
import edu.upc.etsetb.arqsoft.spreadsheet.exceptions.GrammarErrorFormula;
import edu.upc.etsetb.arqsoft.spreadsheet.formulacompute.Argument;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Class of the Cell of the SpreadSheet. Each Cell contains a uniq
 * CellCoordinate and also a CellContent and CellValue.
 *
 * @author estev
 */
public class Cell extends Observable implements Observer {

    /**
     * Coordinate of the Cell in the SpreadSheet.
     */
    protected CellCoordinate coordinates;

    /**
     * Content of the Cell.
     */
    protected CellContent cellcontent;

    /**
     * Value of the Cell.
     */
    public CellValue value;

    /**
     * Constructor of the Cell when passing the column, row, and
     * content.Initialize the Cell and computes all the updateCell use case.
     *
     * @param column Column coordinate
     * @param row Row coordinate
     * @param content Content of the cell
     * @throws
     * edu.upc.etsetb.arqsoft.spreadsheet.exceptions.CircularDependencies Exception raised when exists two formula A and B, and
     * A depends on B and B depends on A.
     * @throws edu.upc.etsetb.arqsoft.spreadsheet.exceptions.GrammarErrorFormula Raised when an incorrect String param is introduced
     */
    public Cell(int column, int row, String content) throws CircularDependencies, GrammarErrorFormula {
        this.coordinates = new CellCoordinate(column, row);
        updateCell(content, true);
    }

    /**
     * Constructor of the Cell when passing the column, row, and
     * content.Initialize the Cell and computes all the updateCell use case.
     *
     * @param column Column coordinate
     * @param row Row coordinate
     * @param content Content of the cell
     * @param computeFormula when updating, used to prevent overflow.
     * @throws
     * edu.upc.etsetb.arqsoft.spreadsheet.exceptions.CircularDependencies Exception raised when exists two formula A and B, and
     * A depends on B and B depends on A.
     * @throws edu.upc.etsetb.arqsoft.spreadsheet.exceptions.GrammarErrorFormula Raised when an incorrect String param is introduced
     */
    public Cell(int column, int row, String content, boolean computeFormula) throws CircularDependencies, GrammarErrorFormula {
        this.coordinates = new CellCoordinate(column, row);
        updateCell(content, computeFormula);
    }

    /**
     * Update the Cell by passing a new content.Parses the Cell and creates the
     * determined CellContent and CellValue
     *
     * @param content Content of the cell
     * @param computeFormula Used to prevent overflow.
     * @throws edu.upc.etsetb.arqsoft.spreadsheet.exceptions.CircularDependencies Exception raised when exists two formula A and B, and
     * A depends on B and B depends on A.
     * @throws edu.upc.etsetb.arqsoft.spreadsheet.exceptions.GrammarErrorFormula Raised when an incorrect String param is introduced.
     */
    public void updateCell(String content, boolean computeFormula) throws CircularDependencies, GrammarErrorFormula {
        TypeOfContent typeOfContent = parseContent(content);
        if (typeOfContent == TypeOfContent.FORMULA) {
            this.cellcontent = new ContentFormula(content);
            if (computeFormula) {
                this.value = new ValueNumber((ContentFormula) this.cellcontent, this.coordinates);
                addAllObservers();

            } else {
                this.value = new CellValue();
            }
        } else if (typeOfContent == TypeOfContent.NUMBER) {
            this.cellcontent = new ContentNumber(content);
            this.value = new ValueNumber((ContentNumber) this.cellcontent);
        } else if (typeOfContent == TypeOfContent.TEXT) {
            this.cellcontent = new ContentText(content);
            this.value = new ValueText((ContentText) this.cellcontent);
        } else {
            this.cellcontent = new CellContent(TypeOfContent.EMPTY, "");
            this.value = new CellValue();
        }

        setChanged();
        notifyObservers();
    }

    /**
     * Checks wheteher the content refers to a Text, Number or Formula
     *
     * @param content Content to parse
     * @return Type of content identifier
     */
    public TypeOfContent parseContent(String content) {
        if (content.equals("")) {
            return TypeOfContent.EMPTY;
        } else if (content.startsWith("=")) {
            return TypeOfContent.FORMULA;
        } else {

            try {
                Double num = Double.parseDouble(content);
                return TypeOfContent.NUMBER;
            } catch (NumberFormatException e) {
                return TypeOfContent.TEXT;
            }
        }
    }

    /**
     * Compute value of the Cell without updating the Content.Used when Updating
     * the whole SpreadSheet to recompute Values after modifying cells refered
     * in formulas.
     *
     * @param computeFormula Used to prevent overflow.Determines if the value 
     * is recomputed or not
     * @throws
     * edu.upc.etsetb.arqsoft.spreadsheet.exceptions.CircularDependencies Exception raised when exists two formula A and B, and
     * A depends on B and B depends on A.
     */
    public void recomputeValue(boolean computeFormula) throws CircularDependencies {
        if (this.cellcontent instanceof ContentFormula) {
            if (computeFormula) {
                addAllObservers();
            }
            this.value = new ValueNumber((ContentFormula) this.cellcontent, this.coordinates);
        } else if (this.cellcontent instanceof ContentNumber) {
            this.value = new ValueNumber((ContentNumber) this.cellcontent);
        } else if (this.cellcontent instanceof ContentText) {
            this.value = new ValueText((ContentText) this.cellcontent);
        } else {
            this.value = new CellValue();
        }

        setChanged();
        notifyObservers();
    }

    /**
     * Only used in Debugging Print a description of the Cell.
     */
    public void show() {
        System.out.println("Cell " + this.coordinates.toString() + " is " + this.cellcontent.getType() + " with value " + printValue());
    }

    @Override
    public String toString() {
        return "Cell{" + "coordinates=" + coordinates + ", cellcontent=" + cellcontent + ", value=" + value + '}';
    }

    /**
     * Returns the Value of the Cell as String
     *
     * @return Value as String
     */
    public String printValue() {
        return value.toString();
    }

    /**
     * Returns the Type of Content of the Cell
     *
     * @return  The type of content
     */
    public TypeOfContent getType_of_content() {
        return cellcontent.getType();
    }

    /**
     * Returns the content of the Cell.
     *
     * @return Content of the cell
     */
    public String getStringContent() {
        return this.cellcontent.getContent();
    }

    public CellContent getContent() {
        return this.cellcontent;
    }

    /**
     * Returns the value of the Cell.
     *
     * @return Value of the cell
     */
    public Value getValue() {
        return this.value;
    }

    public CellCoordinate getCoordinate() {
        return this.coordinates;
    }

    /**
     * Used to add all as Observers all cells that this Cell depends on. Only
     * called when adding a new content Formula to the Cell
     */
    public void addAllObservers() {
        List<Argument> arguments = ((ContentFormula) (this.cellcontent)).getArguments();
        List<CellCoordinate> references_cell = new ArrayList<>();
        for (Argument argument : arguments) {
            references_cell.addAll(argument.getReferences());
        }
        for (CellCoordinate coord : references_cell) {
            int column = coord.getColumn();
            int row = coord.getRow();
            (SpreadSheet.spreadsheet[row - 1][column]).addObserver(this);
        }
    }

    /**
     * Overriding upadte method from Observer Interface.
     *
     * Called when the Cell Arguments of a Observable Formula are updated.
     *
     * @param o Observable object
     * @param o1 Object that causes the update
     */
    @Override
    public void update(java.util.Observable o, Object o1) {
        System.out.println("UPDATE OBSERVER: called over object " + o1);
        try {
            recomputeValue(false);
        } catch (CircularDependencies ex) {
            System.out.println("Circular Dependencies Error: " + ex.getMessage());
        }
    }
}
