/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import java.util.List;
import java.util.Map;

/**
 *
 * @author estev
 */
public class CellImpl {

    /**
     */
    protected CellCoordinate coordinates;

    /**
     *
     */
    protected CellContent cellcontent;

    /**
     *
     */
    protected CellValue value;

    /**
     *
     * @param column
     * @param row
     * @param content
     */
    public CellImpl(int column, int row, String content) {
        this.coordinates = new CellCoordinate(column, row);
        updateCell(content, true);
    }

    /**
     *
     * @param column
     * @param row
     * @param content
     * @param computeFormula
     */
    public CellImpl(int column, int row, String content, boolean computeFormula) {
        this.coordinates = new CellCoordinate(column, row);
        updateCell(content, computeFormula);
    }

    /**
     *
     * @param content
     * @param computeFormula
     */
    public void updateCell(String content, boolean computeFormula) {
        TypeOfContent typeOfContent = parseContent(content);
        if (typeOfContent == TypeOfContent.FORMULA) {
            this.cellcontent = new ContentFormula(content);
            if (computeFormula) {
                this.value = new ValueNumber((ContentFormula) this.cellcontent);
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
    }

    /**
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
     *
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
    }

    /**
     *
     */
    public void show() {
        System.out.println("Cell " + this.coordinates.print() + " is " + this.cellcontent.getType() + " with value " + printValue());
    }

    /**
     *
     * @return
     */
    public String printValue() {
        return "" + value.getValue();
    }

    /**
     *
     * @return
     */
    public TypeOfContent getType_of_content() {
        return cellcontent.getType();
    }

    /**
     *
     * @return
     */
    public String getContent() {
        return this.cellcontent.getContent();
    }
}
