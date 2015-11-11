package dqcup.repair.impl;

import dqcup.repair.RepairedCell;
import dqcup.repair.Tuple;
import java.util.*;
import static dqcup.repair.impl.Index.*;

public class GlobalBag {
    private boolean debug = false;
    private ZipBag zipBag = new ZipBag();
    private StateBag stateBag = new StateBag();
    private ApmtBag apmtBag = new ApmtBag();
    private Apmt2Bag apmt2Bag = new Apmt2Bag();


    public void ingest(Tuple tuple){
        zipBag.ingest(tuple);
        stateBag.ingest(tuple);
        apmt2Bag.ingest(tuple);
        apmtBag.ingest(tuple);
    }

    public Set<RepairedCell> repair(){
        Set<RepairedCell> rst = new HashSet<RepairedCell>();
        rst.addAll(zipBag.repair());
        Set<RepairedCell> stateRst = stateBag.repair();
        rst.addAll(stateBag.repair());
        rst.addAll(apmtBag.repair());
        rst.addAll(apmt2Bag.repair());

        return rst;
    }
}



class ZipBag {

    private Map<String, HashMap<String, List<String>>> zipMap = new HashMap<String, HashMap<String, List<String>>>();
    List<Tuple> zipList = new ArrayList<Tuple>();

    public void ingest(Tuple tuple){
        String ruid = tuple.getValue(RUID);
        String fname = tuple.getValue(FNAME);
        String minit = tuple.getValue(MINIT);
        String lname = tuple.getValue(LNAME);
        String zip = tuple.getValue(ZIP);

        if(!RegexUtil.isZipValid(zip)){
            zipList.add(tuple);
        }

        String key = fname+":"+minit+":"+lname;

        HashMap<String, List<String>> map = null;
        if(zipMap.containsKey(key)){
            map = zipMap.get(key);
            if(map.containsKey(zip)){
                map.get(zip).add(ruid);
            }else{
                List<String> list = new ArrayList<String>();
                list.add(ruid);
                map.put(zip, list);
            }
        }else{
            map = new HashMap<String, List<String>>();
            List<String> list = new ArrayList<String>();
            list.add(ruid);
            map.put(zip,list);
            zipMap.put(key, map);
        }
    }

    public Set<RepairedCell> repair(){

        Set<RepairedCell> rst = new HashSet<RepairedCell>();
        for(String name: zipMap.keySet()){
            int max = 0;
            String maxZip = null;
            Map<String, List<String>> nameMap = zipMap.get(name);

            for(String zipValue : nameMap.keySet()){
                List<String> list = nameMap.get(zipValue);
                if(list.size() > max ){
                    max = list.size();
                    maxZip = zipValue;
                }
            }

            for(String zipValue : nameMap.keySet()){
                if(!zipValue.equals(maxZip)){
                    List<String> ruids = nameMap.get(zipValue);
                    for(String ruid : ruids){
                        if(RegexUtil.isZipValid(maxZip))
                            rst.add(new RepairedCell(Integer.valueOf(ruid), attrs[ZIP], maxZip));
                    }
                }
            }
        }

        for(Tuple tuple: zipList){
            RepairedCell newCell = new RepairedCell(Integer.valueOf(tuple.getValue(RUID)), attrs[ZIP], tuple.getValue(ZIP));
            if(!isIn(newCell, rst))
                rst.add(newCell);
        }
        return rst;
    }

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

class StateBag {
    Map<String, Map<String, List<String>>> zipMap = new HashMap<String, Map<String, List<String>>>();
    List<Tuple> lowerLists = new ArrayList<Tuple>();

    public void ingest(Tuple tuple){
        String ruid = tuple.getValue(RUID);
        String zip = tuple.getValue(ZIP);
        String apmt = tuple.getValue(APMT);
        String state = tuple.getValue(STATE);

        if(!RegexUtil.isStateValid(state)){
            boolean lowerCase = false;
            for(char c : state.toCharArray()){
                if(Character.isLowerCase(c)) {
                    lowerCase = true;
                    break;
                }
            }

            if(lowerCase){
                lowerLists.add(tuple);
            }
        }


        zip += apmt;

        Map<String, List<String>> stateMap = null;
        if(zipMap.containsKey(zip)){
            stateMap = zipMap.get(zip);
            if(stateMap.containsKey(state)){
                stateMap.get(state).add(ruid);
            }else{
                List<String> list = new ArrayList<String>();
                list.add(ruid);
                stateMap.put(state,list);
            }
        } else {
            stateMap = new HashMap<String, List<String>>();
            List<String> list = new ArrayList<String>();
            list.add(ruid);
            stateMap.put(state, list);
            zipMap.put(zip, stateMap);
        }
    }

    public Set<RepairedCell> repair(){
        Set<RepairedCell> rst = new HashSet<RepairedCell>();
        for(String zip: zipMap.keySet()){
            int max = -1;
            String maxState = null;

            Map<String, List<String>> stateMap = zipMap.get(zip);

            if(stateMap.size() == 1)
                continue;

            for(String state : stateMap.keySet()){
                List<String> list = stateMap.get(state);
                if(list.size() > max){
                    max = list.size();
                    maxState = state;
                }
            }

            for(String state : stateMap.keySet()){
                if(!state.equals(maxState.toUpperCase())){
                    List<String> ruids = stateMap.get(state);
                    for(String ruid : ruids){
                        if(RegexUtil.isStateValid(maxState))
                            rst.add(new RepairedCell(Integer.valueOf(ruid), attrs[STATE], maxState));
                    }
                }
            }
        }

        for(Tuple tuple: lowerLists){
            rst.add(new RepairedCell(Integer.valueOf(tuple.getValue(RUID)), attrs[STATE], tuple.getValue(STATE).toUpperCase()));
        }
        return rst;
    }

}


class ApmtBag {
    Map<String, Map<String, List<String>>> fnameMap = new HashMap<String, Map<String, List<String>>>();

    public void ingest(Tuple tuple){
        String ruid = tuple.getValue(RUID);
        String fname = tuple.getValue(FNAME);
        String stnum = tuple.getValue(STNUM);
        String apmt = tuple.getValue(APMT);

        Map<String, List<String>> apmtMap = null;
        fname += stnum;

        if(fnameMap.containsKey(fname)){
            apmtMap = fnameMap.get(fname);
            if(apmtMap.containsKey(apmt)){
                apmtMap.get(apmt).add(ruid);
            }else{
                List<String> list = new ArrayList<String>();
                list.add(ruid);
                apmtMap.put(apmt,list);
            }
        } else {
            apmtMap = new HashMap<String, List<String>>();
            List<String> list = new ArrayList<String>();
            list.add(ruid);
            apmtMap.put(apmt, list);
            fnameMap.put(fname, apmtMap);
        }
    }

    public Set<RepairedCell> repair(){
        Set<RepairedCell> rst = new HashSet<RepairedCell>();
        for(String fname: fnameMap.keySet()){
            int max = -1;
            String maxApmt = null;

            Map<String, List<String>> apmtMap = fnameMap.get(fname);

            if(apmtMap.size() == 1)
                continue;

            for(String apmt : apmtMap.keySet()){
                List<String> list = apmtMap.get(apmt);
                if(list.size() > max){
                    max = list.size();
                    maxApmt = apmt;
                }
            }

            for(String apmt : apmtMap.keySet()){
                List<String> ruids = apmtMap.get(apmt);
                if(!apmt.equals(maxApmt) && ruids.size() < max){
                    for(String ruid : ruids){
                        rst.add(new RepairedCell(Integer.valueOf(ruid), attrs[APMT], maxApmt));
                    }
                }
            }
        }
        return rst;
    }

}

class Apmt2Bag {
    public Set<RepairedCell> rst = new HashSet<RepairedCell>();

    public String swap(String str){
        char[] charArr = str.toCharArray();
        if(charArr.length == 0)
            return str;

        int index = -1;
        for(int i = 0; i < charArr.length; i++){
            if(Character.isLetter(charArr[i]))
                index = i;
        }

        if(index == 0 || index == 2){
            char c = charArr[index];
            charArr[index] = charArr[1];
            charArr[1] = c;
        }

        charArr[1] = Character.toLowerCase(charArr[1]);

        return String.copyValueOf(charArr);
    }

    public void ingest(Tuple tuple){
        String ruid = tuple.getValue(RUID);
        String apmt = new String(tuple.getValue(APMT));
        String stadd = new String(tuple.getValue(STADD));

        if(!RegexUtil.isApmtValid(apmt, stadd)) {
            if (!RegexUtil.isApmtValid(apmt, stadd)) {
                String swapStr = swap(apmt);
                if (!swapStr.equals(apmt))
                    rst.add(new RepairedCell(Integer.valueOf(ruid), attrs[APMT], swapStr));
            }
        }
    }

    public Set<RepairedCell> repair(){
        return rst;
    }
}
