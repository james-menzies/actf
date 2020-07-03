package autocompletetextfield;

import java.util.function.BiPredicate;

public class Presets {

    public final static BiPredicate<String, String> CAMEL_MATCH =
            CamelCaseMatch::test;

    public final static BiPredicate<String, String> SUB_STRING_MATCH =
            (s1, s2) -> {

                if (s1.isEmpty()) {
                    return true;
                } else return s2.toLowerCase().contains(s1.toLowerCase());
            };

    public final static BiPredicate<String, String> EXACT_MATCH =
            (s1, s2) -> s2.indexOf(s1) == 0;

}
