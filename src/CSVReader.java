import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import static java.lang.Integer.parseInt;

public class CSVReader {
    public static void main(String[] args) {
        String DeliveriesPath = "src/Resources/deliveries.csv";
        String MatchesPath = "src/Resources/matches.csv";
        String line1 = "";
        int years[] = new int[10];
        for(int i=0;i<10;i++){
            years[i] = 0;
        }
        HashMap<String,Integer> wonMatches = new HashMap<String, Integer>();
        int winCount = 0;

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
                    count=0;
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

        HashMap<String,Integer> extraRun = new HashMap<String, Integer>();
        int runCount = 0;

        try {
            BufferedReader rdr = new BufferedReader(new FileReader(DeliveriesPath));

            int itrator = 0;
            while((line2 = rdr.readLine()) != null){
                if(itrator == 0){
                    itrator++;
                    continue;
                }
                String[] val = line2.split(",");
                if(parseInt(val[0]) > 576 && parseInt(val[0]) <= 636){
                    if(!extraRun.containsKey(val[2])){
                        int run = parseInt(val[16]);
                        extraRun.put(val[2],run);
                    }else {
                        int counter = extraRun.get(val[2]);
                        counter = counter + parseInt(val[16]);
                        extraRun.put(val[2],counter);
                        counter= 0;
                    }

                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("------Scenario 3------");
        System.out.println(extraRun);
        System.out.println("----------------------");


    }
}
