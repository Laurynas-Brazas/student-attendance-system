package com.studed_registration_system;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ExcelReader implements Reader {

    @Override
    public List<Student> readStudentData(String filePath) {
        List<Student> students = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            boolean skipHeader = true;

            for (Row row : sheet) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                Cell nameCell = row.getCell(0);
                Cell surnameCell = row.getCell(1);
                Cell groupsCell = row.getCell(2);
                Cell attendanceCell = row.getCell(3);

                if (nameCell == null || surnameCell == null || groupsCell == null || attendanceCell == null) {
                    continue;
                }

                String name = nameCell.getStringCellValue().trim();
                String surname = surnameCell.getStringCellValue().trim();
                List<String> groupNames = new ArrayList<>(Arrays.asList(groupsCell.getStringCellValue().split(",")));
                List<Group> groups = new ArrayList<>();

                for (String groupName : groupNames) {
                    Group group = DataManager.getInstance().getGroup(groupName.trim());
                    if (group == null) {
                        group = new Group(groupName.trim());
                    }
                    groups.add(group);
                }

                Map<String, Map<String, Boolean>> attendance = parseAttendance(attendanceCell.getStringCellValue());

                students.add(new Student(name, surname, groups, attendance));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return students;
    }

    private Map<String, Map<String, Boolean>> parseAttendance(String line) {
        Map<String, Map<String, Boolean>> attendance = new HashMap<>();

        if (line == null || line.isEmpty()) {
            return attendance;
        }

        int i = 0;
        while (i < line.length()) {
            int groupStart = i;
            int braceOpen = line.indexOf('{', groupStart);
            int braceClose = line.indexOf('}', braceOpen);

            if (braceOpen == -1 || braceClose == -1) {
                break;
            }

            String groupName = line.substring(groupStart, braceOpen).trim();
            String insideBraces = line.substring(braceOpen + 1, braceClose);

            Map<String, Boolean> dateAttendance = new HashMap<>();

            String[] dateRecords = insideBraces.split(";");
            for (String dateRecord : dateRecords) {
                String[] parts = dateRecord.split(":");
                if (parts.length != 2)
                    continue;
                String date = parts[0].trim();
                String status = parts[1].trim();
                dateAttendance.put(date, status.equalsIgnoreCase("Present"));
            }

            attendance.put(groupName, dateAttendance);

            i = braceClose + 2;
        }
        return attendance;
    }

}
