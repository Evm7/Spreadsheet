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

    public CellImpl(int row, int column, String content) {
        TypeOfContent typeOfContent = parseContent(content);
        if (typeOfContent == TypeOfContent.FORMULA) {
            this.cellcontent = new ContentFormula(content);
        }
        else if(typeOfContent == TypeOfContent.NUMBER){
            this.cellcontent = new ContentNumber(content);
        }
        else if(typeOfContent == TypeOfContent.TEXT){
            this.cellcontent = new ContentText(content);
        }
        else{
            this.cellcontent = new ContentText("");
        }
    }

    public TypeOfContent parseContent(String content) {
        if (content.equals("")){
            return TypeOfContent.EMPTY;
        }
        else if (content.startsWith("=")) {
            return TypeOfContent.FORMULA;
        } else {

            try {
                Float num = Float.parseFloat(content);
                return TypeOfContent.NUMBER;
            } catch (NumberFormatException e) {
                return TypeOfContent.TEXT;
            }
        }
    }

    public void show() {
        System.out.println("Cell " + this.coordinates.print() + " is" + this.cellcontent.getType()+ ".");
    }

    public String printValue() {
        return "_";
    }

    public String getSource() {
        return "_";
    }

    public TypeOfContent getType_of_content() {
        return cellcontent.getType();
    }
}
