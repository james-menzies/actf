/*
package autocompletetextfield;

import org.junit.Test;
import utils.SamplePersonData;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;


public class SearchPredicateTest {

    @Test
    public void createCustomPredicate() {

        ISearchPredicate<LocalDate> myPred = new ISearchPredicate<LocalDate>() {


            @Override
            public boolean test(String input, String object) {
                return object.contains(input);
            }
        };

        LocalDate testDate = LocalDate.of(2020, 1, 1);
        String convertedDate = myPred.convertToString(testDate);

        assertEquals(true, myPred.test("2", convertedDate));

    }

    @Test
    public void nonames() {
        System.out.println(SamplePersonData.get());
    }

}*/
