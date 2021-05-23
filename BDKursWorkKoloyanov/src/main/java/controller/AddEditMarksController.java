package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.WindowEvent;
import model.*;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class AddEditMarksController {

    @FXML
    private TextField valueTextField;
    @FXML
    private ComboBox<People> teacherComboBox;
    @FXML
    private ComboBox<Subject> subjectComboBox;
    @FXML
    private ComboBox<People> studentComboBox;
    @FXML
    private Button addEditButton;

    @FXML
    private Label realEndDateLabel;

    private boolean isEdit = false;

    private Marks marks;

    @FXML
    void initialize() {
        /*
        ObservableList<Group> groups = FXCollections.observableArrayList(getDepartments());
        departmentComboBox.setItems(groups);
        priceTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                priceTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        priceTextField.setText("0");
        beginDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(LocalDate.now().plusDays(1));
        realEndDatePicker.setValue(LocalDate.now().plusDays(1));

         */
    }

    @FXML
    void addEditButtonAction() {
        if (!isEdit) {
            addProject();
        } else {
            editProject();
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

    public void setData(Marks marks) {
        /*
        this.marks = marks;
        nameTextField.setText(marks.getName());
        priceTextField.setText(marks.getPrice().toString());
        if (marks.getDepartment() != null)
            departmentComboBox.getSelectionModel().select(marks.getDepartment());
        if (marks.getDateBeg() != null)
            beginDatePicker.setValue(marks.getDateBeg().toLocalDate());
        if (marks.getDateEnd() != null)
            endDatePicker.setValue(marks.getDateEnd().toLocalDate());
        if (marks.getDateEndReal() != null)
            realEndDatePicker.setValue(marks.getDateEndReal().toLocalDate());
        addEditButton.setText("Изменить");
        realEndDatePicker.setVisible(true);
        realEndDateLabel.setVisible(true);
        isEdit = true;

         */
    }

    private void addProject() {
        /*
        if (!nameTextField.getText().isEmpty()) {
            Group group = null;
            if (departmentComboBox.getSelectionModel().getSelectedItem() != null) {
                group = new Group(departmentComboBox.getSelectionModel().getSelectedItem().getId(), departmentComboBox.getSelectionModel().getSelectedItem().getName());
            }
            int cost;
            try {
                cost = Integer.parseInt(priceTextField.getText());
            } catch (NumberFormatException exception) {
                cost = 0;
            }
            Date begDate = null;
            Date endDate = null;
            if (!beginDatePicker.getEditor().getText().isEmpty())
                begDate = Date.valueOf(beginDatePicker.getValue());
            if (!endDatePicker.getEditor().getText().isEmpty())
                endDate = Date.valueOf(endDatePicker.getValue());
            marks = new Marks(nameTextField.getText(), cost, group, begDate, endDate);
            try {
                DataBaseManager.getShared().insertProject(marks);
                addEditButton.getScene().getWindow().fireEvent(new WindowEvent(addEditButton.getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                showAlert("Ошибка", "Ошибка при попытке сделать запрос!", Alert.AlertType.ERROR);
            }
        } else  {
            showAlert("Поля пустые", "Поле название пустое!", Alert.AlertType.WARNING);
        }

         */
    }

    private void editProject() {
        /*
        if (!nameTextField.getText().isEmpty()) {
            Group group = null;
            Integer projectId = marks.getId();
            if (departmentComboBox.getSelectionModel().getSelectedItem() != null) {
                group = new Group(departmentComboBox.getSelectionModel().getSelectedItem().getId(), departmentComboBox.getSelectionModel().getSelectedItem().getName());
            }
            int cost;
            try {
                cost = Integer.parseInt(priceTextField.getText());
            } catch (NumberFormatException exception) {
                cost = 0;
            }
            Date begDate = null;
            Date endDate = null;
            Date realEndDate = null;
            if (!beginDatePicker.getEditor().getText().isEmpty())
                begDate = Date.valueOf(beginDatePicker.getValue());
            if (!endDatePicker.getEditor().getText().isEmpty())
                endDate = Date.valueOf(endDatePicker.getValue());
            if (!realEndDatePicker.getEditor().getText().isEmpty())
                realEndDate = Date.valueOf(realEndDatePicker.getValue());
            marks = new Marks(projectId, nameTextField.getText(), cost, group, begDate, endDate, realEndDate);
            try {
                if (showQuestionAlert()) {
                    DataBaseManager.getShared().updateProject(marks);
                    addEditButton.getScene().getWindow().fireEvent(new WindowEvent(addEditButton.getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                showAlert("Ошибка", "Ошибка при попытке сделать запрос!", Alert.AlertType.ERROR);
            }
        } else  {
            showAlert("Поля пустые", "Поле название пустое!", Alert.AlertType.WARNING);
        }

         */
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
