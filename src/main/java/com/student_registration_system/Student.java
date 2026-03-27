package com.studed_registration_system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student {
    private String name, surname;
    private List<Group> groups;
    Map<String, Map<String, Boolean>> attendance;
    public Student(String name, String surname, List<Group> groups, Map<String, Map<String, Boolean>> attendance) {
        this.name = name;
        this.surname = surname;
        this.groups = groups;
        this.attendance = attendance;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void addGroup(Group group) {
        groups.add(group);
    }

    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }
    public Map<String, Map<String, Boolean>> getAttendance() {
        return attendance;
    }

    public void setAttendance(String groupName, String date, Boolean isPresent) {
        this.attendance.putIfAbsent(groupName, new HashMap<>());
        this.attendance.get(groupName).put(date, isPresent);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Student other = (Student) obj;
        return name.equals(other.name) && surname.equals(other.surname);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(name, surname);
    }

}
