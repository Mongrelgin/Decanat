package controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.WindowEvent;
import model.DataBaseManager;
import model.Group;
import model.People;

public class AddEditPeopleController {

    @FXML
    private Button addEditButton;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField patherTextField;

    @FXML
    private TextField positionTextField;

    @FXML
    private ComboBox<Group> departmentComboBox;

    private boolean isEdit = false;

    private People people;

    @FXML
    void initialize() {
        ObservableList<Group> groups = FXCollections.observableArrayList(getDepartments());
        departmentComboBox.setItems(groups);
    }

    @FXML
    void addEditButtonAction() {
        if (!isEdit) {
            addEmployee();
        } else {
            editEmployee();
        }
    }

    private ArrayList<Group> getDepartments() {
        ArrayList<Group> array = new ArrayList<>();
        try {
            array = DataBaseManager.getShared().getGroups();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return array;
    }

    public void setData(People people) {
        this.people = people;
        nameTextField.setText(people.getName());
        lastNameTextField.setText(people.getLastName());
        patherTextField.setText(people.getPartherName());
        positionTextField.setText(people.getType().toString());
        if (people.getGroup() != null)
            departmentComboBox.getSelectionModel().select(new Group(people.getGroup().getId(), people.getGroup().getName()));
        addEditButton.setText("Изменить");
        isEdit = true;
    }

    private void addEmployee() {
        if (!nameTextField.getText().isEmpty() && !lastNameTextField.getText().isEmpty() && !patherTextField.getText().isEmpty()) {
            Integer depId;
            String depName;
            Group group = null;
            if (departmentComboBox.getSelectionModel().getSelectedItem() != null) {
                depId = departmentComboBox.getSelectionModel().getSelectedItem().getId();
                depName = departmentComboBox.getSelectionModel().getSelectedItem().getName();
                group = new Group(depId, depName);
            }
            people = new People(nameTextField.getText(), lastNameTextField.getText(), patherTextField.getText(),
                    positionTextField.getText().charAt(0),  group);
            try {
                DataBaseManager.getShared().insertPeople(people);
                addEditButton.getScene().getWindow().fireEvent(new WindowEvent(addEditButton.getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                showAlert("Ошибка запроса", "Ошибка во время выполнения запроса!", Alert.AlertType.ERROR);
            }
            nameTextField.clear();
            lastNameTextField.clear();
            patherTextField.clear();
            positionTextField.clear();
        } else {
            showAlert("Некоректные данные", "Поле Имя, Фамилия или Отчество не заполнено.", Alert.AlertType.WARNING);
        }
    }

    private void editEmployee() {
        if (!nameTextField.getText().isEmpty() && !lastNameTextField.getText().isEmpty() && !patherTextField.getText().isEmpty()) {
            Integer depId;
            Integer idEmp = people.getId();
            System.out.println(idEmp);
            String depName;
            Group group = null;
            if (departmentComboBox.getSelectionModel().getSelectedItem() != null) {
                depId = departmentComboBox.getSelectionModel().getSelectedItem().getId();
                depName = departmentComboBox.getSelectionModel().getSelectedItem().getName();
                group = new Group(depId, depName);
            }
            people = new People(idEmp, nameTextField.getText(), lastNameTextField.getText(), patherTextField.getText(),
                    positionTextField.getText().charAt(0), group);
            try {
                if (showQuestionAlert()) {
                    DataBaseManager.getShared().updatePeople(people);
                    addEditButton.getScene().getWindow().fireEvent(new WindowEvent(addEditButton.getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                showAlert("Ошибка запроса", "Ошибка во время выполнения запроса!", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Некоректные данные", "Поле Имя, Фамилия или Отчество не заполнено.", Alert.AlertType.WARNING);
        }
    }

    private void showAlert(String title ,String message,  Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    private boolean showQuestionAlert() {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Внести изменения?");
        alert.setHeaderText("Вы уверены что хотите внести изменения в запись?");
        ButtonType yes = new ButtonType("Да");
        alert.getButtonTypes().addAll(yes);
        alert.getButtonTypes().addAll(new ButtonType("Нет"));
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == yes;
    }

}
