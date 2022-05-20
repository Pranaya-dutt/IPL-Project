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
        String line = "";
        int years[] = new int[10];
        for(int i=0;i<10;i++){
            years[i] = 0;
        }
        HashMap<String,Integer> wonMatches = new HashMap<String, Integer>();
        int winCount = 0;

        try {
            BufferedReader br = new BufferedReader(new FileReader(MatchesPath));

            int iteration = 0;
            while((line = br.readLine()) != null){
                if(iteration == 0){
                    iteration++;
                    continue;
                }
                String[] values = line.split(",");
                //System.out.println(values[1]);
                int index = parseInt(values[1])-2008;
                years[index]++;

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



    }
}
