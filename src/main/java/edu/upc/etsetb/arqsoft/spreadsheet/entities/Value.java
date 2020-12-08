/*
 *  Project of the ARQSOFT Subject in the MATT Master's Degree.
 *  The goal of the project is to build some of the core components
 *  of a spreadsheet, which can be used through a textual interface.
 *  Developed by Esteve Valls Mascaró
 */
package edu.upc.etsetb.arqsoft.spreadsheet.entities;

/**
 * Interface which groups the different Values that can be found in a Cell.
 *
 * @author estev
 */
public interface Value {

    /**
     *
     * @return
     */
    Object getValue();
}
