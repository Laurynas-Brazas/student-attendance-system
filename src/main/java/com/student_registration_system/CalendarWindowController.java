package com.studed_registration_system;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;

public class CalendarWindowController extends DataDisplay {
    @FXML private GridPane calendarGrid;
    @FXML private Label monthYearLabel;
    @FXML private Button previousMonthButton;
    @FXML private Button nextMonthButton;
    @FXML private CheckBox filledMonthsCheckBox;
    private YearMonth currentYearMonth;

    @Override
    Stage getCurrentStage() {
        return (Stage) calendarGrid.getScene().getWindow();
    }
    @Override
    @FXML
    public void initialize() {
        currentYearMonth = YearMonth.now();
        populateCalendar(currentYearMonth, filledMonthsCheckBox.isSelected());
        previousMonthButton.setOnAction(e -> {
            currentYearMonth = currentYearMonth.minusMonths(1);
            populateCalendar(currentYearMonth, filledMonthsCheckBox.isSelected());
        });

        nextMonthButton.setOnAction(e -> {
            currentYearMonth = currentYearMonth.plusMonths(1);
            populateCalendar(currentYearMonth, filledMonthsCheckBox.isSelected());
        });
    }

    private void populateCalendar(YearMonth yearMonth, boolean showOnlyFilled) {
        calendarGrid.getChildren().clear();
        monthYearLabel.setText(yearMonth.getMonth().toString() + " " + yearMonth.getYear());

        LocalDate firstOfMonth = yearMonth.atDay(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue() - 1;
        int daysInMonth = yearMonth.lengthOfMonth();
        int row = 0;
        int col =  dayOfWeek;

        for (int day = 1; day <= daysInMonth; day++) {
            Button dayButton = new Button(String.valueOf(day));
            dayButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            final int clickedDay = day;
            LocalDate currentDate = LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), day);
            boolean isEmpty = dateIsEmpty(currentDate.toString());
            if (isEmpty && showOnlyFilled) {
                dayButton.setStyle("-fx-opacity: 0.4; -fx-background-color: #dddddd;");
                dayButton.setDisable(true);
            }
            else {
                dayButton.setStyle("");
                dayButton.setDisable(false);
            }
            if (!isEmpty || !showOnlyFilled) {
                dayButton.setOnAction(e -> {
                    LocalDate selectedDate = LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), clickedDay);
                    try {
                        openAttendanceWindow(selectedDate);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });

            }
            calendarGrid.add(dayButton, col, row);
            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }

    }

    private void openAttendanceWindow(LocalDate date) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("attendanceFillWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        AttendanceFillController controller = fxmlLoader.getController();
        controller.passDate(date);
        Stage stage = (Stage) calendarGrid.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Fill Attendance");
        stage.show();
    }

    private boolean dateIsEmpty(String date) {
        for (Student student : DataManager.getInstance().getStudents()) {
            Map<String, Map<String, Boolean>> attendance = student.getAttendance();
            for (Map<String, Boolean> groupAttendance : attendance.values()) {
                if (groupAttendance.containsKey(date)) {
                    return false;
                }
            }
        }
        return true;
    }


    @FXML
    void changeViewMode() {
        populateCalendar(currentYearMonth, filledMonthsCheckBox.isSelected());
    }


    @FXML
    void showReport() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("reportWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Report");
        stage.show();
    }


}
