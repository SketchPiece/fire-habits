package org.example.firehabits;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class FireHabitsViewController {
//    @FXML
//    private Label welcomeText;
//
//    protected void onHelloButtonClick() {
//        welcomeText.setText("Welcome to JavaFX Application!");
//    }
//
//    protected void onFireClick() {
//        System.out.println("Fire Click!");
//    }
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToAddFireView(ActionEvent event) throws IOException {
        URL addFireViewUrl = Objects.requireNonNull(FireHabitsViewController.class.getResource("add-fire-view.fxml"));
        Parent root = FXMLLoader.load(addFireViewUrl);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToActiveFireView(ActionEvent event) throws IOException {
        URL activeFireViewUrl = Objects.requireNonNull(FireHabitsViewController.class.getResource("active-fire-view.fxml"));
        Parent root = FXMLLoader.load(activeFireViewUrl);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}