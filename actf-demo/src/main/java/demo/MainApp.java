package demo;

import com.redbrickhut.actf.AutoCompleteTextField;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import utils.Person;
import utils.SamplePersonData;

import java.io.IOException;
import java.util.function.Function;

public class MainApp extends Application {


    @FXML
    private ListView<Person> display;

    @FXML
    private HBox inputDropIn;

    @FXML
    private GridPane selectedPersonInfo;

    @FXML
    private Label selectedFirstName;

    @FXML
    private Label selectedLastName;

    @FXML
    private Label selectedEmail;

    @FXML
    private Label selectedPhone;

    @FXML
    private Label selectedCountry;

    private AutoCompleteTextField<Person> actf;

    public static void main(String[] args) {

        Application.launch(args);
    }

    @FXML
    private void initialize() {

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



        actf = new AutoCompleteTextField<>(list);
        HBox.setHgrow(actf, Priority.ALWAYS);
        inputDropIn.getChildren().add(actf);

        actf.setMaxRows(5);
        actf.setRowHeight(35);

        bindPersonAttributeLabel(selectedFirstName, Person::getFirstName);
        bindPersonAttributeLabel(selectedLastName, Person::getLastName);
        bindPersonAttributeLabel(selectedPhone, Person::getPhone);
        bindPersonAttributeLabel(selectedCountry, Person::getCountry);
        bindPersonAttributeLabel(selectedEmail, Person::getEmail);
    }

    private void bindPersonAttributeLabel(Label label, Function<Person, String> function) {

        label.textProperty()
                .bind(Bindings.createStringBinding( () -> {

                    if (actf.getSelectedObject() != null) {
                        return function.apply(actf.getSelectedObject());
                    }
                    else return "";
                }, actf.selectedObjectProperty()));
    }

    @Override
    public void start(Stage stage) throws Exception {

        Parent root;

        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/demo/autocompleteshell.fxml"));
            loader.setController(this);
            root = (Parent) loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle("FX Auto-Complete TextField Demo");
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

