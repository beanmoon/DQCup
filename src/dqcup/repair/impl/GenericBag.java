package dqcup.repair.impl;

import dqcup.repair.RepairedCell;
import dqcup.repair.Tuple;

import java.util.Set;

/**
 * Created by bean on 11/11/15.
 */
public abstract class GenericBag {

    public abstract  void ingest(Tuple tuple);
    public abstract  Set<RepairedCell> repair();

    public boolean isIn(RepairedCell cell, Set<RepairedCell> cells){
        boolean in  = false;
        for(RepairedCell c: cells){
            if(cell.getRowId() == c.getRowId() && cell.getColumnId() == c.getColumnId()){
                in = true;
                break;
            }
        }

        return  in;
    }
}
