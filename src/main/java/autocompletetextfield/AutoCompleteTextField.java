package autocompletetextfield;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.event.Event;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Popup;


public class AutoCompleteTextField<T> extends TextField {

    private ObjectProperty<T> selectedObject;
    private AutoCompleteTextFieldVM<T> viewModel;

    public AutoCompleteTextField() {

        viewModel = new AutoCompleteTextFieldVM<>();

        ListView<String> listView = new ListView<>();
        listView.minWidthProperty().bind(widthProperty());
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

    private ObjectProperty<T> getSelectedObject() {
        return viewModel.getSelectedObject();
    }

}
