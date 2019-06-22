package com.danielmaartens;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws Exception {

        List<List<List<String>>> allResults = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);

        System.out.println("\nHello there !\n");

        Integer initialDelay = 1000;
        Integer delay = initialDelay;

        delayedPrint("This program will calculate the ranking table for a soccer league.\n", delay);
        delay += initialDelay;

        delayedPrint("The data for the results of the games should be stored in a text file.\n", delay);
        delay += initialDelay;

        delayedPrint("Please provide the full path of the file where your results are stored:\n", delay);

        String file = scanner.next();
        
        List<TeamMatchPoints> teamMatchPoints = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            Pattern p = Pattern.compile("^([a-zA-Z\\s]+)([0-9]+$)");
            
            while ((line = br.readLine()) != null) {
                
                List<TeamScore> scores = new ArrayList<>();
                
                String[] matchResults = line.split(", ");

                for (String result: matchResults) {

                    // Use regex pattern to match team names with spaces
                    Matcher m = p.matcher(result);

                    if (m.find()) {
                        TeamScore score = new TeamScore();

                        String team = m.group(1);

                        // Remove the last space from the team name
                        score.setTeam(team.substring(0, team.length()-1));
                        score.setScore(Integer.valueOf(m.group(2)));

                        scores.add(score);
                    }

                }
                
                teamMatchPoints.addAll(calculateMatchPoints(scores));
                
            }

            List<TeamMatchPoints> finalTeamMatchPoints = reduceTeamMatchPoints(teamMatchPoints);

            // Sort team match points by points and then by team name (if points are the same)
            Comparator<TeamMatchPoints> comparator = Comparator.comparingInt(TeamMatchPoints::getPoints).reversed().thenComparing(TeamMatchPoints::getTeam);

            Collections.sort(finalTeamMatchPoints, comparator);

            int rank = 1;

            for (TeamMatchPoints team : finalTeamMatchPoints) {

                Integer points = team.getPoints();
                System.out.println(rank + ". " + team.getTeam() + ", " + points + (points.equals(1) ? " pt" : " pts"));
                rank++;

            }
            
        }
    }

    public static List<TeamMatchPoints> reduceTeamMatchPoints (List<TeamMatchPoints> allTeamMatchPoints) {
        HashMap<String, Integer> finalTeamPoints = new HashMap<>();
        List<TeamMatchPoints> reducedMatchPoints = new ArrayList<>();

        for (TeamMatchPoints matchPoints : allTeamMatchPoints) {

                String name = matchPoints.getTeam();
                Integer points = matchPoints.getPoints();


                if (!finalTeamPoints.containsKey(name)) {
                    finalTeamPoints.put(name, points);
                } else {
                    Integer nextPointsTotal = finalTeamPoints.get(name) + points;

                    finalTeamPoints.put(name, nextPointsTotal);
                }
            }

        for (Map.Entry<String, Integer> entry : finalTeamPoints.entrySet()) {

            TeamMatchPoints teamPoints = new TeamMatchPoints();

            teamPoints.setTeam(entry.getKey());
            teamPoints.setPoints(entry.getValue());

            reducedMatchPoints.add(teamPoints);
        }



        return reducedMatchPoints;
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

    public static List<TeamMatchPoints> calculateMatchPoints(List<TeamScore> matchResults) {

        List<TeamMatchPoints> matchPoints = new ArrayList<>();
        TeamMatchPoints teamAPoints = new TeamMatchPoints();
        TeamMatchPoints teamBPoints = new TeamMatchPoints();

        TeamScore teamA = matchResults.get(0);
        TeamScore teamB = matchResults.get(1);

        String teamAName = teamA.getTeam();
        Integer teamAGoals = teamA.getScore();
        teamAPoints.setTeam(teamAName);
        teamAPoints.setPoints(0);

        String teamBName = teamB.getTeam();
        Integer teamBGoals = teamB.getScore();
        teamBPoints.setTeam(teamBName);
        teamBPoints.setPoints(0);

        if (teamAGoals.equals(teamBGoals)) {

            teamAPoints.setPoints(1);
            teamBPoints.setPoints(1);

        } else if (teamAGoals > teamBGoals) {
            teamAPoints.setPoints(3);
        } else {
            teamBPoints.setPoints(3);
        }

        matchPoints.add(teamAPoints);
        matchPoints.add(teamBPoints);

        return matchPoints;
    }

}
