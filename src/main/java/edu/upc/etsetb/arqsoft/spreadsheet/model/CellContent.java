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
public class CellContent {
    private TypeOfContent type_of_content;
    private String content;

    public CellContent(TypeOfContent type_of_content, String content) {
        this.type_of_content = type_of_content;
        this.content = content;
    }
    
    public TypeOfContent getType(){
        return type_of_content;
    }
    
    public String getContent(){
        return content;
    }
        
}
