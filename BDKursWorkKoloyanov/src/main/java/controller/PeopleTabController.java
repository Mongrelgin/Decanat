package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.DataBaseManager;
import model.People;
import model.Group;
import model.UserRole;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class PeopleTabController {

    @FXML
    private TableView<People> tableView;

    @FXML
    private TableColumn<People, Integer> id;

    @FXML
    private TableColumn<People, String> name;

    @FXML
    private TableColumn<People, String> lastName;

    @FXML
    private TableColumn<People, String> partherName;

    @FXML
    private TableColumn<People, Character> type;

    @FXML
    private TableColumn<People, Group> group;

    @FXML
    private Button addButton;

    @FXML
    private Button changeButton;

    @FXML
    private Button deleteButton;

    ObservableList<People> array = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        if(DataBaseManager.getShared().getUser().getUserRole() != UserRole.admin) {
            addButton.setDisable(true);
            changeButton.setDisable(true);
            deleteButton.setDisable(true);
        }
        array = FXCollections.observableArrayList(getData());
        fillTableView(array);

    }

    @FXML
    void addButtonAction() {
        Stage addEditView = new Stage();
        try {
            Parent addEditViewRoot = FXMLLoader.load(getClass().getResource("../view/AddEditPeopleView.fxml"));
            addEditView.setScene(new Scene(addEditViewRoot, 465, 374));
            addEditView.setTitle("Добавить запись");
            addEditView.setResizable(false);
            addEditView.setFullScreen(false);
            addEditView.show();
            addEditView.setOnCloseRequest(windowEvent -> {
                array = FXCollections.observableArrayList(getData());
                tableView.setItems(array);
                tableView.refresh();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void changeButtonAction() {
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            Stage addEditView = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/AddEditPeopleView.fxml"));
            try {
                Parent addEditViewRoot = loader.load();
                addEditView.setTitle("Редактировать запись");
                addEditView.setScene(new Scene(addEditViewRoot, 465, 374));
                addEditView.setResizable(false);
                addEditView.setFullScreen(false);
                AddEditPeopleController addEditPeopleController = loader.getController();
                addEditView.show();
                addEditPeopleController.setData(tableView.getSelectionModel().getSelectedItem());
                addEditView.setOnCloseRequest(windowEvent -> {
                    array = FXCollections.observableArrayList(getData());
                    tableView.setItems(array);
                    tableView.refresh();
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Внимание!", "Выберите запись для редактирования!", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    void deleteButtonAction() {
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            if (showQuestionAlert()) {
                try {
                    DataBaseManager.getShared().deletePeople(tableView.getSelectionModel().getSelectedItem());
                    array = FXCollections.observableArrayList(getData());
                    tableView.setItems(array);
                    tableView.refresh();
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    showAlert("Ошибка запроса!", "Не удалось выполнить запрос!", Alert.AlertType.ERROR);
                }
            }
        } else {
            showAlert("Внимание!", "Выберите запись для удаления!", Alert.AlertType.INFORMATION);
        }
    }

    private void fillTableView(ObservableList<People> array) {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        partherName.setCellValueFactory(new PropertyValueFactory<>("partherName"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        group.setCellValueFactory(new PropertyValueFactory<>("group"));
        tableView.setItems(array);
    }

    private ArrayList<People> getData() {
        ArrayList<People> result = new ArrayList<>();

        try {
            result = DataBaseManager.getShared().getPeople();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void showAlert(String title ,String message,  Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    private boolean showQuestionAlert() {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Удалить запись?");
        alert.setHeaderText("Вы уверены что хотить удалить запись?");
        ButtonType yes = new ButtonType("Да");
        alert.getButtonTypes().addAll(yes);
        alert.getButtonTypes().addAll(new ButtonType("Нет"));
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == yes;
    }
}