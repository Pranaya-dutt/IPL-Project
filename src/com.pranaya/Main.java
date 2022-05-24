package com.pranaya;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Main {
    static String deliveriesFilePath = "src/Resources/deliveries.csv";
    static String matchesFilePath = "src/Resources/matches.csv";

    public static void main(String[] args) {
        List<Match> matches = getMatchesData();
        List<Delivery> deliveries = getDeliveriesData();

        findNumberOfMatchesPlayedPerYearOfAllYears(matches);
        findNumberOfMatchesWonPerTeamInAllSeasons(matches);
        findExtraRunsConcededPerTeamIn2016(matches, deliveries);
        findMostEconomicalBowlerIn2015(matches, deliveries);
        findNumberOfTossWonByEachTeam(matches);
    }

    private static List<Delivery> getDeliveriesData() {
        List<Delivery> deliveryData = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(deliveriesFilePath));
            String line = "";
            int iteration = 0;
            while ((line = reader.readLine()) != null) {
                if (iteration == 0) {
                    iteration++;
                    continue;
                }
                String[] fields = line.split(",");

                Delivery delivery = new Delivery();
                delivery.setId(Integer.parseInt(fields[0]));
                delivery.setInning(Integer.parseInt(fields[1]));
                delivery.setExtraRuns(Integer.parseInt(fields[16]));
                delivery.setBowlingTeam(fields[3]);
                delivery.setTotalRuns(Integer.parseInt(fields[17]));
                delivery.setBowler(fields[8]);

                deliveryData.add(delivery);

            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
        return deliveryData;
    }

    private static List<Match> getMatchesData() {
        List<Match> matchData = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(matchesFilePath));
            String line = "";
            int iteration = 0;
            while ((line = reader.readLine()) != null) {
                if(iteration == 0){
                    iteration++;
                    continue;
                }
                String[] fields = line.split(",");

                Match match = new Match();
                match.setId(Integer.parseInt(fields[0]));
                match.setSeason(Integer.parseInt(fields[1]));
                match.setCity(fields[2]);
                match.setDate(fields[3]);
                match.setResult(fields[8]);
                match.setWinner(fields[10]);
                match.setTossWinner(fields[6]);


                matchData.add(match);
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }
        return matchData;
    }

    private static void findNumberOfMatchesPlayedPerYearOfAllYears(List<Match> matches) {
        int[] years = new int[10];
        for(int i=0;i<10;i++){
            years[i] = 0;
        }
        for (Match match : matches) {
            int index = (match.getSeason()) - 2008;
            years[index]++;
        }
        System.out.println("------Scenario 1------");
        System.out.println("Matches played per year");
        System.out.println();
        for(int i=0;i<10;i++){
            System.out.println("Matches played in " + (i+2008)+ " = " + years[i]);
        }
        System.out.println("----------------------");
    }

    private static void findNumberOfMatchesWonPerTeamInAllSeasons(List<Match> matches) {
        Map<String,Integer> wonMatches = new HashMap<String, Integer>();
        int winCount;

        for(Match match : matches){
            if(match.getResult().equals("normal")){
                if(!wonMatches.containsKey(match.getWinner())){
                    wonMatches.put(match.getWinner(), 1);
                } else {
                    winCount = wonMatches.get(match.getWinner());
                    winCount += 1;
                    wonMatches.put(match.getWinner(), winCount);
                }
            }
        }
        System.out.println();

        System.out.println("------Scenario 2------");
        System.out.println("Number of Matches won by every team.");
        System.out.println();
        wonMatches.forEach((key, value) -> {
            if (!key.equals("")) System.out.println(key + " : " + value);
        });
        System.out.println("----------------------");
    }

    private static void findExtraRunsConcededPerTeamIn2016(List<Match> matches, List<Delivery> deliveries) {
        Map<String, Integer> extraRuns = new HashMap<>();
        for(Match match : matches){
            if(match.getSeason() == 2016){
                for(Delivery delivery : deliveries){
                    if(delivery.getId() == match.getId()) {
                        if (!extraRuns.containsKey(delivery.getBowlingTeam())) {
                            extraRuns.put(delivery.getBowlingTeam(), delivery.getExtraRuns());
                        } else {
                            int runs = extraRuns.get(delivery.getBowlingTeam());
                            runs = runs + delivery.getExtraRuns();
                            extraRuns.put(delivery.getBowlingTeam(), runs);
                        }
                    }
                }
            }
        }
        System.out.println();

        System.out.println("------Scenario 3------");
        System.out.println("Extra runs conceded per team in 2016");
        System.out.println();
        extraRuns.forEach((key, value) -> System.out.println(key + " : " + value));
        System.out.println("----------------------");
    }

    private static void findMostEconomicalBowlerIn2015(List<Match> matches, List<Delivery> deliveries) {
        Map<String, Integer> runsPerBowler = new HashMap<>();
        Map<String, Float> oversPerBowler = new HashMap<>();
        TreeMap<Float, String> economyRatePerBowler = new TreeMap<>();

        for(Match match: matches){
            if(match.getSeason() == 2015){
                for(Delivery delivery : deliveries){
                    if(delivery.getId() == match.getId()){
                        if(!runsPerBowler.containsKey(delivery.getBowler())){
                            runsPerBowler.put(delivery.getBowler(), delivery.getTotalRuns());
                        } else {
                            int runs = runsPerBowler.get(delivery.getBowler());
                            runs = runs + delivery.getTotalRuns();
                            runsPerBowler.put(delivery.getBowler(), runs);
                        }
                        if(!oversPerBowler.containsKey(delivery.getBowler())){
                            oversPerBowler.put(delivery.getBowler(), 1f);
                        } else {
                            float balls = oversPerBowler.get(delivery.getBowler());
                            balls = balls + 1f;
                            oversPerBowler.put(delivery.getBowler(), balls);
                        }
                    }
                }
            }
        }

        oversPerBowler.forEach((key, value) -> {
            oversPerBowler.put(key,(value/6));
        });

        for(Match match : matches){
            if(match.getSeason() == 2015){
                for(Delivery delivery : deliveries){
                    if(delivery.getId() == match.getId()){
                        if(!economyRatePerBowler.containsValue(delivery.getBowler())){
                            float economyRate = runsPerBowler.get(delivery.getBowler())/oversPerBowler.get(delivery.getBowler());
                            economyRatePerBowler.put(economyRate, delivery.getBowler());
                        }
                    }
                }
            }
        }
        System.out.println();
        System.out.println("------Scenario 4------");
        System.out.println("Find the most economical bowler of 2015 IPL");
        System.out.println();
        System.out.println("Most economical bowler of 2015 IPL is :");
        System.out.println(economyRatePerBowler.firstEntry().getValue() + " with economy rate of " + economyRatePerBowler.firstKey());
        System.out.println("----------------------");
    }

    private static void findNumberOfTossWonByEachTeam(List<Match> matches) {
        Map<String, Integer> wonTossMap = new HashMap<>();
        int tossWinnerCounter;
        for(Match match: matches){
            if(!wonTossMap.containsKey(match.getTossWinner())){
                wonTossMap.put(match.getTossWinner(), 1);
            } else {
                tossWinnerCounter = wonTossMap.get(match.getTossWinner());
                tossWinnerCounter++;
                wonTossMap.put(match.getTossWinner(), tossWinnerCounter);
            }
        }
        System.out.println();

        System.out.println("------Scenario 5------");
        System.out.println("Number of times toss won by each team");
        System.out.println();
        wonTossMap.forEach((key, value) -> System.out.println(key + " : " + value));
        System.out.println("----------------------");
    }
}
