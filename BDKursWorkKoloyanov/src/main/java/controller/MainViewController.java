package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import model.DataBaseManager;

import java.io.IOException;

public class MainViewController {
    @FXML
    private Button subjectsTabButton;
    @FXML
    private Label userNameLabel;

    @FXML
    private Button peopleTabButton;

    @FXML
    private Button groupsTabButton;

    @FXML
    private Button marksTabButton;

    @FXML
    private AnchorPane tabs;

    @FXML
    private Label tabLabel;

    private boolean isLogOut = false;

    @FXML
    void pressOnTabButton(ActionEvent event) {
        System.out.println(((Button) event.getSource()).getId());
        System.out.println(peopleTabButton.toString());

        switch (((Button) event.getSource()).getId()) {
            case "peopleTabButton":
                openPeopleTab();
                break;
            case "groupsTabButton":
                openGroupsTab();
                break;
            case "marksTabButton":
                openMarksTab();
                break;
            case "subjectsTabButton":
                openSubjectsTab();
                break;
            default:
                break;

        }
    }

    @FXML
    void logOutButton() {
        isLogOut = true;
        marksTabButton.getScene().getWindow().hide();
    }

    @FXML
    void initialize() {
        tabLabel.setText("Люди");
        loadPage("peopleTab");
        userNameLabel.setText(DataBaseManager.getShared().getUser().getLogin());
    }

    private void openPeopleTab() {
        peopleTabButton.setStyle("-fx-background-color: white; -fx-text-fill: black");
        groupsTabButton.setStyle("-fx-background-color:  #d203fc; -fx-text-fill: white");
        marksTabButton.setStyle("-fx-background-color:  #d203fc; -fx-text-fill: white");
        subjectsTabButton.setStyle("-fx-background-color:  #d203fc; -fx-text-fill: white");
        tabLabel.setText("Люди");
        loadPage("peopleTab");
    }

    private void openGroupsTab() {
        peopleTabButton.setStyle("-fx-background-color:  #d203fc; -fx-text-fill: white");
        groupsTabButton.setStyle("-fx-background-color:  white; -fx-text-fill: black");
        marksTabButton.setStyle("-fx-background-color:  #d203fc; -fx-text-fill: white");
        subjectsTabButton.setStyle("-fx-background-color:  #d203fc; -fx-text-fill: white");
        tabLabel.setText("Группы");
        loadPage("groupsTab");
    }

    private void openMarksTab() {
        peopleTabButton.setStyle("-fx-background-color:  #d203fc; -fx-text-fill: white");
        groupsTabButton.setStyle("-fx-background-color:  #d203fc; -fx-text-fill: white");
        marksTabButton.setStyle("-fx-background-color:  white; -fx-text-fill: black");
        subjectsTabButton.setStyle("-fx-background-color:  #d203fc; -fx-text-fill: white");
        tabLabel.setText("Оценки");
        loadPage("marksTab");
    }

    private void openSubjectsTab() {
        peopleTabButton.setStyle("-fx-background-color:  #d203fc; -fx-text-fill: white");
        groupsTabButton.setStyle("-fx-background-color:  #d203fc; -fx-text-fill: white");
        marksTabButton.setStyle("-fx-background-color:  #d203fc; -fx-text-fill: white");
        subjectsTabButton.setStyle("-fx-background-color:  white; -fx-text-fill: black");
        tabLabel.setText("Предметы");
        loadPage("subjectsTab");
    }

    private void loadPage(String name) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../view/"+name+".fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Region n = (Region) root;
        tabs.getChildren().add(n);
        n.prefHeightProperty().bind(tabs.heightProperty());
        n.prefWidthProperty().bind(tabs.widthProperty());
    }

    public boolean isLogOut() { return isLogOut; }

}
