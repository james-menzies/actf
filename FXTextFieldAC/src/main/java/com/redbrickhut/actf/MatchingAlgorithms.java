package com.redbrickhut.actf;

import java.util.function.BiPredicate;

public class MatchingAlgorithms {

    public final static BiPredicate<String, String> CAMEL_MATCH =
            CamelCaseMatch::test;

    public static BiPredicate<String, String> getCamelMatch(char... customWordChars) {
        return (x, y) -> CamelCaseMatch.test(x, y, customWordChars);
    }

    public final static BiPredicate<String, String> SUB_STRING_MATCH =
            (s1, s2) -> {

                if (s1.isEmpty()) {
                    return true;
                } else return s2.toLowerCase().contains(s1.toLowerCase());
            };

    public final static BiPredicate<String, String> EXACT_MATCH =
            (s1, s2) -> s2.toLowerCase().indexOf(s1.toLowerCase()) == 0;


}
