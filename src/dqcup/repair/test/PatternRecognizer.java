package dqcup.repair.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dqcup.repair.DbFileReader;
import dqcup.repair.RepairedCell;
import dqcup.repair.Tuple;

public class PatternRecognizer {

	public static void getPattern(){
		LinkedList<Tuple> tuples = DbFileReader.readFile("input/DB-easy.txt");
		Map<String, List<Tuple>> tupleMap = new HashMap<String, List<Tuple>>();
		Map<Integer, String> idMap = new HashMap<Integer, String>();
		for(Tuple tuple: tuples){
			int ruid = Integer.valueOf(tuple.getValue("RUID"));
			String cuid = tuple.getValue("CUID");
			
			idMap.put(ruid, cuid);
			
			if(tupleMap.containsKey(cuid)){
				tupleMap.get(cuid).add(tuple);
			}else{
				List<Tuple> list = new ArrayList<Tuple>();
				list.add(tuple);
				tupleMap.put(cuid, list);
			}
		}
		
		Set<RepairedCell> truth = TestUtil.readTruth("input/Truth-easy.txt");
		for(RepairedCell cell: truth){
			int ruid = cell.getRowId();
			List<Tuple> tupleList = tupleMap.get(idMap.get(ruid));
			System.out.println(cell.toString());
			if(tupleList != null){
				for(Tuple tuple: tupleList){
					System.out.println(tuple.toString(""));
				}
			}
			System.out.println("----------------------------------------------------------------");
			
		}
		
		System.out.println(tupleMap.size());
	}
	
	
	public static void repair(){
		LinkedList<Tuple> orgTuples = DbFileReader.readFile("input/DB-easy.txt");
		Map<String, List<Tuple>> tupleMap = new HashMap<String, List<Tuple>>();
		Map<Integer, String> idMap = new HashMap<Integer, String>();
		for(Tuple tuple: orgTuples){
			int ruid = Integer.valueOf(tuple.getValue("RUID"));
			String cuid = tuple.getValue("CUID");
			
			idMap.put(ruid, cuid);
			
			if(tupleMap.containsKey(cuid)){
				tupleMap.get(cuid).add(tuple);
			}else{
				List<Tuple> list = new ArrayList<Tuple>();
				list.add(tuple);
				tupleMap.put(cuid, list);
			}
		}
		
		
		Set<RepairedCell> truth = TestUtil.readTruth("input/Truth-easy.txt");
		for(RepairedCell cell: truth){
			int ruid = cell.getRowId();
			List<Tuple> tupleList = tupleMap.get(idMap.get(ruid));
			System.out.println(cell.toString());
			if(tupleList != null){
				for(Tuple tuple: tupleList){
					System.out.println(tuple.toString(""));
				}
			}
			System.out.println("----------------------------------------------------------------");
			
		}
		
		System.out.println(tupleMap.size());
	}
	
	public static void main(String[] args) {
		getPattern();
	}
}
