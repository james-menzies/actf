package autocompletetextfield;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ListBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AutoCompleteTextFieldVM<T> {

    private final StringProperty userInput;
    private final ObjectProperty<T> selectedObject;
    private final Map<String, T> stringToObject;
    private final ListBinding<String> suggestions;
    private final StringProperty selectedSuggestion;
    private final BiPredicate<String, String> matchingAlgorithm;
    private final Function<T, String> objectConversion;

    StringProperty userInputProperty() {
        return userInput;
    }

    ObservableList<String> suggestionsProperty() {
        return suggestions;
    }

    StringProperty selectedSuggestionProperty() {
        return selectedSuggestion;
    }

    ObjectProperty<T> selectedObjectProperty() {
        return selectedObject;
    }

    void handleUserSelect() {

        String currentSelection =
                selectedSuggestionProperty().get();

        if (currentSelection != null) {
            userInputProperty().set(currentSelection);
        }
    }

    AutoCompleteTextFieldVM(ObservableList<T> validChoices) {

        this.selectedObject = new SimpleObjectProperty<>();
        userInput = new SimpleStringProperty("");

        matchingAlgorithm = CamelCaseMatch::test;
        objectConversion = Object::toString;

        stringToObject = getStringToObject(validChoices);
        initializeSelectedObject();
        selectedSuggestion = new SimpleStringProperty("");
        suggestions = getSuggestions();
    }

    private Map<String, T> getStringToObject(ObservableList<T> validChoices) {
        return validChoices.stream()
                .collect(Collectors.toMap
                        (objectConversion, t -> t,
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

    private ListBinding<String> getSuggestions() {
        return new ListBinding<>() {

            {
                super.bind(userInput);
            }

            @Override
            protected ObservableList<String> computeValue() {

                return getNewStrings();
            }
        };
    }

    private ObservableList<String> getNewStrings() {
        return FXCollections.observableList(
                getValidChoicesAsString()
                        .stream()
                        .filter((s -> matchingAlgorithm.test(userInput.get(), s)))
                        .collect(Collectors.toList())
        );
    }
}
