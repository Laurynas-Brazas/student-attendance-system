package com.studed_registration_system;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class DataDisplay {
    abstract Stage getCurrentStage();
    abstract void initialize();

    @FXML
    void importData() {
        File file = chooseFile(getCurrentStage());
        if (file != null) {
            Reader reader = null;
            String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);
            if (extension.equals("csv"))
                reader = new CSVReader();
            if (extension.equals("xlsx"))
                reader = new ExcelReader();

            if (reader != null) {
                DataManager.getInstance().clearAll();
                List<Student> students = reader.readStudentData(file.getAbsolutePath());
                for (Student student : students)
                    DataManager.getInstance().addStudent(student);
                initializeGroups(students);
            }
        }
    }

    @FXML
    void exportData() {
        File file = chooseFileToSave(getCurrentStage());

        if (file != null) {
            Writer writer = null;
            String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);
            if (extension.equals("csv"))
                writer = new CSVWriter();
            if (extension.equals("xlsx"))
                writer = new ExcelWriter();
            if (writer != null)
                writer.writeStudentData(file.getAbsolutePath());
        }

    }
    private File chooseFile(Stage parentStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Student Data File");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx")
        );

        return fileChooser.showOpenDialog(parentStage);
    }

    private File chooseFileToSave(Stage parentStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Student Data File");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx")
        );

        return fileChooser.showSaveDialog(parentStage);
    }

    private void initializeGroups(List<Student> students) {
        for (Student student : students) {
            for (Group studentGroup : student.getGroups()) {
                Group group = DataManager.getInstance().getGroup(studentGroup.getName());
                if (group == null) {
                    group = new Group(studentGroup.getName());
                    DataManager.getInstance().addGroup(group);
                }
                group.addStudent(student);
            }
        }
    }
    @FXML
    void goToOptions() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("optionWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = getCurrentStage();
        stage.setScene(scene);
        stage.setTitle("Options");
        stage.show();
    }
}
