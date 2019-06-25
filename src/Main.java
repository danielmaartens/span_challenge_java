import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        Boolean running = true;
        String userInput;

        System.out.println("\nWelcome to the League Points Calculator !\n");

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

            if (Utils.fileExists(file)) {
                try {
                    System.out.println("\nRESULTS\n");

                    List<TeamValue> finalTeamMatchPoints = Utils.getTeamRank(file);

                    for (TeamValue team : finalTeamMatchPoints) {

                        Integer points = team.getValue();
                        System.out.println(team.getRank() + ". " + team.getName() + ", " + points + (points.equals(1) ? " pt" : " pts"));

                    }

                    System.out.println("\nWould you like to check match point results of another league ? [y/n]: ");

                    userInput = scanner.next();

                    Boolean answerYes = Utils.booleanFromString(userInput);

                    while (answerYes == null) {
                        System.out.println("\nI do not understand your command, please try again...");
                        System.out.println("Would you like to check match point results of another league ? [y/n]: ");

                        userInput = scanner.next();

                        answerYes = Utils.booleanFromString(userInput);
                    }

                    running = answerYes;

                } catch (Exception e) {
                    System.out.println("Something went wrong while trying to calculate the match points: " + e.getMessage());
                }

            } else {
                System.out.println("\nSorry, your file does not exist ! Please double-check your file path and try again... Press [c] to continue, or any other key (besides ENTER) to exit...\n");

                userInput = scanner.next();

                running = (Boolean) Utils.booleanFromString(userInput);

            }
        }

        System.out.println("\nThank you for using the League Points Calculator !");
        System.exit(0);


    }

}
