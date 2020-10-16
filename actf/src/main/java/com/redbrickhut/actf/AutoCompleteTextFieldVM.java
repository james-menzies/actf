package com.redbrickhut.actf;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
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

class AutoCompleteTextFieldVM<T> {

    private final StringProperty userInput;
    private final ReadOnlyObjectWrapper<T> selectedObject;
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

    ReadOnlyObjectProperty<T> selectedObjectProperty() {
        return selectedObject.getReadOnlyProperty();
    }


    AutoCompleteTextFieldVM(ObservableList<T> validChoices) {

        this(validChoices, CamelCaseMatch::test, Object::toString);
    }

    AutoCompleteTextFieldVM(ObservableList<T> validChoices,
                            BiPredicate<String, String> matchingAlgorithm,
                            Function<T, String> objectConversion) {

        userInput = new SimpleStringProperty("");

        this.matchingAlgorithm = matchingAlgorithm;
        this.objectConversion = objectConversion;

        stringToObject = initStringToObject(validChoices);
        selectedObject = initSelectedObject();
        selectedSuggestion = new SimpleStringProperty("");
        suggestions = getSuggestions();

    }

    private Map<String, T> initStringToObject(ObservableList<T> validChoices) {
        return validChoices.stream()
                .collect(Collectors.toMap
                        (objectConversion, t -> t,
                                (t, t2) -> t));

    }

    private ReadOnlyObjectWrapper<T> initSelectedObject() {

        ReadOnlyObjectWrapper<T> selectedObject = new ReadOnlyObjectWrapper<>();

        selectedObject.bind(Bindings.createObjectBinding(() ->
                stringToObject.getOrDefault(userInput.get(), null), userInput));

        return selectedObject;
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

    private List<String> getValidChoicesAsString() {
        return new ArrayList<>(stringToObject.keySet());
    }

    void handleUserSelect() {

        String currentSelection =
                selectedSuggestionProperty().get();

        if (currentSelection != null) {
            userInputProperty().set(currentSelection);
        }
    }

    DoubleBinding createListHeightProperty(IntegerProperty maxRows, DoubleProperty cellHeight) {

        return Bindings.createDoubleBinding(() -> {

            if (suggestions.size() == 0) {
                return 75.0;
            } else if(suggestions.size() >= maxRows.getValue()) {
                return maxRows.getValue() * cellHeight.getValue() + 2;
            }
            else return suggestions.size() * cellHeight.getValue() + 2;
        }, maxRows, cellHeight, suggestions);

    }
}
