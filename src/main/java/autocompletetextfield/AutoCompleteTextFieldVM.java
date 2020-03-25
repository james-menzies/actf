package autocompletetextfield;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ListBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AutoCompleteTextFieldVM<T> implements  IAutoCompleteTextField<T> {

    private final StringProperty userInput;
    private final ObjectProperty<T> selectedObject;
    private final Map<String, T> stringToObject;
    private final ListBinding<String> suggestions;
    private final StringProperty selectedSuggestion;

    @Override
    public StringProperty userInputProperty() {
        return userInput;
    }

    @Override
    public ObservableList<String> suggestionsProperty() {
        return suggestions;
    }

    @Override
    public StringProperty selectedSuggestionProperty() {
        return selectedSuggestion;
    }

    @Override
    public ObjectProperty<T> getSelectedObject() {
        return selectedObject;
    }

    @Override
    public void handleUserSelect() {

        String currentSelection =
                selectedSuggestionProperty().get();

        if (currentSelection != null) {
            userInputProperty().set(currentSelection);
        }
    }

    public AutoCompleteTextFieldVM
            (ObjectProperty<T> selectedObject,
            ObservableList<T> validChoices,
            ISearchPredicate<T> searchPredicate) {

        this.selectedObject = selectedObject;
        userInput = new SimpleStringProperty("");
        initializeSelectedObject();
        selectedSuggestion = new SimpleStringProperty("");
        stringToObject =
                getStringToObject(validChoices, searchPredicate);
        var validChoicesAsString = getValidChoicesAsString();
        suggestions = getSuggestions(searchPredicate, validChoicesAsString);
    }

    private Map<String, T> getStringToObject(ObservableList<T> validChoices,
                                             ISearchPredicate<T> searchPredicate) {
        return validChoices.stream()
                .collect(Collectors.toMap
                        (searchPredicate::convertToString, t -> t,
                                (t, t2) -> t));

    }

    private List<String> getValidChoicesAsString() {
        return new ArrayList<>(stringToObject.keySet());
    }

    private void initializeSelectedObject() {

        selectedObject.bind(Bindings.createObjectBinding(() -> {
            return stringToObject.getOrDefault(userInput.get(), null);
        }, userInput));
    }

    private ListBinding<String> getSuggestions(ISearchPredicate<T> searchPredicate,
                                               List<String> validChoicesAsString) {
        return new ListBinding<>() {

            {
                super.bind(userInput);
            }

            @Override
            protected ObservableList<String> computeValue() {

                return getNewStrings(validChoicesAsString, searchPredicate);
            }
        };
    }

    private ObservableList<String> getNewStrings(List<String> validChoicesAsString,
                                                 ISearchPredicate<T> searchPredicate) {
        return FXCollections.observableList(
                validChoicesAsString
                        .stream()
                        .filter((s -> searchPredicate.test(userInput.get(), s)))
                        .collect(Collectors.toList())
        );
    }
}
