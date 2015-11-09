package dqcup.repair.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.DebugGraphics;

import dqcup.repair.DatabaseRepair;
import dqcup.repair.DbFileReader;
import dqcup.repair.RepairedCell;
import dqcup.repair.Tuple;
import dqcup.repair.test.TestUtil;
import static dqcup.repair.impl.Index.*;

public class DatabaseRepairImpl implements DatabaseRepair {
	private boolean debug = false;

	@Override
	public Set<RepairedCell> repair(String fileRoute) {
		//Please implement your own repairing methods here.
		LinkedList<Tuple> tuples = DbFileReader.readFile(fileRoute);
		Map<String,PersonalBag> personalBagMap = new HashMap<String, PersonalBag>();
		
		for(Tuple tuple: tuples){
			String cuid = tuple.getValue(CUID);
			
			if(personalBagMap.containsKey(cuid)){
				personalBagMap.get(cuid).ingest(tuple);
			}else{
				PersonalBag personalBag = new PersonalBag(cuid);
				personalBagMap.put(cuid, personalBag);
			}
		}
		
		if(debug){
			System.out.println("tuples' size = " + tuples.size());
			System.out.println("personalBagMap's size = " + personalBagMap.size());
		}
		List<String> rst = new ArrayList<String>();
		Iterator it = personalBagMap.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pair = (Map.Entry)it.next();
			PersonalBag personalBag = (PersonalBag)pair.getValue();
			rst.addAll(personalBag.repair());
		}
		
		if(debug){
			System.out.println("rst's size = " + rst.size());
			for(String str : rst){
				System.out.println(str);
			}
		}
			
		
		Set<RepairedCell> truth = TestUtil.readTruth("input/Truth-easy.txt");
		
		
		
		HashSet<RepairedCell> result = new HashSet<RepairedCell>();
		
		return result;
	}
	
	public void setDebug(boolean debug){
		this.debug = debug;
	}

	public static void main(String[] args) {
		DatabaseRepairImpl impl = new DatabaseRepairImpl();
		impl.setDebug(true);
		impl.repair("input/DB-easy.txt");
	}
}
