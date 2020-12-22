/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Value;
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
     * Constructor of the Cell when passing the column, row, and content.
     * Initialize the Cell and computes all the updateCell use case.
     *
     * @param column
     * @param row
     * @param content
     */
    public Cell(int column, int row, String content) {
        this.coordinates = new CellCoordinate(column, row);
        updateCell(content, true);
    }

    /**
     * Constructor of the Cell when passing the column, row, and content.
     * Initialize the Cell and computes all the updateCell use case.
     *
     * @param column
     * @param row
     * @param content
     * @param computeFormula: when updating, used to prevent overflow.
     */
    public Cell(int column, int row, String content, boolean computeFormula) {
        this.coordinates = new CellCoordinate(column, row);
        updateCell(content, computeFormula);
    }

    /**
     * Update the Cell by passing a new content. Parses the Cell and creates the
     * determined CellContent and CellValue
     *
     * @param content
     * @param computeFormula
     */
    public void updateCell(String content, boolean computeFormula) {
        TypeOfContent typeOfContent = parseContent(content);
        if (typeOfContent == TypeOfContent.FORMULA) {
            this.cellcontent = new ContentFormula(content);
            if (computeFormula) {
                addAllObservers();
                this.value = new ValueNumber((ContentFormula) this.cellcontent);
            }else{
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
     * @param content
     * @return
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
     * Compute value of the Cell without updating the Content. Used when
     * Updating the whole SpreadSheet to recompute Values after modifying cells
     * refered in formulas.
     */
    public void recomputeValue() {
        if (this.cellcontent instanceof ContentFormula) {
            this.value = new ValueNumber((ContentFormula) this.cellcontent);
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
        System.out.println("Cell " + this.coordinates.print() + " is " + this.cellcontent.getType() + " with value " + printValue());
    }

    /**
     * Returns the Value of the Cell as String
     *
     * @return
     */
    public String printValue() {
        return value.print();
    }

    /**
     * Returns the Type of Content of the Cell
     *
     * @return
     */
    public TypeOfContent getType_of_content() {
        return cellcontent.getType();
    }

    /**
     * Returns the content of the Cell.
     *
     * @return
     */
    public String getContent() {
        return this.cellcontent.getContent();
    }

    /**
     * Returns the value of the Cell.
     *
     * @return
     */
    public Value getValue() {
        return this.value;
    }

    /**
     * Used to add all as Observers all cells that this Cell depends on.
     * Only called when adding a new content Formula to the Cell
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
     * @param o
     * @param o1 
     */
    @Override
    public void update(java.util.Observable o, Object o1) {
        System.out.println("UPDATE OBSERVER: called over object "+ o1);
        recomputeValue();
    }
}
