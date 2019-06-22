package com.danielmaartens;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {

        List<List<List<String>>> allResults = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);

        System.out.print("Hello there ! You will be asked to input the full path of your file containing: \n");

        Integer initialDelay = 3000;
        Integer delay = initialDelay;

        delayedPrint("This program will calculate the ranking table for a soccer league.\n", delay);
        delay += initialDelay;

        delayedPrint("The data for the results of the games should be stored in a text file.\n", delay);
        delay += initialDelay;

        delayedPrint("Please provide the full path of the file where your results are stored:\n");

        String file = scanner.next();
        
        List<HashMap<String, Integer>> teamMatchPoints = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            
            while ((line = br.readLine()) != null) {
                
                List<List<String>> results = new ArrayList<>();
                
                String[] matchResults = line.split(",");

                for (String result:
                     matchResults) {
                    String[] teamResults = result.split(" ");
                    results.add(Arrays.asList(teamResults));
                }
                
                teamMatchPoints.add(calculateMatchPoints(results));
                
            }

            HashMap<String, Integer> finalTeamMatchPoints = reduceTeamMatchPoints(teamMatchPoints);
            
        }
    }

    public static HashMap<String, Integer> reduceTeamMatchPoints (List<HashMap<String, Integer>> allMatchesTeamPoints) {
        HashMap<String, Integer> finalTeamPoints = new HashMap<>();

        for (HashMap<String, Integer> matchTeamPoints :
                allMatchesTeamPoints) {

            for (Map.Entry<String, Integer> entry : matchTeamPoints.entrySet()) {
                String name = entry.getKey();
                Integer points = entry.getValue();


                if (!finalTeamPoints.containsKey(name)) {
                    finalTeamPoints.put(name, points);
                } else {
                    Integer nextPointsTotal = finalTeamPoints.get(name) + points;

                    finalTeamPoints.put(name, nextPointsTotal);
                }
            }

        }

        return finalTeamPoints;
    }

    public static void delayedPrint(String text, Integer delay) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(text);
            }
        },  delay);
    }

    public static void delayedPrint(String text) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(text);
            }
        },  3000);
    }

    public static HashMap<String, Integer> calculateMatchPoints(List<List<String>> matchResults) {

        HashMap<String, Integer> matchPoints = new HashMap<>();
        List<String> teamA = matchResults.get(0);
        List<String> teamB = matchResults.get(1);

        String teamAName = teamA.get(0);
        Integer teamAGoals = Integer.valueOf(teamA.get(1));

        String teamBName = teamB.get(0);
        Integer teamBGoals = Integer.valueOf(teamB.get(1));

        int teamAPoints = 0;
        int teamBPoints = 0;

        if (teamAGoals.equals(teamBGoals)) {

            teamAPoints = 1;
            teamBPoints = 1;

        } else if (teamAGoals > teamBGoals) {
            teamAPoints = 3;
        } else {
            teamBPoints = 3;
        }

        matchPoints.put(teamAName, teamAPoints);
        matchPoints.put(teamBName, teamBPoints);

        return matchPoints;
    }

}
