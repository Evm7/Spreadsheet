/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

/**
 * Super Class containing the Content of the Cell. Each Cell shall contain a
 * Content. The Content of the Cell is exactly the input of the user to that
 * cell.
 *
 * @author estev
 */
public class CellContent {

    private TypeOfContent type_of_content;
    private String content;

    /**
     * Constructor of the class which contains the type_of_content and the
     * String of the content inserted by the user
     *
     * @param type_of_content
     * @param content
     */
    public CellContent(TypeOfContent type_of_content, String content) {
        this.type_of_content = type_of_content;
        this.content = content;
    }
    
    @Override
    public String toString(){
        return this.content;
    }

    /**
     * Getter of the Type of Content
     *
     * @return
     */
    public TypeOfContent getType() {
        return type_of_content;
    }

    /**
     * Getter of the Content
     *
     * @return
     */
    public String getContent() {
        return content;
    }

}
