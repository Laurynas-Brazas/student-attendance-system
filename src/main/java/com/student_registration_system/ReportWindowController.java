package com.studed_registration_system;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class ReportWindowController {
    @FXML private VBox vbox;
    @FXML private TextField groupField;
    @FXML private TextField studentField;
    @FXML private TextField filterFromField;
    @FXML private TextField filterToField;
    @FXML
    private void initialize() {
        populateList();
    }

    @FXML
    private void populateList() {
        vbox.getChildren().clear();
        String filterForGroup = groupField.getText();
        String filterForStudent = studentField.getText();
        String filterFrom = filterFromField.getText();
        String filterTo = filterToField.getText();

        for (Group group : DataManager.getInstance().getGroups()) {
            if (!filterForGroup.isEmpty() && !filterForGroup.equals(group.getName()))
                continue;

            Label groupLabel = new Label(group.getName());
            groupLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
            vbox.getChildren().add(groupLabel);

            VBox studentsVBox = new VBox(5);

            for (Student student : group.getStudents()) {
                if (!filterForStudent.isEmpty() &&
                        !filterForStudent.equals(student.getName() + " " + student.getSurname()))
                    continue;

                HBox headerRow = new HBox(0);
                HBox studentInformation = new HBox(0);

                headerRow.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-padding: 5px;");
                studentInformation.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-padding: 5px;");

                Label studentLabel = new Label("Student");
                studentLabel.setPrefWidth(150);
                studentLabel.setStyle("-fx-border-color: black; -fx-alignment: center; -fx-padding: 5px;");

                Label studentName = new Label(student.getName() + " " + student.getSurname());
                studentName.setPrefWidth(150);
                studentName.setStyle("-fx-border-color: black; -fx-alignment: center; -fx-padding: 5px;");

                headerRow.getChildren().add(studentLabel);
                studentInformation.getChildren().add(studentName);

                Map<String, Boolean> groupAttendance = student.getAttendance().get(group.getName());

                if (groupAttendance != null) {
                    List<String> dates = new ArrayList<>(groupAttendance.keySet());
                    Collections.sort(dates);

                    for (String date : dates) {
                        boolean skip = false;
                        if (!filterFrom.isEmpty() && date.compareTo(filterFrom) < 0)
                            skip = true;
                        if (!filterTo.isEmpty() && date.compareTo(filterTo) > 0)
                            skip = true;
                        if (skip)
                            continue;

                        Label dateField = new Label(date);
                        dateField.setPrefWidth(100);
                        dateField.setStyle("-fx-border-color: black; -fx-alignment: center; -fx-padding: 5px;");
                        headerRow.getChildren().add(dateField);

                        boolean presence = groupAttendance.get(date);
                        Label presenceField = new Label(presence ? "✓" : "✗");
                        presenceField.setPrefWidth(100);
                        presenceField.setStyle("-fx-border-color: black; -fx-alignment: center; -fx-padding: 5px;");
                        studentInformation.getChildren().add(presenceField);
                    }
                }

                vbox.getChildren().addAll(headerRow, studentInformation);
            }
        }
    }

    @FXML
    private void saveAsPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF Report");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        fileChooser.setInitialFileName("Attendance_Report.pdf");

        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            try {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();

                for (Group group : DataManager.getInstance().getGroups()) {
                    Font groupFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
                    document.add(new Paragraph(group.getName(), groupFont));
                    document.add(new Paragraph(" "));

                    for (Student student : group.getStudents()) {
                        Map<String, Boolean> attendance = student.getAttendance().get(group.getName());

                        if (attendance != null && !attendance.isEmpty()) {
                            int columns = attendance.size() + 1;
                            PdfPTable table = new PdfPTable(columns);
                            table.setWidthPercentage(100);

                            PdfPCell studentHeader = new PdfPCell(new Phrase("Student"));
                            table.addCell(studentHeader);

                            List<String> sortedDates = new ArrayList<>(attendance.keySet());
                            Collections.sort(sortedDates);

                            for (String date : sortedDates) {
                                PdfPCell dateHeader = new PdfPCell(new Phrase(date));
                                table.addCell(dateHeader);
                            }

                            table.addCell(student.getName() + " " + student.getSurname());


                            for (String date : sortedDates) {
                                boolean present = attendance.get(date);
                                table.addCell(present ? "Present" : "Absent");
                            }

                            document.add(table);
                            document.add(new Paragraph(" "));
                        }
                    }
                }

                document.close();

            } catch (DocumentException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}


