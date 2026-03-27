package com.studed_registration_system;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class OptionWindowController {
    @FXML private Button attendanceButton;

    @FXML
    void switchToDataWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("dataWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) attendanceButton.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Data Window");
        stage.show();
    }
    @FXML
    void switchToAttendanceWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("calendarWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) attendanceButton.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Attendance Window");
        stage.show();
    }

}
