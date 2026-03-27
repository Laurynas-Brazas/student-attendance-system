package com.studed_registration_system;

import java.util.List;

public interface Reader {
    List<Student> readStudentData(String filePath);
}
