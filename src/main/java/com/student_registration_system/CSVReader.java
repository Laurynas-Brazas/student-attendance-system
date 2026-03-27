package com.studed_registration_system;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CSVReader implements Reader {

    @Override
    public List<Student> readStudentData(String filePath) {
        List <Student> students = new ArrayList<Student>();
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean skipHeader = true;
            while ((line = br.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }
                line = line.replace(" ", "");
                List<String> data = parseCSVLine(line);
                if (data.size() != 4)
                    continue;
                String name = data.get(0);
                String surname = data.get(1);
                List<String> groupNames = new ArrayList<>(Arrays.asList(data.get(2).split(",")));
                List<Group> groups = new ArrayList<>(Arrays.asList());
                for (String groupName : groupNames) {
                    Group group = DataManager.getInstance().getGroup(groupName);
                    if (group == null)  {
                        group = new Group(groupName);
                    }
                    groups.add(group);
                }
                Map<String, Map<String, Boolean>> attendance = parseAttendance(data.get(3));
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

            String groupName = line.substring(groupStart, braceOpen - 1).trim();
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


    private List<String> parseCSVLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        boolean insideQuotes = false;
        for (char c : line.toCharArray()) {
            if (c == '"') {

                insideQuotes = !insideQuotes;
            }
            else if (insideQuotes) {
                builder.append(c);
            }
            else {
                if (c == ',') {
                    tokens.add(builder.toString());
                    builder.setLength(0);
                }
                else {
                    builder.append(c);
                }
            }
        }
        tokens.add(builder.toString());
        return tokens;
    }
}
