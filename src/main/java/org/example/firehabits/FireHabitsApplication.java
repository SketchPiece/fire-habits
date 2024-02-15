package org.example.firehabits;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FireHabitsApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FireHabitsApplication.class.getResource("active-fire-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Fire Habits");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}