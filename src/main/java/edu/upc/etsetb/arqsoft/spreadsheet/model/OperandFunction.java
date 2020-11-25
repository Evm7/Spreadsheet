/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import edu.upc.etsetb.arqsoft.spreadsheet.entities.Operand;

/**
 *
 * @author estev
 */
public class OperandFunction implements Operand {

    Operand[] operants;

    public OperandFunction(Operand[] operants) {
        this.operants = operants;
    }

    public OperandNumber[] getValue() {
        return flatten(this);
    }
    
    private OperandNumber[] flatten(OperandFunction fun){
        List<OperandNumber> flatList = new ArrayList<OperandNumber>();
        for(Operand op : fun.operants){
            if(op instanceof OperandNumber){
                flatList.add((OperandNumber) op);
            }else{
                flatList.addAll(Arrays.asList(flatten((OperandFunction) op)));
            }
        }
        return flatList.toArray(new OperandNumber[flatList.size()]);
    }



    @Override
    public String print() {
        String res = "[";
        for (Operand op : operants) {
            res = res + op.print() + " ; ";
        }
        return res + "]";
    }

}
