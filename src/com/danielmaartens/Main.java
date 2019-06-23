package com.danielmaartens;

import utils.Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static final String TeamResultGroupingPattern = "^([a-zA-Z\\s]+)([0-9]+$)";

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        Boolean running = true;

        System.out.println("\nWelcome to the Match Point Calculator !\n");

        Integer initialDelay = 1500;
        Integer delay = initialDelay;

        delayedPrint("This program will calculate the ranking table for a soccer league.\n", delay);
        delay += initialDelay;

        delayedPrint("The data for the results of the games should be stored in a text file.", delay);
        delay += initialDelay;

        while (running) {

            delayedPrint("\nPlease provide the full path of the file where your results are stored:\n", delay);
            delayedPrint("Full File Path: ", delay + 10);

            String file = scanner.next();

            try {
                System.out.println("\nRESULTS\n");

                List<TeamValue> finalTeamMatchPoints = getOrderedMatchPointsFromFile(file);

                for (TeamValue team : finalTeamMatchPoints) {

                    Integer points = team.getValue();
                    System.out.println(team.getRank() + ". " + team.getName() + ", " + points + (points.equals(1) ? " pt" : " pts"));

                }

                System.out.println("\nWould you like to check match point results of another league ? [Y/N]: ");

                String answer = scanner.next();

                running = Utils.booleanFromString(answer);
                delay = 0;

            } catch (Exception e) {
                System.out.println("Something went wrong while trying to calculate the match points: " + e.getMessage());
            }
        }

        System.out.println("\nThank you for using the Match Point Calculator !");
        System.exit(0);


    }

    public static void setTeamRanks(List<TeamValue> sortedTeamValues) {

        int index = 1;
        int rank = 0;
        Integer previousTeamPoints = null;

        for (TeamValue team : sortedTeamValues) {

            Integer points = team.getValue();

            if (!points.equals(previousTeamPoints)) {
                rank++;
            }

            team.setRank(rank);

            if (points.equals(previousTeamPoints)) {
                rank = index;
            }

            previousTeamPoints = points;
            index++;

        }
    }

    public static List<TeamValue> getOrderedMatchPointsFromFile(String file) throws Exception {
        List<TeamValue> teamMatchPoints = new ArrayList<>();
        List<TeamValue> finalTeamMatchPoints = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            Pattern p = Pattern.compile(TeamResultGroupingPattern);

            while ((line = br.readLine()) != null) {

                List<TeamValue> scores = new ArrayList<>();

                String[] matchResults = line.split(", ");

                for (String result : matchResults) {

                    TeamValue teamResult = getTeamResultFromString(result, p);

                    if (teamResult != null) {
                        scores.add(teamResult);
                    }

                }

                teamMatchPoints.addAll(calculateMatchPoints(scores));

            }
        }

        finalTeamMatchPoints = reduceTeamMatchPoints(teamMatchPoints);

        sort(finalTeamMatchPoints);
        setTeamRanks(finalTeamMatchPoints);

        return finalTeamMatchPoints;
    }

    public static TeamValue getTeamResultFromString(String result, Pattern pattern) {

        // Use regex pattern to match team names that include spaces
        Matcher m = pattern.matcher(result);

        if (m.find()) {

            String team = m.group(1);

            // Remove the last space from the team name
            String name = team.substring(0, team.length() - 1);

            Integer value = Integer.valueOf(m.group(2));

            return new TeamValue(name, value);
        }

        return null;
    }

    public static void sort(List<TeamValue> finalTeamMatchPoints) {
        // Sort team match points by points and then by team name (if points are the same)
        Comparator<TeamValue> comparator = Comparator.comparingInt(TeamValue::getValue).reversed().thenComparing(TeamValue::getName);

        Collections.sort(finalTeamMatchPoints, comparator);
    }

    public static List<TeamValue> reduceTeamMatchPoints(List<TeamValue> allTeamMatchPoints) {
        HashMap<String, Integer> finalTeamPoints = new HashMap<>();

        for (TeamValue matchPoints : allTeamMatchPoints) {

            String name = matchPoints.getName();
            Integer points = matchPoints.getValue();

            if (!finalTeamPoints.containsKey(name)) {
                finalTeamPoints.put(name, points);
            } else {
                Integer nextPointsTotal = finalTeamPoints.get(name) + points;

                finalTeamPoints.put(name, nextPointsTotal);
            }
        }

        return Utils.convertTeamValueMapToList(finalTeamPoints);
    }

    public static void delayedPrint(String text, Integer delay) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(text);
            }
        }, delay);
    }

    public static void delayedPrint(String text) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(text);
            }
        }, 3000);
    }

    public static List<TeamValue> calculateMatchPoints(List<TeamValue> matchResults) {

        List<TeamValue> matchPoints = new ArrayList<>();

        TeamValue teamA = matchResults.get(0);
        TeamValue teamB = matchResults.get(1);

        String teamAName = teamA.getName();
        Integer teamAGoals = teamA.getValue();
        TeamValue teamAPoints = new TeamValue(teamAName, 0);

        String teamBName = teamB.getName();
        Integer teamBGoals = teamB.getValue();
        TeamValue teamBPoints = new TeamValue(teamBName, 0);

        if (teamAGoals.equals(teamBGoals)) {

            teamAPoints.setValue(1);
            teamBPoints.setValue(1);

        } else if (teamAGoals > teamBGoals) {
            teamAPoints.setValue(3);
        } else {
            teamBPoints.setValue(3);
        }

        matchPoints.add(teamAPoints);
        matchPoints.add(teamBPoints);

        return matchPoints;
    }

}
