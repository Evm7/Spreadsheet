/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

/**
 *
 * @author estev
 */
public class CellImpl {

    /**
     * @param args the command line arguments
     */
    protected CellCoordinate coordinates;
    protected CellContent cellcontent;
    protected CellValue value;

    public CellImpl(int column, int row, String content) {
        this.coordinates = new CellCoordinate(column, row);
        updateCell(content, true);
    }

    public CellImpl(int column, int row, String content, boolean computeFormula) {
        this.coordinates = new CellCoordinate(column, row);
        updateCell(content, computeFormula);
    }

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

    public void show() {
        System.out.println("Cell " + this.coordinates.print() + " is " + this.cellcontent.getType() + " with value " + printValue());
    }

    public String printValue() {
        return "" + value.getValue();
    }

    public TypeOfContent getType_of_content() {
        return cellcontent.getType();
    }

    public String getContent() {
        return this.cellcontent.getContent();
    }
}
