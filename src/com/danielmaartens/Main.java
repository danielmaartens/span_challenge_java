package com.danielmaartens;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {

        List<List<List<String>>> allResults = new ArrayList<>();

        String file = "";

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
}
