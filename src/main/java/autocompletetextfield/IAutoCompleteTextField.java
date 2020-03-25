package autocompletetextfield;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

public interface IAutoCompleteTextField<T> {

    StringProperty userInputProperty();
    ObservableList<String> suggestionsProperty();
    StringProperty selectedSuggestionProperty();
    ObjectProperty<T> getSelectedObject();
    void handleUserSelect();
}
