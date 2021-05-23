package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.WindowEvent;
import model.DataBaseManager;
import model.Group;

import java.sql.SQLException;
import java.util.Optional;

public class AddEditGroupsController {

    @FXML
    private TextField textfield;

    @FXML
    private Button addEditButton;

    private boolean isEdit = false;
    private Group group;

    @FXML
    void addEditButtonAction() {
        if (!isEdit) {
            addData();
            textfield.setText(null);
        } else {
            editData();
        }
    }

    @FXML
    void initialize() {

    }

    public void setData(Group group) {
        this.group = group;
        textfield.setText(group.getName());
        addEditButton.setText("Изменить");
        isEdit = true;
    }

    private void addData() {
        if (!textfield.getText().isEmpty()) {
            try {
                DataBaseManager.getShared().insertToGroups(textfield.getText());
                addEditButton.getScene().getWindow().fireEvent(new WindowEvent(addEditButton.getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                showAlert("Ошибка", "Ошибка при запросе!", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Внимание!", "Поле ввода пустое!", Alert.AlertType.INFORMATION);
        }
    }

    private void editData() {
        if (!textfield.getText().isEmpty()) {
            group.setName(textfield.getText());
            try {
                if (showQuestionAlert()) {
                    DataBaseManager.getShared().updateGroups(group);
                    addEditButton.getScene().getWindow().fireEvent(new WindowEvent(addEditButton.getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                showAlert("Ошибка", "Ошибка при запросе!", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Внимание!", "Поле ввода пустое!", Alert.AlertType.INFORMATION);
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
