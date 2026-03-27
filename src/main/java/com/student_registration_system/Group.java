package com.studed_registration_system;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private SimpleStringProperty name;
    private SimpleIntegerProperty studentCount;
    private List<Student> students;

    public Group(String groupName, List<Student> students) {
        this.name = new SimpleStringProperty(groupName);
        this.students = students;
        this.studentCount = new SimpleIntegerProperty(students.size());
    }

    public Group(String groupName) {
        this.name = new SimpleStringProperty(groupName);
        this.students = new ArrayList<>();
        this.studentCount = new SimpleIntegerProperty(0);
    }

    public String getName() {
        return name.get();
    }

    public int getStudentCount() {
        return students.size();
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void addStudent(Student student) {
        students.add(student);
        studentCount.set(students.size());
    }

    public void removeStudent(Student student) {
        students.remove(student);
        studentCount.set(students.size());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Group other = (Group) obj;
        return this.getName().equals(other.getName());
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(getName());
    }

}
