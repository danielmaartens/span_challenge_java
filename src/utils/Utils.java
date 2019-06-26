package utils;

import main.TeamValue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static final String TEAM_RESULT_GROUPING_PATTERN = "^([a-zA-Z\\s]+)([0-9]+$)";

    /**
     * Converts a list of TeamValues to a map with the team name as key with the corresponding value in TeamValueobject.
     * This is so that the tests who know the team name can easily access the value property.
     *
     * @param teamValues
     * @return
     */
    public static HashMap<String, Integer> convertTeamValueListToMap(List<TeamValue> teamValues) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        for (TeamValue team : teamValues) map.put(team.getName(), team.getValue());

        return map;
    }


    /**
     * Convert a map object to a list for easier processing of data later.
     *
     * @param teamValuesMap
     * @return
     */
    private static List<TeamValue> convertTeamValueMapToList(HashMap<String, Integer> teamValuesMap) {
        List<TeamValue> list = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : teamValuesMap.entrySet()) {

            TeamValue team = new TeamValue(entry.getKey(), entry.getValue());

            list.add(team);
        }

        return list;
    }

    /**
     * Converts expected user input for yes/no/continue questions into a boolean value.
     *
     * @param s
     * @return
     */
    public static Boolean booleanFromString(String s) {
        String lowerCaseS = s.toLowerCase();

        switch (lowerCaseS) {
            case "y":
            case "yes":
            case "c":
                return true;
            case "n":
            case "no":
                return false;
            default:
                return null;
        }

    }


    /**
     * Sets the rank value for all teams.
     * Note: the array must be sorted.
     *
     * @param sortedTeamValues
     */
    private static void setTeamRanks(List<TeamValue> sortedTeamValues) {

        int index = 1;
        int rank = 0;
        Integer previousTeamPoints = null;

        for (TeamValue team : sortedTeamValues) {

            Integer points = team.getValue();

            // Only change rank to running index if current points and previous points are different
            // This is to make sure that teams who have the same points have the same rank.
            if (!points.equals(previousTeamPoints)) {
                rank = index;
            }

            team.setRank(rank);

            // Set previous points to current points for next iteration check.
            previousTeamPoints = points;
            index++;

        }
    }

    /**
     * This is the most important function.
     * It serves as the parent for most of the other functions within this module.
     * It is responsible for reading through the file contents line by line and
     * processing the final ranks of teams in the league based on all the matches played.
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static List<TeamValue> getLeagueResults(String file) throws Exception {
        List<TeamValue> matchPoints = new ArrayList<>();
        List<TeamValue> leagueResults = new ArrayList<>();

        // read file contents
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            Pattern p = Pattern.compile(TEAM_RESULT_GROUPING_PATTERN);

            // go through each line of the file
            while ((line = br.readLine()) != null) {

                List<TeamValue> scores = new ArrayList<>();

                // Each line represents the outcome of a match.
                // Each team's own outcome of the match is separated by a ", "
                // which is why we first split the line by ", " to get a matchResults array
                // of two strings representing the outcome of each team for the match.
                String[] matchResults = line.split(", ");

                // Now we loop through the matchResults
                for (String result : matchResults) {

                    // We parse the string into a TeamValueobject for easy processing later.
                    TeamValue teamResult = getTeamResultFromString(result, p);

                    // We add this result to an array representing the scores for each team of this match.
                    if (teamResult != null) {
                        scores.add(teamResult);
                    }

                }

                // Now that we have an array of TeamValueobjects for the match representing each team,
                // we can calculate the match points.

                // Here we also concatenate the new matchPoints array with all previous added matchPoints.
                // The purpose of this is to have an array of TeamValueobjects each representing
                // the points the team gained in a match.

                matchPoints.addAll(calculateMatchPoints(scores));

            }
        }

        // Now we reduce this array of all our teams' matchPoints
        // into an array containing a single entry for each team
        // with the value representing the sum of all their match points gained.
        leagueResults = reduceTeamMatchPoints(matchPoints);

        // Sorts leagueResults by value DESC, and if value is the same then by name ASC.
        Comparator<TeamValue> comparator = Comparator.comparingInt(TeamValue::getValue).reversed().thenComparing(TeamValue::getName);
        leagueResults.sort(comparator);

        // Set the team ranks on the sorted data.
        setTeamRanks(leagueResults);

        return leagueResults;
    }

    /**
     * Expects a string containing the name of the team followed by a space and then the team's score for that match.
     * E.g. team "GoGetters" with score 10 should have a string as follows: "GoGetters 10"
     * <p>
     * It will then convert this string into a TeamValueobject that has a name and value variable.
     * It should also convert the string score into a number.
     *
     * @param result
     * @param pattern
     * @return
     */
    public static TeamValue getTeamResultFromString(String result, Pattern pattern) {

        // Use regex pattern to match team names that include spaces
        Matcher m = pattern.matcher(result);

        if (m.find()) {

            String team = m.group(1);

            // Remove the space at the end of the team name
            String name = team.substring(0, team.length() - 1);

            // Convert string value into a number.
            Integer value = Integer.valueOf(m.group(2));

            // return a TeamValueclass object
            return new TeamValue(name, value);
        }

        return null;
    }

    /**
     * When this function is called we have a list
     * containing each team's match points for all games played.
     * <p>
     * We want to reduced that list to one that only has
     * one entry for each team, with each new object having it's
     * value represent the sum of all match points gained in the league.
     *
     * @param allTeamMatchPoints
     * @return
     */
    private static List<TeamValue> reduceTeamMatchPoints(List<TeamValue> allTeamMatchPoints) {

        // Using a map here makes it easier to reduce into a single entry per team.
        HashMap<String, Integer> finalTeamPoints = new HashMap<>();

        for (TeamValue matchPoints : allTeamMatchPoints) {

            String name = matchPoints.getName();
            Integer points = matchPoints.getValue();

            // If the name does not exist in the map, it will be initialised with the value of points.
            // Otherwise it will just add this match's points to the previous points value.

            if (!finalTeamPoints.containsKey(name)) {
                finalTeamPoints.put(name, points);
            } else {
                Integer nextPointsTotal = finalTeamPoints.get(name) + points;

                finalTeamPoints.put(name, nextPointsTotal);
            }
        }

        // Convert the map back into a list for better processing later.
        return Utils.convertTeamValueMapToList(finalTeamPoints);
    }

    static void delayedPrint(String text, Integer delay) {
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

    /**
     * Processes a list of the two team scores in a single match
     * and returns a new TeamValueobject for each team where the value parameter
     * represents the points the team received from either Losing/Winning/Drawing the match.
     *
     * @param matchResults
     * @return
     */
    public static List<TeamValue> calculateMatchPoints(List<TeamValue> matchResults) {

        List<TeamValue> matchPoints = new ArrayList<>();

        // Initialise new TeamValueobjects for each team
        // setting initial points to 0

        TeamValue teamA = matchResults.get(0);
        TeamValue teamB = matchResults.get(1);

        String teamAName = teamA.getName();
        Integer teamAGoals = teamA.getValue();
        TeamValue teamAPoints = new TeamValue(teamAName, 0);

        String teamBName = teamB.getName();
        Integer teamBGoals = teamB.getValue();
        TeamValue teamBPoints = new TeamValue(teamBName, 0);

        // Match is a DRAW
        if (teamAGoals.equals(teamBGoals)) {

            teamAPoints.setValue(1);
            teamBPoints.setValue(1);

            // Team A WON
        } else if (teamAGoals > teamBGoals) {
            teamAPoints.setValue(3);

            // Team B WON
        } else {
            teamBPoints.setValue(3);
        }

        // Add the new objects to an empty list
        matchPoints.add(teamAPoints);
        matchPoints.add(teamBPoints);

        return matchPoints;
    }
}
