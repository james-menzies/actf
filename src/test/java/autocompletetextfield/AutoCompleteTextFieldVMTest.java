package autocompletetextfield;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.Before;
import org.junit.Test;
import utils.Person;

import java.util.function.BiPredicate;
import static org.junit.Assert.*;

public class AutoCompleteTextFieldVMTest {

    private AutoCompleteTextFieldVM<Person> viewModel;
    private ObservableList<Person> samplePersonList;
    private BiPredicate<String, String> matchingAlgorithm;

    private final Person p1 = new Person("James", "Menzies");
    private final Person p2 = new Person("Emma", "Menzies");
    private final Person p3 = new Person("Hayato", "Simpson");
    private final Person p4 = new Person("Doug", "Stanhope");
    private final Person p5 = new Person("Billy", "Smith");

    @Before
    public void before() {

        samplePersonList = FXCollections.observableArrayList(
               p1, p2, p3, p4, p5
        );

        matchingAlgorithm = (s1, s2) ->
                s2.toLowerCase().contains(s1.toLowerCase());

        viewModel = new AutoCompleteTextFieldVM<>
                (samplePersonList, matchingAlgorithm, Object::toString);
    }


    @Test
    public void validChoicesPopulateCorrectly() {

        assertTrue(matchingAlgorithm.test("Menzies", p1.toString()));
        assertFalse(matchingAlgorithm.test("JamesMeng", p1.toString()));
    }

    @Test
    public void chosenObjectVariableBehavesCorrectly() {


    }
}