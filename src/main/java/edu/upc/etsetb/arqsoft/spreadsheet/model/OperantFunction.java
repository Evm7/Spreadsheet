/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.etsetb.arqsoft.spreadsheet.model;

import edu.upc.etsetb.arqsoft.spreadsheet.entities.Operant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 *
 * @author estev
 */
public class OperantFunction implements Operant {

    Operant[] operants;

    public OperantFunction(Operant[] operants) {
        this.operants = operants;
    }

    public OperantNumber[] getValue() {
        return flatten(this);
    }
    
    private OperantNumber[] flatten(OperantFunction fun){
        List<OperantNumber> flatList = new ArrayList<OperantNumber>();
        for(Operant op : fun.operants){
            if(op instanceof OperantNumber){
                flatList.add((OperantNumber) op);
            }else{
                flatList.addAll(Arrays.asList(flatten((OperantFunction) op)));
            }
        }
        return flatList.toArray(new OperantNumber[flatList.size()]);
    }



    @Override
    public String print() {
        String res = "[";
        for (Operant op : operants) {
            res = res + op.print() + " ; ";
        }
        return res + "]";
    }

}
