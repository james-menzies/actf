package autocompletetextfield;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.Rule;
import org.junit.Test;
import utils.JavaFXThreadingRule;
import utils.Person;
import utils.SamplePersonData;

import java.io.IOException;

public class AutoCompleteTextFieldViewTest  {


    @FXML
    private ListView<Person> display;

    @FXML
    private TextField input;

    @FXML
    private void initialize() {

        AutoCompleteTextFieldVM<Person> viewModel;
        ObservableList<Person> list =
                FXCollections.observableArrayList(SamplePersonData.get());

        display.setItems(FXCollections.observableArrayList(list));

        display.setCellFactory( lv -> {
            return new ListCell<>() {
                @Override
                protected void updateItem(Person item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null)
                    setText(String.format("%s %s", item.getFirstName(), item.getLastName()));
                }
            };
        });

        ISearchPredicate<Person> predicate =
                new ISearchPredicate<Person>() {
                    @Override
                    public boolean test(String userInput, String option) {
                        return SmartStringSearch.test(userInput, option);
                    }
                };

        viewModel = new AutoCompleteTextFieldVM<>(
                new SimpleObjectProperty<>(),
                list, predicate
        );

        input = new AutoCompleteTextFieldView(viewModel);

    }

    @Rule
    public JavaFXThreadingRule javaFXThreadingRule = new JavaFXThreadingRule();

    @Test
    public void showAnyStage() {

        Stage stage = new Stage();
        Parent root;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/autocompleteshell.fxml"));
            loader.setController(this);
            root = (Parent) loader.load();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}