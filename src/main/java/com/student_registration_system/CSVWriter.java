package com.studed_registration_system;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CSVWriter implements Writer {

    @Override
    public void writeStudentData(String filePath) {
        List<Student> students = DataManager.getInstance().getStudents();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Name,Surname,Groups,Attendance");
            writer.newLine();

            for (Student student : students) {
                StringBuilder sb = new StringBuilder();

                sb.append(student.getName()).append(",").append(student.getSurname()).append(",");

                sb.append("\"");
                List<Group> groups = student.getGroups();
                for (int i = 0; i < groups.size(); i++) {
                    sb.append(groups.get(i).getName());
                    if (i < groups.size() - 1) {
                        sb.append(",");
                    }
                }
                sb.append("\",");

                sb.append("\"");
                Map<String, Map<String, Boolean>> attendanceMap = student.getAttendance();
                int groupCount = 0;
                for (Map.Entry<String, Map<String, Boolean>> groupEntry : attendanceMap.entrySet()) {
                    String groupName = groupEntry.getKey();
                    Map<String, Boolean> dates = groupEntry.getValue();

                    sb.append(groupName).append(":{");

                    int dateCount = 0;
                    for (Map.Entry<String, Boolean> dateEntry : dates.entrySet()) {
                        sb.append(dateEntry.getKey())
                                .append(":")
                                .append(dateEntry.getValue() ? "Present" : "Absent");
                        if (dateCount < dates.size() - 1) {
                            sb.append(";");
                        }
                        dateCount++;
                    }

                    sb.append("}");
                    if (groupCount < attendanceMap.size() - 1) {
                        sb.append(";");
                    }
                    groupCount++;
                }
                sb.append("\"");

                writer.write(sb.toString());
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
