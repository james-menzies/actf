package autocompletetextfield;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
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

public class AutoCompleteTextField<T> extends TextField {

    private AutoCompleteTextFieldVM<T> viewModel;
    private DoubleProperty rowHeight;
    private IntegerProperty maxRows;
    private static double DEFAULT_ROW_HEIGHT = 25.0;
    private static int DEFAULT_MAX_ROWS = 10;



    private static final BiPredicate<String, String> DEFAULT_MATCHING_ALGORITHM =
            (s1, s2) -> CamelCaseMatch.test(s1, s2, '\'', '-');

    public AutoCompleteTextField(ObservableList<T> validChoices) {

        this(validChoices, DEFAULT_MATCHING_ALGORITHM, Object::toString);
    }

    AutoCompleteTextField(ObservableList<T> validChoices,
                          BiPredicate<String, String> matchingAlgorithm,
                          Function<T, String> objectConversion) {

        viewModel = new AutoCompleteTextFieldVM<>
                (validChoices, matchingAlgorithm, objectConversion);

        ListView<String> listView = new ListView<>();
        listView.minWidthProperty().bind(widthProperty());
        listView.setItems(viewModel.suggestionsProperty());
        listView.setPlaceholder(new Label("No Matches"));
        listView.getSelectionModel().select(0);

        rowHeight = new SimpleDoubleProperty();
        rowHeight.addListener( (v, o, n) -> {
            listView.setFixedCellSize((Double) n);
        });
        rowHeight.setValue(DEFAULT_ROW_HEIGHT);

        maxRows = new SimpleIntegerProperty(DEFAULT_MAX_ROWS);

        listView.prefHeightProperty().bind(
                viewModel.createListHeightProperty(maxRows, rowHeight));


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

    public static void setDefaultRowHeight(double defaultRowHeight) {
        DEFAULT_ROW_HEIGHT = defaultRowHeight;
    }

    public static void setDefaultMaxRows(int defaultMaxRows) {
        DEFAULT_MAX_ROWS = defaultMaxRows;
    }

    public void setRowHeight(double rowHeight) {
        this.rowHeight.set(rowHeight);
    }

    public void setMaxRows(int maxRows) {
        this.maxRows.set(maxRows);
    }

    public ObjectProperty<T> selectedObjectProperty() {
        return viewModel.selectedObjectProperty();
    }

    public T getSelectedObject() {
        return viewModel.selectedObjectProperty().get();
    }
}
