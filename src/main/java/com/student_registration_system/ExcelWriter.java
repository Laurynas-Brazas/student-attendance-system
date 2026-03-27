package com.studed_registration_system;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ExcelWriter implements Writer {

    @Override
    public void writeStudentData(String filePath) {
        List<Student> students = DataManager.getInstance().getStudents();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Students");

        // Create the header
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Name");
        headerRow.createCell(1).setCellValue("Surname");
        headerRow.createCell(2).setCellValue("Groups");
        headerRow.createCell(3).setCellValue("Attendance");

        int rowNum = 1;
        for (Student student : students) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(student.getName());
            row.createCell(1).setCellValue(student.getSurname());

            // Groups formatted like: "Group1,Group2"
            StringBuilder groupsBuilder = new StringBuilder();
            List<Group> groups = student.getGroups();
            for (int i = 0; i < groups.size(); i++) {
                groupsBuilder.append(groups.get(i).getName());
                if (i < groups.size() - 1) {
                    groupsBuilder.append(",");
                }
            }
            row.createCell(2).setCellValue(groupsBuilder.toString());

            // Attendance formatted like: "Group1:{2024-04-24:Present;2024-04-25:Absent};Group2:{2024-04-26:Present}"
            StringBuilder attendanceBuilder = new StringBuilder();
            Map<String, Map<String, Boolean>> attendanceMap = student.getAttendance();
            int groupCount = 0;
            for (Map.Entry<String, Map<String, Boolean>> groupEntry : attendanceMap.entrySet()) {
                String groupName = groupEntry.getKey();
                Map<String, Boolean> dates = groupEntry.getValue();

                attendanceBuilder.append(groupName).append(":{");

                int dateCount = 0;
                for (Map.Entry<String, Boolean> dateEntry : dates.entrySet()) {
                    attendanceBuilder.append(dateEntry.getKey())
                            .append(":")
                            .append(dateEntry.getValue() ? "Present" : "Absent");
                    if (dateCount < dates.size() - 1) {
                        attendanceBuilder.append(";");
                    }
                    dateCount++;
                }

                attendanceBuilder.append("}");
                if (groupCount < attendanceMap.size() - 1) {
                    attendanceBuilder.append(";");
                }
                groupCount++;
            }
            row.createCell(3).setCellValue(attendanceBuilder.toString());
        }

        // Auto size columns for neatness
        for (int i = 0; i < 4; i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
