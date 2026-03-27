package com.studed_registration_system;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentCreationController {
    @FXML
    private ListView<CheckBox> groupList;
    @FXML private TextField studentNameField, studentSurnameField;
    private TableView<Student> studentTable;
    private TableView<Group> groupTable;
    private Student originalStudent;
    @FXML
    public void initialize() {
        originalStudent = null;
        for (Group group : DataManager.getInstance().getGroups()) {
            CheckBox cb = new CheckBox(group.getName());
            groupList.getItems().add(cb);
        }
    }

    @FXML
    private void saveChanges() {
        Map<String, Map<String, Boolean>> attendance = new HashMap<>();
        if (originalStudent != null) {
            attendance = originalStudent.getAttendance();
            DataManager.getInstance().removeStudent(originalStudent);
        }
        Student student = new Student(studentNameField.getText(), studentSurnameField.getText(), getSelectedGroups(), attendance);
        DataManager.getInstance().addStudent(student);

        Stage stage = (Stage) groupList.getScene().getWindow();
        groupTable.getItems().clear();
        groupTable.getItems().addAll(DataManager.getInstance().getGroups());
        studentTable.getItems().clear();
        studentTable.getItems().addAll(DataManager.getInstance().getStudents());
        stage.close();
    }

    public List<Group> getSelectedGroups() {
        List<Group> selected = new ArrayList<>();
        for (int i = 0; i < DataManager.getInstance().getGroups().size(); i++) {
            if (groupList.getItems().get(i).isSelected()) {
                selected.add(DataManager.getInstance().getGroups().get(i));
            }
        }
        return selected;
    }

    public void passTables(TableView<Student> studentTable, TableView<Group> groupTable) {
        this.studentTable = studentTable;
        this.groupTable = groupTable;
    }

    public void setStudentToEdit(Student student) {
        originalStudent = student;
        studentNameField.setText(student.getName());
        studentSurnameField.setText(student.getSurname());
        for (CheckBox cb : groupList.getItems()) {
            for (Group group : student.getGroups()) {
                if (cb.getText().equals(group.getName()))
                    cb.setSelected(true);
            }
        }
    }

}
