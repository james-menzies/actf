package autocompletetextfield;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Popup;

import java.util.function.BiPredicate;
import java.util.function.Function;

// TODO: 3/7/20 Write documentation!
public class AutoCompleteTextField<T> extends TextField {

    private AutoCompleteTextFieldVM<T> viewModel;

    public AutoCompleteTextField(ObservableList<T> validChoices) {

        this(validChoices, CamelCaseMatch::test, Object::toString);
    }

    AutoCompleteTextField(ObservableList<T> validChoices,
                          BiPredicate<String, String> matchingAlgorithm,
                          Function<T, String> objectConversion) {

        viewModel = new AutoCompleteTextFieldVM<>
                (validChoices, matchingAlgorithm, objectConversion);

        ListView<String> listView = new ListView<>();
        listView.minWidthProperty().bind(widthProperty());
        // TODO: 3/7/20 optimize pop up window size
        listView.setItems(viewModel.suggestionsProperty());
        listView.setPlaceholder(new Label("No Matches"));
        listView.getSelectionModel().select(0);


        Popup popup = new Popup();
        popup.getContent().add(listView);
        popup.setAutoHide(true);

        viewModel.selectedSuggestionProperty().bind(listView.getSelectionModel().selectedItemProperty());
        viewModel.userInputProperty().bindBidirectional(textProperty());

        textProperty().addListener( (observable ->  {
            if (isFocused() && !popup.isShowing()) {
                Bounds bounds = localToScreen(getBoundsInLocal());
                popup.show(this, bounds.getMinX(), bounds.getMaxY());
            }
        }));

        listView.itemsProperty().get().addListener((InvalidationListener) observable -> {
            Platform.runLater(() -> listView.getSelectionModel().select(0));
        });

        listView.setOnKeyPressed( e ->
        {
            if (e.getCode() == KeyCode.TAB) {
                viewModel.handleUserSelect();
                popup.hide();
                Event.fireEvent(this, e);
            }
        });
    }

    public ObjectProperty<T> selectedObjectProperty() {
        return viewModel.selectedObjectProperty();
    }

    public T getSelectedObject() {
        return viewModel.selectedObjectProperty().get();
    }
}
