package autocompletetextfield;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.Before;
import org.junit.Test;
import utils.Person;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class AutoCompleteTextFieldVMTest {

    private AutoCompleteTextFieldVM<Person> viewModel;
    private ObservableList<Person> samplePersonList;
    private BiPredicate<String, String> matchingAlgorithm;

    private final Person p1 = new Person("James", "Menzies", null, null, null);
    private final Person p2 = new Person("Emma", "Menzies", null, null, null);
    private final Person p3 = new Person("Hayato", "Simpson", null, null, null);
    private final Person p4 = new Person("Doug", "Stanhope", null, null, null);
    private final Person p5 = new Person("Billy", "Smith", null, null, null);

    @Before
    public void before() {

        samplePersonList = FXCollections.observableArrayList(
               p1, p2, p3, p4, p5
        );

        matchingAlgorithm = MatchingAlgorithms.SUB_STRING_MATCH;

        viewModel = new AutoCompleteTextFieldVM<>
                (samplePersonList, matchingAlgorithm, Object::toString);
    }


    @Test
    public void validChoicesPopulateCorrectly() {

        assertTrue(matchingAlgorithm.test("Menzies", p1.toString()));
        assertFalse(matchingAlgorithm.test("JamesMeng", p1.toString()));
    }

    @Test
    public void selectedObjectRespondsToUserInput() {
        assertNull(viewModel.selectedObjectProperty().get());

        viewModel.userInputProperty().set(p1.toString());

        assertEquals("Person 1 should be the selected object",
                p1, viewModel.selectedObjectProperty().get());

        viewModel.userInputProperty().set("randomtext");

        assertNull("Random chars produce null object",
                viewModel.selectedObjectProperty().get());

        viewModel.userInputProperty().set(p1.toString());

        assertEquals("Person 1 should be re-selected",
                p1, viewModel.selectedObjectProperty().get());
    }

    @Test
    public void selectedObjectRespondsToUserSelection() {

        viewModel.selectedSuggestionProperty().set(p1.toString());
        viewModel.handleUserSelect();

        assertEquals("Person 1 should be the selected object",
                p1, viewModel.selectedObjectProperty().get());

        viewModel.selectedSuggestionProperty().set(p2.toString());
        viewModel.handleUserSelect();

        assertEquals("Person 2 should now be selected.",
                p2, viewModel.selectedObjectProperty().get());
    }

    @Test
    public void manualPopulationTest() {

        List<String> sourceList = manualSamplePersonFilter("");

        assertEquals("Empty string contains full list",
                5, sourceList.size());

        sourceList = manualSamplePersonFilter("Menzies");

        assertEquals("Testing \"Menzies\"",
                2, sourceList.size());

        sourceList = manualSamplePersonFilter("");

        assertEquals("All options re-populate",
                5, sourceList.size());
    }

    private List<String> manualSamplePersonFilter(String input) {
        return samplePersonList.stream()
                .map(Person::toString)
                .filter(str -> matchingAlgorithm.test(input, str))
                .collect(Collectors.toList());
    }

    @Test
    public void suggestionsPopulate() {

        assertEquals("User input should be blank to begin",
                "", viewModel.userInputProperty().get());

        assertEquals("VM suggestions should include all options",
                samplePersonList.size(), viewModel.suggestionsProperty().size());

        List<String> sourceList = manualSamplePersonFilter("Menzies");
        viewModel.userInputProperty().set("Menzies");


        assertTrue("Suggestion should only contain James and Emma Menzies",
                compareLists(sourceList, viewModel.suggestionsProperty()));
    }

    private <V> boolean compareLists(List<V> l1, List<V> l2) {

        boolean sameSize = l1.size() == l2.size();
        boolean sameContents = l1.containsAll(l2);
        return sameContents && sameSize;
    }
}