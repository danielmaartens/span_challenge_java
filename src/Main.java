import java.io.File;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        Boolean running = true;
        String userInput;
        Print print = new Print(1500);

        print.ln("\nWelcome to the League Rank Calculator !\n");

        print.withDelay("This program will calculate the ranking table for a soccer league.\n");

        print.withDelay("The data for the results of the games should be stored in a text file.");

        while (running) {

            print.withDelay("\nPlease provide the full path of the file where your results are stored:\n");
            print.withDelay("Full File Path To Data: ", print.getCurrentDelay(10));

            String file = scanner.next();

            if (new File(file).exists()) {
                try {
                    System.out.println("\nLEAGUE RANK RESULTS\n");

                    List<TeamValue> finalTeamMatchPoints = Utils.getLeagueResults(file);

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

        System.out.println("\nThank you for using the League Rank Calculator !");
        System.exit(0);


    }

}
