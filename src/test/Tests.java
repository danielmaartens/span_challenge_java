package test;

import static org.junit.jupiter.api.Assertions.*;

import main.TeamValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.nio.file.Paths;

public class Tests {

    private final TeamValue teamValue = new TeamValue("GoGetters", 10);

    private final Pattern teamResultGroupingPattern = Pattern.compile(Utils.TEAM_RESULT_GROUPING_PATTERN);

    private final String file = Paths.get("input.csv").toString();

    private HashMap<String, Integer> matchPointsMap;
    private List<TeamValue> finalRank;

    @Test
    @DisplayName("Check main.TeamValue.getName() returns correct value")
    void test_getName() {
        assertEquals("GoGetters", teamValue.getName());
    }

    @Test
    @DisplayName("Check main.TeamValue.getValue() returns correct value and of type int")
    void test_getValue() {
        assertEquals(10, teamValue.getValue());
    }

    @Test
    @DisplayName("getTeamResultFromString function should correctly parse the input string into a team name (handling space in between) and a value of type int.")
    void teamResultFromString() {
        TeamValue teamResult = Utils.getTeamResultFromString("FC Awesome 1", teamResultGroupingPattern);
        assertAll("Team Result",
                () -> {
                    String name = teamResult.getName();
                    assertNotNull(name);

                    assertEquals("FC Awesome", name);
                },

                () -> assertEquals(1, teamResult.getValue())
        );
    }

    @Nested
    @DisplayName("Correct Team Order and Match Points")
    class FinalResult {

        @BeforeEach
        void initialiseFinalResult() throws Exception {
            finalRank = Utils.getLeagueResults(file);
        }

        @Test
        @DisplayName("Tarantulas should be 1st with 6pts")
        void firstTeam() {
            TeamValue team = finalRank.get(0);
            assertAll(
                    () -> assertEquals("Tarantulas", team.getName()),
                    () -> assertEquals(1, team.getRank()),
                    () -> assertEquals(6, team.getValue())
            );
        }

        @Test
        @DisplayName("Lions should be 2nd with 5pts")
        void secondTeam() {
            TeamValue team = finalRank.get(1);
            assertAll(
                    () -> assertEquals("Lions", team.getName()),
                    () -> assertEquals(2, team.getRank()),
                    () -> assertEquals(5, team.getValue())
            );
        }

        @Test
        @DisplayName("FC Awesome should be 3rd with 1pt")
        void thirdTeam() {
            TeamValue team = finalRank.get(2);
            assertAll(
                    () -> assertEquals("FC Awesome", team.getName()),
                    () -> assertEquals(3, team.getRank()),
                    () -> assertEquals(1, team.getValue())
            );
        }

        @Test
        @DisplayName("Snakes should be 3rd (after FC Awesome) with 1pt")
        void fourthTeam() {
            TeamValue team = finalRank.get(3);
            assertAll(
                    () -> assertEquals("Snakes", team.getName()),
                    () -> assertEquals(3, team.getRank()),
                    () -> assertEquals(1, team.getValue())
            );
        }

        @Test
        @DisplayName("Grouches should be 5th with 0pts")
        void fifthTeam() {
            TeamValue team = finalRank.get(4);
            assertAll(
                    () -> assertEquals("Grouches", team.getName()),
                    () -> assertEquals(5, team.getRank()),
                    () -> assertEquals(0, team.getValue())
            );
        }
    }

    @Nested
    @DisplayName("Calculate Win Lose Match Points")
    class WinLose {

        @BeforeEach
        void initialiseTeamPointsMap() {
            TeamValue teamAResult = new TeamValue("A", 1);
            TeamValue teamBResult = new TeamValue("B", 0);
            List<TeamValue> matchResults = new ArrayList<>();
            matchResults.add(teamAResult);
            matchResults.add(teamBResult);

            List<TeamValue> matchPoints = Utils.calculateMatchPoints(matchResults);
            matchPointsMap = Utils.convertTeamValueListToMap(matchPoints);
        }

        @Test
        @DisplayName("Team A WON so they should have 3 match points.")
        void matchPointsForTeamA() {

            assertEquals(3, matchPointsMap.get("A"));

        }

        @Test
        @DisplayName("Team B LOST so they should have 0 match points")
        void matchPointsForTeamB() {

            assertEquals(0, matchPointsMap.get("B"));

        }
    }


    @Nested
    @DisplayName("Calculate Draw Match Points")
    class Draw {

        @BeforeEach
        void initialiseTeamPointsMap() {
            TeamValue teamAResult = new TeamValue("A", 1);
            TeamValue teamBResult = new TeamValue("B", 1);
            List<TeamValue> matchResults = new ArrayList<>();
            matchResults.add(teamAResult);
            matchResults.add(teamBResult);

            List<TeamValue> matchPoints = Utils.calculateMatchPoints(matchResults);
            matchPointsMap = Utils.convertTeamValueListToMap(matchPoints);
        }

        @Test
        @DisplayName("Team A should have 1 match point.")
        void matchPointsForTeamA() {

            assertEquals(1, matchPointsMap.get("A"));

        }

        @Test
        @DisplayName("Team B should have 1 match point.")
        void matchPointsForTeamB() {

            assertEquals(1, matchPointsMap.get("B"));

        }
    }
}


