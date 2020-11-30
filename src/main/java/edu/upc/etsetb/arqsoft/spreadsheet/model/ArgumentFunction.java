/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Term;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author estev
 */
public class ArgumentFunction extends Argument {

    public ArgumentFunction(String arg) {
        super(arg);    // Example:  different Arguments separated by ; --> 3;4;5;A1:3B

    }

    @Override
    public OperandFunction getValue() {
        List<Term> example = new LinkedList<Term>();
        example.add(new OperandNumber(4.0));
        example.add(new OperandNumber(1.0));
        example.add(new OperandNumber(2.0));
        System.out.println("4;1;2");
        return new OperandFunction(example);
    }

    @Override
    public String getSource() {
        return arg;
    }

}
