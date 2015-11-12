package dqcup.repair.test;

import dqcup.repair.DatabaseRepair;
import dqcup.repair.DbFileReader;
import dqcup.repair.RepairedCell;
import dqcup.repair.Tuple;
import dqcup.repair.impl.DatabaseRepairImpl;

import java.util.*;

public class Test {
    public static boolean debug = true;

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        long startTime = 0, endTime = 0, totalTime = 0;
        double avgFindAccuracy = 0, avgRepairAccuracy = 0;
        Set<RepairedCell> found, truth;

        DatabaseRepair dr = new DatabaseRepairImpl();
        truth = TestUtil.readTruth("input/Truth-easy.txt");
        if (truth.size() != 0) {
            startTime = System.currentTimeMillis();
            found = dr.repair("input/DB-easy.txt");
            endTime = System.currentTimeMillis();

            double findAccuracy = TestUtil.findAccuracy(truth, found);
            double repairAccuracy = TestUtil.repairAccuracy(truth, found);
            System.out.println("easy-Time:" + (endTime - startTime));
            System.out.println("easy-Find Accuracy:" + findAccuracy);
            System.out.println("easy-Repair Accuracy:" + repairAccuracy);
            totalTime += (endTime - startTime);
            avgFindAccuracy += findAccuracy;
            avgRepairAccuracy += repairAccuracy;

            if (debug) {
                Set<RepairedCell> rest = TestUtil.getUnfound(truth, found);
                if (rest.size() > 0) {
                    System.out.println("There's still " + rest.size() + " instances not found!");
                    getPattern(rest);
                }


                Set<RepairedCell> foundFalseSet = TestUtil.getFoundFalse(truth, found);
                if (foundFalseSet.size() > 0) {
                    System.out.println("There's " + foundFalseSet.size() + " instances wrongly found!");
                    for (RepairedCell cell : foundFalseSet) {
                        System.out.println(cell);
                    }
                }

                Set<RepairedCell> duplicateSet = TestUtil.getDuplicates(found);
                if (duplicateSet.size() > 0) {
                    System.out.println("There's " + duplicateSet.size() + " duplicate instances!");
                    for (RepairedCell cell : duplicateSet) {
                        System.out.println(cell);
                    }
                }

                Set<RepairedCell> repairFalseSet = TestUtil.getRepairFalse(truth, found);
                if(repairFalseSet.size() > 0){
                    System.out.println("There's " + repairFalseSet.size() + " instances wrongly repaired!");
                    for(RepairedCell cell : repairFalseSet){
                        System.out.println(cell);
                    }
                }
            }
        }

        dr = new DatabaseRepairImpl();
        truth = TestUtil.readTruth("input/Truth-normal.txt");
        if (truth.size() != 0) {
            startTime = System.currentTimeMillis();
            found = dr.repair("input/DB-normal.txt");
            endTime = System.currentTimeMillis();

            double findAccuracy = TestUtil.findAccuracy(truth, found);
            double repairAccuracy = TestUtil.repairAccuracy(truth, found);
            System.out.println("normal-Time:" + (endTime - startTime));
            System.out.println("normal-Find Accuracy:" + findAccuracy);
            System.out.println("normal-Repair Accuracy:" + repairAccuracy);
            totalTime += (endTime - startTime);
            avgFindAccuracy += findAccuracy;
            avgRepairAccuracy += repairAccuracy;
        }

        dr = new DatabaseRepairImpl();
        truth = TestUtil.readTruth("input/Truth-hard.txt");
        if (truth.size() != 0) {
            startTime = System.currentTimeMillis();
            found = dr.repair("input/DB-hard.txt");
            endTime = System.currentTimeMillis();

            double findAccuracy = TestUtil.findAccuracy(truth, found);
            double repairAccuracy = TestUtil.repairAccuracy(truth, found);
            System.out.println("hard-Time:" + (endTime - startTime));
            System.out.println("hard-Find Accuracy:" + findAccuracy);
            System.out.println("hard-Repair Accuracy:" + repairAccuracy);
            totalTime += (endTime - startTime);
            avgFindAccuracy += findAccuracy;
            avgRepairAccuracy += repairAccuracy;
        }

        System.out.println("Total Time:" + totalTime);
        System.out.println("Average Find Accuracy:" + avgFindAccuracy / 3);
        System.out.println("Average Repair Accuracy:" + avgRepairAccuracy / 3);
    }


    public static void getPattern(Set<RepairedCell> truth) {
        LinkedList<Tuple> tuples = DbFileReader.readFile("input/DB-easy.txt");
        Map<String, List<Tuple>> tupleMap = new HashMap<String, List<Tuple>>();
        Map<Integer, String> idMap = new HashMap<Integer, String>();
        for (Tuple tuple : tuples) {
            int ruid = Integer.valueOf(tuple.getValue("RUID"));
            String cuid = tuple.getValue("CUID");

            idMap.put(ruid, cuid);

            if (tupleMap.containsKey(cuid)) {
                tupleMap.get(cuid).add(tuple);
            } else {
                List<Tuple> list = new ArrayList<Tuple>();
                list.add(tuple);
                tupleMap.put(cuid, list);
            }
        }

        for (RepairedCell cell : truth) {
            int ruid = cell.getRowId();
            List<Tuple> tupleList = tupleMap.get(idMap.get(ruid));
            System.out.println(cell.toString());
            if (tupleList != null) {
                for (Tuple tuple : tupleList) {
                    System.out.println(tuple.toString(""));
                }
            }
            System.out.println("----------------------------------------------------------------");

        }

        System.out.println(tupleMap.size());
    }

}
