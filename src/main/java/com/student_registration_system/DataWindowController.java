package com.studed_registration_system;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

public class DataWindowController extends DataDisplay{
    @FXML private TableView<Group> groupTable;
    @FXML private TableColumn<Group, String> groupNameColumn;
    @FXML private TableColumn<Group, Integer> studentCountColumn;
    @FXML private TableColumn<Group, Void> editGroupColumn;
    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, String> studentNameColumn;
    @FXML private TableColumn<Student, Void> editStudentColumn;
    @FXML private Button importButton;

    @Override
    @FXML
    void initialize() {
        importButton.setOnAction(event -> {
            importData();
            groupTable.getItems().clear();
            studentTable.getItems().clear();
            groupTable.getItems().setAll(DataManager.getInstance().getGroups());
            studentTable.getItems().setAll(DataManager.getInstance().getStudents());
        });
        groupNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        studentCountColumn.setCellValueFactory(new PropertyValueFactory<>("studentCount"));
        studentNameColumn.setCellValueFactory(data -> {
            Student student = data.getValue();
            return new javafx.beans.property.SimpleStringProperty(student.getName() + " " + student.getSurname());
        });

        addEditButtonToTable();
        addEditButtonToStudentTable();

        groupTable.getItems().addAll(DataManager.getInstance().getGroups());
        studentTable.getItems().addAll(DataManager.getInstance().getStudents());
    }

    private void addEditButtonToStudentTable() {
        Callback<TableColumn<Student, Void>, TableCell<Student, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Student, Void> call(final TableColumn<Student, Void> param) {
                return new TableCell<>() {
                    private final Button button = new Button("Edit");
                    {
                        button.setOnAction(event -> {
                            Student student = getTableView().getItems().get(getIndex());
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("studentCreationWindow.fxml"));
                                Scene scene = new Scene(loader.load());

                                StudentCreationController controller = loader.getController();
                                controller.passTables(studentTable, groupTable);
                                controller.setStudentToEdit(student);

                                Stage stage = new Stage();
                                stage.setScene(scene);
                                stage.setTitle("Edit Student: " + student.getName() + " " + student.getSurname());
                                stage.show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(button);
                        }
                    }
                };
            }
        };

        editStudentColumn.setCellFactory(cellFactory);
    }

    private void addEditButtonToTable() {
        Callback<TableColumn<Group, Void>, TableCell<Group, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Group, Void> call(final TableColumn<Group, Void> param) {
                return new TableCell<>() {
                    private final Button button = new Button("Edit");
                    {
                        button.setOnAction(event -> {
                            Group group = getTableView().getItems().get(getIndex());
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("groupCreationWindow.fxml"));
                                Scene scene = new Scene(loader.load());

                                GroupCreationController controller = loader.getController();
                                controller.passTables(studentTable, groupTable);
                                controller.setGroupToEdit(group);

                                Stage stage = new Stage();
                                stage.setScene(scene);
                                stage.setTitle("Edit Group: " + group.getName());
                                stage.show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(button);
                        }
                    }
                };
            }
        };

        editGroupColumn.setCellFactory(cellFactory);
    }

    @FXML
    void goToGroupCreation() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("groupCreationWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        GroupCreationController controller = fxmlLoader.getController();
        controller.passTables(studentTable, groupTable);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void goToStudentCreation() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("studentCreationWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        StudentCreationController controller = fxmlLoader.getController();
        controller.passTables(studentTable, groupTable);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    Stage getCurrentStage() {
        return (Stage)groupTable.getScene().getWindow();
    }

}
