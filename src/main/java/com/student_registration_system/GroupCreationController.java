package com.studed_registration_system;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class GroupCreationController {
    @FXML private ListView<CheckBox> studentList;
    @FXML private TextField groupNameField;
    private TableView<Student> studentTable;
    private TableView<Group> groupTable;
    private Group originalGroup;
    @FXML
    public void initialize() {
        originalGroup = null;
        for (Student student : DataManager.getInstance().getStudents()) {
            CheckBox cb = new CheckBox(student.getName() + " " + student.getSurname());
            studentList.getItems().add(cb);
        }
    }

    @FXML
    private void saveChanges() {
        Group group = new Group(groupNameField.getText(), getSelectedStudents());
        if (originalGroup != null) {
            DataManager.getInstance().removeGroup(originalGroup);
        }
        DataManager.getInstance().addGroup(group);

        Stage stage = (Stage) studentList.getScene().getWindow();
        groupTable.getItems().clear();
        groupTable.getItems().addAll(DataManager.getInstance().getGroups());
        studentTable.getItems().clear();
        studentTable.getItems().addAll(DataManager.getInstance().getStudents());
        stage.close();
    }

    public List<Student> getSelectedStudents() {
        List<Student> selected = new ArrayList<>();
        for (int i = 0; i < DataManager.getInstance().getStudents().size(); i++) {
            if (studentList.getItems().get(i).isSelected()) {
                selected.add(DataManager.getInstance().getStudents().get(i));
            }
        }
        return selected;
    }

    public void passTables(TableView<Student> studentTable,TableView<Group> groupTable) {
        this.studentTable = studentTable;
        this.groupTable = groupTable;
    }

    public void setGroupToEdit(Group group) {
        originalGroup = group;
        groupNameField.setText(group.getName());
        for (CheckBox cb : studentList.getItems()) {
            for (Student student : group.getStudents()) {
                if (cb.getText().equals(student.getName() + " " + student.getSurname()))
                    cb.setSelected(true);
            }
        }
    }

}
