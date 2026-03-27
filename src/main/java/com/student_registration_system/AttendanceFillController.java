package com.studed_registration_system;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

public class AttendanceFillController {
    @FXML private VBox VBox;
    @FXML private Label dateLabel;
    LocalDate date;

    public void passDate(LocalDate date) {
        this.date = date;
        buildUI();
    }

    private void buildUI() {
        String day = date.toString();
        dateLabel.setText(day);
        for (Group group : DataManager.getInstance().getGroups()) {
            javafx.scene.control.Label groupLabel = new javafx.scene.control.Label(group.getName());
            groupLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
            VBox.getChildren().add(groupLabel);

            VBox studentsVBox = new VBox(5);
            for (Student student : group.getStudents()) {
                javafx.scene.layout.HBox studentRow = new javafx.scene.layout.HBox(10);
                javafx.scene.control.Label studentName = new javafx.scene.control.Label(student.getName() + " " + student.getSurname());
                studentName.setPrefWidth(150);
                javafx.scene.control.Button presentButton = new javafx.scene.control.Button("Present");
                javafx.scene.control.Button absentButton = new javafx.scene.control.Button("Absent");

                if (student.getAttendance().containsKey(group.getName())) {
                    Map<String, Boolean> groupAttendance = student.getAttendance().get(group.getName());
                    if (groupAttendance.containsKey(day)) {
                        if (groupAttendance.get(day)) {
                            presentButton.setStyle("-fx-background-color: lightgreen;");
                        } else {
                            absentButton.setStyle("-fx-background-color: lightcoral;");
                        }
                    }
                }

                presentButton.setOnAction(e -> {
                    student.setAttendance(group.getName(), day, true);
                    presentButton.setStyle("-fx-background-color: lightgreen;");
                    absentButton.setStyle("-fx-background-color: lightgray;");
                });

                absentButton.setOnAction(e -> {
                    student.setAttendance(group.getName(), day, false);
                    absentButton.setStyle("-fx-background-color: lightcoral;");
                    presentButton.setStyle("-fx-background-color: lightgray;");
                });

                studentRow.getChildren().addAll(studentName, presentButton, absentButton);
                studentsVBox.getChildren().add(studentRow);
            }
            VBox.getChildren().add(studentsVBox);
        }
    }
    @FXML
    private void saveChanges() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("calendarWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) dateLabel.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Attendance Window");
        stage.show();
    }

}
