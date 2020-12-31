/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

/**
 *
 * @author estev
 */
public class ContentText extends CellContent{
    
    /**
     * Constructor of the Content Text of the Cell
     * @param text Content of the cell
     */
    public ContentText(String text) {
        super(TypeOfContent.TEXT, text);
        
    }
}
