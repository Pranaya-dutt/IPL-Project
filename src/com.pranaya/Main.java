package com.pranaya;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;


public class Main {

//    private static final int MATCH_ID = 0;
//    public static final int MATCH_SEASON = 1;
//    public static final int MATCH_CITY = 2;
//    public static final int MATCH_DATE = 3;
//    public static final int MATCH_TEAM1 = 4;
//    public static final int MATCH_TEAM2 = 5;
//    public static final int MATCH_TOSS_WINNER = 6;
//    public static final int MATCH_TOSS_DECISION = 7;
//    public static final int MATCH_RESULT = 8;
//    public static final int MATCH_DL_APPLIED = 9;
//    public static final int MATCH_WINNER = 10;
//    public static final int MATCH_WIN_BY_RUNS = 11;
//    public static final int MATCH_WIN_BY_WICKETS = 12;
//    public static final int MATCH_PLAYER_OF_MATCH = 13;
//    public static final int MATCH_VENUE = 14;
//    public static final int DELIVERY_ID = 0;
//    public static final int DELIVERY_INNING = 1;
//    public static final int DELIVERY_BATTING_TEAM = 2;
//    public static final int DELIVERY_BOWLING_TEAM = 3;
//    public static final int DELIVERY_OVER = 4;
//    public static final int DELIVERY_BALL = 5;
//    public static final int DELIVERY_BATSMAN = 6;
//    public static final int DELIVERY_NON_STRIKER = 7;
//    public static final int DELIVERY_BOWLER = 8;
//    public static final int DELIVERY_SUPER_OVER = 9;
//    public static final int DELIVERY_WIDE_RUNS = 10;
//    public static final int DELIVERY_BYE_RUNS = 11;
//    public static final int DELIVERY_LEG_BYE_RUNS = 12;
//    public static final int DELIVERY_NO_BALL_RUNS = 13;
//    public static final int DELIVERY_PENALTY_RUNS = 14;
//    public static final int DELIVERY_BATSMAN_RUN = 15;
//    public static final int DELIVERY_EXTRA_RUNS = 16;
//    public static final int DELIVERY_TOTAL_RUNS = 17;
//
//    static String deliveriesFilePath = "src/Resources/deliveries.csv";
//    static String matchesFilePath = "src/Resources/matches.csv";

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

//    private static List<Delivery> getDeliveriesData() {
//        List<Delivery> deliveryData = new ArrayList<>();
//        try {
//            BufferedReader reader = new BufferedReader(new FileReader(deliveriesFilePath));
//            String line = "";
//            int iteration = 0;
//            while ((line = reader.readLine()) != null) {
//                if (iteration == 0) {
//                    iteration++;
//                    continue;
//                }
//                String[] fields = line.split(",");
//
//                Delivery delivery = new Delivery();
//
//                delivery.setId(Integer.parseInt(fields[DELIVERY_ID]));
//                delivery.setInning(Integer.parseInt(fields[DELIVERY_INNING]));
//                delivery.setBattingTeam(fields[DELIVERY_BATTING_TEAM]);
//                delivery.setBowlingTeam(fields[DELIVERY_BOWLING_TEAM]);
//                delivery.setOver(Integer.parseInt(fields[DELIVERY_OVER]));
//                delivery.setBall(Integer.parseInt(fields[DELIVERY_BALL]));
//                delivery.setBatsman(fields[DELIVERY_BATSMAN]);
//                delivery.setNonStriker(fields[DELIVERY_NON_STRIKER]);
//                delivery.setBowler(fields[DELIVERY_BOWLER]);
//                delivery.setIsSuperOver(Integer.parseInt(fields[DELIVERY_SUPER_OVER]));
//                delivery.setWideRuns(Integer.parseInt(fields[DELIVERY_WIDE_RUNS]));
//                delivery.setByeRuns(Integer.parseInt(fields[DELIVERY_BYE_RUNS]));
//                delivery.setLegByeRuns(Integer.parseInt(fields[DELIVERY_LEG_BYE_RUNS]));
//                delivery.setNoBallRuns(Integer.parseInt(fields[DELIVERY_NO_BALL_RUNS]));
//                delivery.setPenaltyRuns(Integer.parseInt(fields[DELIVERY_PENALTY_RUNS]));
//                delivery.setBatsmanRuns(Integer.parseInt(fields[DELIVERY_BATSMAN_RUN]));
//                delivery.setExtraRuns(Integer.parseInt(fields[DELIVERY_EXTRA_RUNS]));
//                delivery.setTotalRuns(Integer.parseInt(fields[DELIVERY_TOTAL_RUNS]));
//
//                deliveryData.add(delivery);
//
//            }
//        } catch (IOException e) {
//            throw new RuntimeException();
//        }
//        return deliveryData;
//    }

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
//    private static List<Match> getMatchesData() {
//        List<Match> matchData = new ArrayList<>();
//        try {
//            BufferedReader reader = new BufferedReader(new FileReader(matchesFilePath));
//            String line = "";
//            int iteration = 0;
//            while ((line = reader.readLine()) != null) {
//                if (iteration == 0) {
//                    iteration++;
//                    continue;
//                }
//                String[] fields = line.split(",");
//
//                Match match = new Match();
//
//                match.setId(Integer.parseInt(fields[MATCH_ID]));
//                match.setSeason(Integer.parseInt(fields[MATCH_SEASON]));
//                match.setCity(fields[MATCH_CITY]);
//                match.setDate(fields[MATCH_DATE]);
//                match.setTeam1(fields[MATCH_TEAM1]);
//                match.setTeam2(fields[MATCH_TEAM2]);
//                match.setTossWinner(fields[MATCH_TOSS_WINNER]);
//                match.setTossDecision(fields[MATCH_TOSS_DECISION]);
//                match.setResult(fields[MATCH_RESULT]);
//                match.setDlApplied(Integer.parseInt(fields[MATCH_DL_APPLIED]));
//                match.setWinner(fields[MATCH_WINNER]);
//                match.setWinByRuns(Integer.parseInt(fields[MATCH_WIN_BY_RUNS]));
//                match.setWinByWickets(Integer.parseInt(fields[MATCH_WIN_BY_WICKETS]));
//                match.setPlayerOfMatch(fields[MATCH_PLAYER_OF_MATCH]);
//                match.setVenue(fields[MATCH_VENUE]);
//
//                matchData.add(match);
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return matchData;
//    }

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
