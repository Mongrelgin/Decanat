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
import model.Marks;
import model.UserRole;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class MarksTabController {

    @FXML
    private TableView<Marks> tableView;

    @FXML
    private TableColumn<Marks, Integer> id;

    @FXML
    private TableColumn<Marks, String> student;

    @FXML
    private TableColumn<Marks, String> subject;

    @FXML
    private TableColumn<Marks, String> teacher;

    @FXML
    private TableColumn<Marks, Integer> value;

    @FXML
    private Button addButton;

    @FXML
    private Button changeButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button reportButton;

    @FXML
    void addButtonAction() {
        Stage addEditView = new Stage();
        try {
            Parent addEditViewRoot = FXMLLoader.load(getClass().getResource("../view/AddEditMarksView.fxml"));
            addEditView.setScene(new Scene(addEditViewRoot, 573, 364));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/AddEditMarksView.fxml"));
            try {
                Parent addEditViewRoot = loader.load();
                addEditView.setTitle("Редактировать запись");
                addEditView.setScene(new Scene(addEditViewRoot, 573, 364));
                addEditView.setResizable(false);
                addEditView.setFullScreen(false);
                AddEditMarksController addEditMarksController = loader.getController();
                addEditView.show();
                addEditMarksController.setData(tableView.getSelectionModel().getSelectedItem());
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
                    DataBaseManager.getShared().deleteProject(tableView.getSelectionModel().getSelectedItem());
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

    @FXML
    void reportButtonAction() {
        Stage reportView = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/reportView.fxml"));
        try {
            Parent reportViewRoot = loader.load();
            reportView.setTitle("Отчет");
            reportView.setScene(new Scene(reportViewRoot, 752, 413));
            reportView.setResizable(false);
            reportView.setFullScreen(false);
            reportView.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ObservableList<Marks> array = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        if(DataBaseManager.getShared().getUser().getUserRole() != UserRole.admin) {
            addButton.setDisable(true);
            changeButton.setDisable(true);
            deleteButton.setDisable(true);
            reportButton.setDisable(true);
        }
        array = FXCollections.observableArrayList(getData());
        fillTableView(array);
    }

    private void fillTableView(ObservableList<Marks> array) {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        student.setCellValueFactory(new PropertyValueFactory<>("student"));
        subject.setCellValueFactory(new PropertyValueFactory<>("subject"));
        teacher.setCellValueFactory(new PropertyValueFactory<>("teacher"));
        value.setCellValueFactory(new PropertyValueFactory<>("value"));
        tableView.setItems(array);
    }

    private ArrayList<Marks> getData() {
        ArrayList<Marks> result = new ArrayList<>();

        try {
            result = DataBaseManager.getShared().getProjects();
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
