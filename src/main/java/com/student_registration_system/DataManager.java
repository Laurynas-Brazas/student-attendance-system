package com.studed_registration_system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {
    private static DataManager instance;
    Map<String, Group> groups = new HashMap<String, Group>();
    Map<String, Student> students = new HashMap<String, Student>();

    private DataManager() {}

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public void addStudent(Student student) {
        students.put(student.getName() + " " + student.getSurname(), student);
        for (Group group : student.getGroups()) {
            if(!group.getStudents().contains(student))
                group.addStudent(student);
        }
    }

    public void removeStudent(Student student) {
        for (Group group : new ArrayList<>(student.getGroups())) {
            removeStudentFromGroup(student, group);
        }
        students.remove(student.getName() + " " + student.getSurname());
    }

    public void removeStudentFromGroup(Student student, Group group) {
        group.removeStudent(student);
        student.getGroups().remove(group);
    }

    public void addGroup(Group group) {
        groups.put(group.getName(), group);
        for (Student student : group.getStudents()) {
            if (!student.getGroups().contains(group)) {
                student.addGroup(group);
            }
        }
    }

    public void removeGroup(Group group) {
        for (Student student : new ArrayList<>(group.getStudents())) {
            removeStudentFromGroup(student, group);
        }
        groups.remove(group.getName());
    }

    public List<Group> getGroups() {
        return new ArrayList<Group>(groups.values());
    }

    public Group getGroup(String name) {
        return groups.get(name);
    }

    public List<Student> getStudents() {
        return new ArrayList<Student>(students.values());
    }

    public Student getStudent(String name) {
        return students.get(name);
    }

    public void clearAll() {
        groups.clear();
        students.clear();
    }
}
