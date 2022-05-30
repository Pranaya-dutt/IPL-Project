package com.pranaya;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;


public class Main {


    public static void main(String[] args) {
        List<Match> matches = getMatchesData();
        List<Delivery> deliveries = getDeliveriesData();

        findNumberOfMatchesPlayedPerYearOfAllYears(matches);
        findNumberOfMatchesWonPerTeamInAllSeasons(matches);
        findExtraRunsConcededPerTeamIn2016(matches, deliveries);
        findMostEconomicalBowlerIn2015(matches, deliveries);
        findNumberOfTossWonByEachTeam(matches);
    }

    public static Connection getConnection() {
        Connection con= null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/iplDB", "root", "Pranaya@11");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return con;
    }

    private static List<Delivery> getDeliveriesData(){
        List<Delivery> deliveryData = new ArrayList<>();

        Statement st = null;
        ResultSet rs = null;
        Connection con = getConnection();

        try{
            st = con.createStatement();
            rs = st.executeQuery("select * from deliveries");
            while(rs.next()){
                Delivery delivery = new Delivery();

                delivery.setId(rs.getInt("match_id"));
                delivery.setInning(rs.getInt("inning"));
                delivery.setBattingTeam(rs.getString("batting_team"));
                delivery.setBowlingTeam(rs.getString("bowling_team"));
                delivery.setOver(rs.getInt("over"));
                delivery.setBall(rs.getInt("ball"));
                delivery.setBatsman(rs.getString("batsman"));
                delivery.setNonStriker(rs.getString("non_striker"));
                delivery.setBowler(rs.getString("bowler"));
                delivery.setIsSuperOver(rs.getInt("is_super_over"));
                delivery.setWideRuns(rs.getInt("wide_runs"));
                delivery.setByeRuns(rs.getInt("bye_runs"));
                delivery.setLegByeRuns(rs.getInt("legbye_runs"));
                delivery.setNoBallRuns(rs.getInt("noball_runs"));
                delivery.setPenaltyRuns(rs.getInt("penalty_runs"));
                delivery.setBatsmanRuns(rs.getInt("batsman_runs"));
                delivery.setExtraRuns(rs.getInt("extra_runs"));
                delivery.setTotalRuns(rs.getInt("total_runs"));

                deliveryData.add(delivery);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return deliveryData;
    }


    private static List<Match> getMatchesData(){
        List<Match> matchData = new ArrayList<>();

        Statement st = null;
        ResultSet rs = null;
        Connection con = getConnection();

        try{
            st = con.createStatement();
            rs = st.executeQuery("select * from matches");
            while(rs.next()){
                Match match = new Match();

                match.setId(rs.getInt("id"));
                match.setSeason(rs.getInt("season"));
                match.setCity(rs.getString("city"));
                match.setDate(String.valueOf(rs.getDate("date")));
                match.setTeam1(rs.getString("team1"));
                match.setTeam2(rs.getString("team2"));
                match.setTossWinner(rs.getString("toss_winner"));
                match.setTossDecision(rs.getString("toss_decision"));
                match.setResult(rs.getString("result"));
                match.setDlApplied(rs.getInt("dl_applied"));
                match.setWinner(rs.getString("winner"));
                match.setWinByRuns(rs.getInt("win_by_runs"));
                match.setWinByWickets(rs.getInt("win_by_wickets"));
                match.setPlayerOfMatch(rs.getString("player_of_match"));
                match.setVenue(rs.getString("venue"));

                matchData.add(match);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return matchData;
    }

    private static void findNumberOfMatchesPlayedPerYearOfAllYears(List<Match> matches) {
        Map<Integer, Integer> matchesPerYear = new TreeMap<>();

        for (Match match : matches) {
            if (!matchesPerYear.containsKey(match.getSeason())) {
                matchesPerYear.put(match.getSeason(), 1);
            } else {
                matchesPerYear.put(match.getSeason(), matchesPerYear.get(match.getSeason()) + 1);
            }
        }

        System.out.println("Matches played per year");
        System.out.println();
        matchesPerYear.forEach((key, value) -> System.out.println("Matches won in year " + key + " : " + value));
        System.out.println("----------------------");
    }

    private static void findNumberOfMatchesWonPerTeamInAllSeasons(List<Match> matches) {
        Map<String, Integer> wonMatches = new HashMap<>();
        int winCount;

        for (Match match : matches) {
            if (match.getResult().equals("normal")) {
                if (!wonMatches.containsKey(match.getWinner())) {
                    wonMatches.put(match.getWinner(), 1);
                } else {
                    winCount = wonMatches.get(match.getWinner());
                    winCount += 1;
                    wonMatches.put(match.getWinner(), winCount);
                }
            }
        }

        System.out.println();
        System.out.println("Number of Matches won by every team.");
        System.out.println();
        wonMatches.forEach((key, value) -> {
            if (!key.equals("")) System.out.println(key + " : " + value);
        });
        System.out.println("----------------------");
    }

    private static void findExtraRunsConcededPerTeamIn2016(List<Match> matches, List<Delivery> deliveries) {
        Map<String, Integer> extraRuns = new HashMap<>();
        for (Match match : matches) {
            if (match.getSeason() == 2016) {
                for (Delivery delivery : deliveries) {
                    if (delivery.getId() == match.getId()) {
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
        System.out.println("Extra runs conceded per team in 2016");
        System.out.println();
        extraRuns.forEach((key, value) -> System.out.println(key + " : " + value));
        System.out.println("----------------------");
    }

    private static void findMostEconomicalBowlerIn2015(List<Match> matches, List<Delivery> deliveries) {
        Map<String, Integer> runsPerBowler = new HashMap<>();
        Map<String, Float> oversPerBowler = new HashMap<>();
        TreeMap<Float, String> economyRatePerBowler = new TreeMap<>();

        for (Match match : matches) {
            if (match.getSeason() == 2015) {
                for (Delivery delivery : deliveries) {
                    if (delivery.getId() == match.getId()) {
                        if (!runsPerBowler.containsKey(delivery.getBowler())) {
                            runsPerBowler.put(delivery.getBowler(), delivery.getTotalRuns());
                        } else {
                            int runs = runsPerBowler.get(delivery.getBowler());
                            runs = runs + delivery.getTotalRuns();
                            runsPerBowler.put(delivery.getBowler(), runs);
                        }
                        if (!oversPerBowler.containsKey(delivery.getBowler())) {
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
            oversPerBowler.put(key, (value / 6));
        });

        for (Match match : matches) {
            if (match.getSeason() == 2015) {
                for (Delivery delivery : deliveries) {
                    if (delivery.getId() == match.getId()) {
                        if (!economyRatePerBowler.containsValue(delivery.getBowler())) {
                            float economyRate = runsPerBowler.get(delivery.getBowler()) / oversPerBowler.get(delivery.getBowler());
                            economyRatePerBowler.put(economyRate, delivery.getBowler());
                        }
                    }
                }
            }
        }

        System.out.println();
        System.out.println("Most economical bowler of 2015 IPL is :");
        System.out.println(economyRatePerBowler.firstEntry().getValue() + " with economy rate of " + economyRatePerBowler.firstKey());
        System.out.println("----------------------");
    }

    private static void findNumberOfTossWonByEachTeam(List<Match> matches) {
        Map<String, Integer> wonTossMap = new HashMap<>();
        int tossWinnerCounter;
        for (Match match : matches) {
            if (!wonTossMap.containsKey(match.getTossWinner())) {
                wonTossMap.put(match.getTossWinner(), 1);
            } else {
                tossWinnerCounter = wonTossMap.get(match.getTossWinner());
                tossWinnerCounter++;
                wonTossMap.put(match.getTossWinner(), tossWinnerCounter);
            }
        }

        System.out.println();
        System.out.println("Number of times toss won by each team");
        System.out.println();
        wonTossMap.forEach((key, value) -> System.out.println(key + " : " + value));
        System.out.println("----------------------");
    }
}
