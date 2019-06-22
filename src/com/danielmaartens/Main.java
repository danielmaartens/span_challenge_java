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

        HashMap<String, Integer> finalTeamPoints = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            List<List<String>> results = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                String[] matchResults = line.split(",");

                for (String result:
                     matchResults) {
                    String[] teamResults = result.split(" ");
                    results.add(Arrays.asList(teamResults));
                }

                allResults.add(results);
            }
        }
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
}
