package dqcup.repair.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;

import dqcup.repair.RepairedCellSortable;

public class MyTestUtil {
	public static void sort() {
		List<RepairedCellSortable> list = new ArrayList<RepairedCellSortable>();
		File f = new File("input/Truth-easy.txt");
		
		FileReader fileReader;
		try {
			fileReader = new FileReader(f);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String buffer = null;
			while((buffer = bufferedReader.readLine()) != null){
				String[] params = buffer.split(",");
				RepairedCellSortable cell = null;
				if(params.length == 2){
					cell = new RepairedCellSortable(Integer.valueOf(params[0]), params[1], "");
				}else{
					cell = new RepairedCellSortable(Integer.valueOf(params[0]), params[1], params[2]);
				}
				
				list.add(cell);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Collections.sort(list);
		for(RepairedCellSortable cell : list){
			System.out.println(cell.toString());
		}
	}
	
	public static void main(String[] args) {
		sort();
	}
}
