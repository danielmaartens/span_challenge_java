import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        Boolean running = true;

        System.out.println("\nWelcome to the Match Point Calculator !\n");

        Integer initialDelay = 1500;
        Integer delay = initialDelay;

        Utils.delayedPrint("This program will calculate the ranking table for a soccer league.\n", delay);
        delay += initialDelay;

        Utils.delayedPrint("The data for the results of the games should be stored in a text file.", delay);
        delay += initialDelay;

        while (running) {

            Utils.delayedPrint("\nPlease provide the full path of the file where your results are stored:\n", delay);
            Utils.delayedPrint("Full File Path: ", delay + 10);

            String file = scanner.next();

            try {
                System.out.println("\nRESULTS\n");

                List<TeamValue> finalTeamMatchPoints = Utils.getOrderedMatchPointsFromFile(file);

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

}
