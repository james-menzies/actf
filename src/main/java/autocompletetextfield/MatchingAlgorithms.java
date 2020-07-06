package autocompletetextfield;

import java.util.function.BiPredicate;

public class MatchingAlgorithms {

    public final static BiPredicate<String, String> CAMEL_MATCH =
            CamelCaseMatch::test;

    // TODO: 3/7/20 Write tests for substring match 
    public final static BiPredicate<String, String> SUB_STRING_MATCH =
            (s1, s2) -> {

                if (s1.isEmpty()) {
                    return true;
                } else return s2.toLowerCase().contains(s1.toLowerCase());
            };

    // TODO: 3/7/20 Write tests for exact match 
    public final static BiPredicate<String, String> EXACT_MATCH =
            (s1, s2) -> s2.indexOf(s1) == 0;

}
