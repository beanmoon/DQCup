package dqcup.repair.test;

import dqcup.repair.RepairedCell;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TestUtil {
    public static Set<RepairedCell> readTruth(String fileRoute) {
        File file = new File(fileRoute);
        Set<RepairedCell> truth = new HashSet<RepairedCell>();

        if (!file.exists()) {
            System.out.println(fileRoute + "文件不存在,无法测试！");
            return truth;
        }
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String line = null;
            while (null != (line = br.readLine())) {
                String[] paras = line.split(",");
                RepairedCell cell = null;
                if (paras.length == 2) {
                    cell = new RepairedCell(Integer.parseInt(paras[0]), paras[1], "");
                } else {
                    cell = new RepairedCell(Integer.parseInt(paras[0]), paras[1], paras[2]);
                }
                truth.add(cell);
            }

            br.close();
            fr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return truth;
    }

    public static double findAccuracy(Set<RepairedCell> truth, Set<RepairedCell> found) {
        if (found.size() != 0) {
            HashMap<Integer, HashSet<String>> foundMap = new HashMap<Integer, HashSet<String>>();

            for (RepairedCell cell : found) {
                HashSet<String> columnIds = null;
                if (foundMap.get(cell.getRowId()) == null) {
                    columnIds = new HashSet<String>();
                } else {
                    columnIds = foundMap.get(cell.getRowId());
                }
                columnIds.add(cell.getColumnId());
                foundMap.put(cell.getRowId(), columnIds);
            }


            int tAndF = 0;
            for (RepairedCell cell : truth) {
                if (foundMap.get(cell.getRowId()) != null) {
                    if (foundMap.get(cell.getRowId()).contains(cell.getColumnId())) {
                        tAndF++;
                    }

                }
            }
            double precision = tAndF * 1.0 / found.size(), recall = tAndF * 1.0 / truth.size();
            System.out.println("find precision = " + precision + ", find recall = " + recall);
            return 2 * precision * recall / (precision + recall);
        }
        return 0;
    }

    public static double repairAccuracy(Set<RepairedCell> truth, Set<RepairedCell> found) {
        if (found.size() != 0) {
            Set<RepairedCell> tAndF = new HashSet<RepairedCell>();
            tAndF.addAll(truth);
            tAndF.retainAll(found);
            double precision = tAndF.size() * 1.0 / found.size(), recall = tAndF.size() * 1.0 / truth.size();

            System.out.println("repair precision = " + precision + ", repair recall = " + recall);
            return 2 * precision * recall / (precision + recall);
        }
        return 0;
    }

    public static Set<RepairedCell> getUnfound(Set<RepairedCell> truth, Set<RepairedCell> found) {
        Set<RepairedCell> rst = new HashSet<RepairedCell>();
        if (found.size() != 0) {
            HashMap<Integer, HashSet<String>> foundMap = new HashMap<Integer, HashSet<String>>();

            for (RepairedCell cell : found) {
                HashSet<String> columnIds = null;
                if (foundMap.get(cell.getRowId()) == null) {
                    columnIds = new HashSet<String>();
                } else {
                    columnIds = foundMap.get(cell.getRowId());
                }
                columnIds.add(cell.getColumnId());
                foundMap.put(cell.getRowId(), columnIds);
            }

            for (RepairedCell cell : truth) {
                if (foundMap.get(cell.getRowId()) == null || !foundMap.get(cell.getRowId()).contains(cell.getColumnId())) {
                    rst.add(cell);
                }
            }
        }
        return rst;
    }

    public static Set<RepairedCell> getFoundFalse(Set<RepairedCell> truth, Set<RepairedCell> found) {
        Set<RepairedCell> rst = new HashSet<RepairedCell>();
        if (truth.size() != 0) {
            HashMap<Integer, HashSet<String>> truthMap = new HashMap<Integer, HashSet<String>>();

            for (RepairedCell cell : truth) {
                HashSet<String> columnIds = null;
                if (truthMap.get(cell.getRowId()) == null) {
                    columnIds = new HashSet<String>();
                } else {
                    columnIds = truthMap.get(cell.getRowId());
                }
                columnIds.add(cell.getColumnId());
                truthMap.put(cell.getRowId(), columnIds);
            }

            for (RepairedCell cell : found) {
                if (truthMap.get(cell.getRowId()) == null || !truthMap.get(cell.getRowId()).contains(cell.getColumnId())) {
                    rst.add(cell);
                }
            }
        }
        return rst;
    }
}
