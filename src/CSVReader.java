import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class CSVReader {
    public static void main(String[] args) {
        String DeliveriesPath = "src/Resources/deliveries.csv";
        String MatchesPath = "src/Resources/matches.csv";
        String line1 = "";
        int[] years = new int[10];
        for(int i=0;i<10;i++){
            years[i] = 0;
        }
        HashMap<String,Integer> wonMatches = new HashMap<String, Integer>();
        int winCount;
        HashMap<String,Integer> wonToss = new HashMap<String, Integer>();
        int tossCount;


        try {
            BufferedReader br = new BufferedReader(new FileReader(MatchesPath));

            int iteration = 0;
            while((line1 = br.readLine()) != null){
                if(iteration == 0){
                    iteration++;
                    continue;
                }
                String[] values = line1.split(",");
                //Logic for Scenario 1
                int index = parseInt(values[1])-2008;
                years[index]++;

                // Logic for Scenario 2
                if(!wonMatches.containsKey(values[10])){
                    winCount = 1;
                    wonMatches.put(values[10], winCount);
                } else{
                    int count = wonMatches.get(values[10]);
                    count++;
                    wonMatches.put(values[10], count);
                }

                // Logic for Scenario 5
                if(!wonToss.containsKey(values[6])){
                    tossCount = 1;
                    wonToss.put(values[6], tossCount);
                } else{
                    int count = wonToss.get(values[6]);
                    count++;
                    wonToss.put(values[6], count);
                }

            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        System.out.println("------Scenario 1------");
        for(int i=0;i<10;i++){
            System.out.println("Matches played in " + (i+2008)+ " = " + years[i]);
        }
        System.out.println("----------------------");

        System.out.println();

        System.out.println("------Scenario 2------");
        System.out.println(wonMatches);
        System.out.println("----------------------");

        String line2 = "";

        HashMap<String,Integer> ecoBaller = new HashMap<String,Integer>();
        HashMap<String,Integer> extraRun = new HashMap<String, Integer>();
        // int runCount = 0;
        // HashMap<String,Integer> ballerOvers = new HashMap<String, Integer>();


        try {
            BufferedReader rdr = new BufferedReader(new FileReader(DeliveriesPath));

            int itrator = 0;
            while((line2 = rdr.readLine()) != null){
                if(itrator == 0){
                    itrator++;
                    continue;
                }
                String[] val = line2.split(",");

                // Logic for scenario 3
                if(parseInt(val[0]) > 576 && parseInt(val[0]) <= 636){
                    if(!extraRun.containsKey(val[2])){
                        int run = parseInt(val[16]);
                        extraRun.put(val[2],run);
                    }else {
                        int counter = extraRun.get(val[2]);
                        counter = counter + parseInt(val[16]);
                        extraRun.put(val[2],counter);
                    }
                }
                //int ballCount=0;
                // Logic for scenario 4
                if(parseInt(val[0]) >= 518 && parseInt(val[0]) <= 576){
                    if(!ecoBaller.containsKey(val[8])){
                        int eco = parseInt(val[17]);
                        ecoBaller.put(val[8],eco);
                    }else {
                        int ecoCount = ecoBaller.get(val[8]);
                        ecoCount = ecoCount + parseInt(val[17]);
                        ecoBaller.put(val[8],ecoCount);
                    }
//                    if(!ballerOvers.containsKey(val[8])){
//                        int ball =  1;
//                        ballerOvers.put(val[8],ball);
//                    } else {
//                        int ballCount=ballerOvers.get(val[8]);
//                        ballCount = ballCount + 1;
//                        ballerOvers.put(val[8],ballCount);
//                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println();

        System.out.println("------Scenario 3------");
        System.out.println(extraRun);
        System.out.println("----------------------");

        System.out.println();

        System.out.println("------Scenario 4------");
        //System.out.println(ecoBaller);
        int minValue = Collections.min(ecoBaller.values());
        //System.out.println(ballerOvers);
        for(Map.Entry<String, Integer> entry: ecoBaller.entrySet()) {
            if(entry.getValue() == minValue) {
                System.out.println("Top Economical baller is " + entry.getKey());
                break;
            }
        }
        System.out.println("----------------------");

        System.out.println();

        System.out.println("------Scenario 5------");
        System.out.println("Number of toss won by each team in all years of IPL");
        System.out.println(wonToss);
        System.out.println("----------------------");


    }
}
