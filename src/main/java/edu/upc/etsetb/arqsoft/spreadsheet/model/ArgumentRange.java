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
public class ArgumentRange extends Argument {


    public ArgumentRange(String arg) {
        super(arg);// When the argument refers to more than one cell in a range --> ex:  A1:B3
    }

    @Override
    public OperandFunction getValue() {
        List<Term> example = new LinkedList<Term>();
        example.add(new OperandNumber(3.0));
        example.add(new OperandNumber(6.0));
        example.add(new OperandNumber(12.0));
        System.out.println("3;6;12");
        return new OperandFunction(example);
    }

    @Override
    public String getSource() {
        return arg;
    }

}
