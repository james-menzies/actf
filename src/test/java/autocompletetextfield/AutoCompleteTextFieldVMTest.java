package autocompletetextfield;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.Person;

import static org.junit.Assert.*;

public class AutoCompleteTextFieldVMTest {

    private AutoCompleteTextFieldVM<Person> viewModel;
    private ObservableList<Person> samplePersonList;
    private ObjectProperty<Person> selectedValue;
    private ISearchPredicate<Person> searchPredicate;

    private Person p1 = new Person("James", "Menzies");
    private Person p2 = new Person("Emma", "Menzies");
    private Person p3 = new Person("Hayato", "Simpson");
    private Person p4 = new Person("Doug", "Stanhope");
    private Person p5 = new Person("Billy", "Smith");


    @Before
    public void before() {

        samplePersonList = FXCollections.observableArrayList(
               p1, p2, p3, p4, p5
        );

        selectedValue = new SimpleObjectProperty<>();

        searchPredicate = new ISearchPredicate<Person>() {

            @Override
            public boolean test(String userInput, String option) {
                return option.toLowerCase().contains(userInput.toLowerCase());
            }
        };

        viewModel = new AutoCompleteTextFieldVM<>
                (selectedValue, samplePersonList, searchPredicate);
    }

    @Test
    public void personToStringBehavesAsExpected() {

        Assert.assertEquals("MENZIES, James", samplePersonList.get(0).toString());
    }

    @Test
    public void validChoicesPopulateCorrectly() {

        ObservableList<String> suggestions =
                viewModel.suggestionsProperty();

        Assert.assertEquals("Suggestions initially contain all options",
                samplePersonList.size(), suggestions.size());

        viewModel.userInputProperty().set("me");
        Assert.assertEquals("size of list after typing 'me'",
                2, suggestions.size());
        assertTrue("Contains James Menzies",
                suggestions.contains(p1.toString()));
        assertTrue("Contains Emma Menzies",
                suggestions.contains(p2.toString()));

        viewModel.userInputProperty().set("");
        Assert.assertEquals("On resetting input, list populates back to max size",
                samplePersonList.size(), suggestions.size());
    }

    @Test
    public void chosenObjectVariableBehavesCorrectly() {

        assertNull("Object should be null to begin with.",
                viewModel.selectedObjectProperty().get());

        viewModel.userInputProperty().set(searchPredicate.convertToString(p1));
        assertEquals(samplePersonList.get(0), viewModel.selectedObjectProperty().get());

        viewModel.userInputProperty().set("random nonsensical garbage");
        assertNull("Object reverts to null when string stops matching.",
                viewModel.selectedObjectProperty().get());
    }
}