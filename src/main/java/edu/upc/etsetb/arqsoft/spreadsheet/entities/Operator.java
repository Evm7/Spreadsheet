/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.entities;

/**
 *
 * @author estev
 */
public interface Operator {
    Operant computeOperation(Operant arg1, Operant arg2);
    String getSign();
    int getWeight();

}
