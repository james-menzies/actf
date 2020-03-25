package autocompletetextfield;

import java.util.function.BiPredicate;

public interface ISearchPredicate<T> extends BiPredicate<String, String> {

    default String convertToString(T ob) {
        return ob.toString();
    }

    @Override
    boolean test(String userInput, String option );
}
