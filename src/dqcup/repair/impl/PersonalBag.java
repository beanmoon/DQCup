package dqcup.repair.impl;

import dqcup.repair.RepairedCell;
import dqcup.repair.Tuple;

import java.util.*;

import static dqcup.repair.impl.Index.*;

public class PersonalBag {	
	private String cuid;
	private static String[] attrs = "RUID:CUID:SSN:FNAME:MINIT:LNAME:STNUM:STADD:APMT:CITY:STATE:ZIP".split(":");
	
	private List<HashMap<String, ArrayList<String>>> mapList = new ArrayList<HashMap<String, ArrayList<String>>>(); 
	
	public PersonalBag(String cuid) {
		this.cuid = cuid;
		for(int i = RUID; i <= ZIP; i++){
			mapList.add(new HashMap<String, ArrayList<String>>());
		}
	}
	
	public void ingest(Tuple tuple){
		String ruid = tuple.getValue(RUID);
		
		for(int i = SSN; i <= ZIP; i++){
			String value = tuple.getValue(i);
			if(mapList.get(i).containsKey(value)){
				mapList.get(i).get(value).add(ruid);
			} else {
				ArrayList<String> list = new ArrayList<String>();
				list.add(ruid);
				mapList.get(i).put(value, list);				
			}
		}		
	}
	
	public Set<RepairedCell> repair(){
		Set<RepairedCell> rst = new HashSet<RepairedCell>();
		
		for(int i = SSN; i <= ZIP; i++){
			Map<String, ArrayList<String>> map = mapList.get(i);
			Iterator it = map.entrySet().iterator();
			
			int max = 0;
			String maxKey = null;
			
			while(it.hasNext()){
				Map.Entry<String, ArrayList<String>> pair = (Map.Entry<String, ArrayList<String>>)it.next();
				String key = pair.getKey();
				List<String> list = pair.getValue();
				
				if(list.size() > max){
					max = list.size();
					maxKey = key;
				}				
			}
			
			
			for(String key : map.keySet()){
				if(!key.equals(maxKey)){
					ArrayList<String> ruids = map.get(key);
					for(String ruid: ruids){
						rst.add(new RepairedCell(Integer.valueOf(ruid), attrs[i], maxKey));
					}
				}					
			}			
		}
		
		
		return rst;
	}
}


