package utils;

import com.danielmaartens.TeamValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    public static HashMap<String, Integer> convertTeamValueListToMap(List<TeamValue> teamValues) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        for (TeamValue team : teamValues) map.put(team.getName(), team.getValue());

        return map;
    }


    public static List<TeamValue> convertTeamValueMapToList(HashMap<String, Integer> teamValuesMap) {
        List<TeamValue> list = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : teamValuesMap.entrySet()) {

            TeamValue team = new TeamValue(entry.getKey(), entry.getValue());

            list.add(team);
        }

        return list;
    }

    public static Boolean booleanFromString(String s) {
        String lowerCaseS = s.toLowerCase();

        switch (lowerCaseS) {
            case "y":
            case "yes":
                return true;
            case "n":
            case "no":
                return false;
            default:
                return null;
        }

    }
}
