package com.probending.probending.util;

import java.util.ArrayList;
import java.util.List;

public class UtilMethods {


    // Very useful for AutoComplete, in args just place args and in List place all of the autocomplete strings, and it will neatly output a list with alphabetical order.
    public static List<String> getPossibleCompletions(String[] args, List<String> possibilitiesOfCompletion) {
        String argumentToFindCompletionFor = args[args.length - 1];
        ArrayList<String> listOfPossibleCompletions = new ArrayList<>();

        for (String foundString : possibilitiesOfCompletion) {
            if (foundString.regionMatches(true, 0, argumentToFindCompletionFor, 0, argumentToFindCompletionFor.length())) {
                listOfPossibleCompletions.add(foundString);
            }
        }
        return listOfPossibleCompletions;
    }

}
