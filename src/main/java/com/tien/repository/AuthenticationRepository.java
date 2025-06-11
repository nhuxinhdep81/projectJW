package com.tien.repository;

import com.tien.dto.StudentDTO;
import com.tien.entity.Student;

public interface AuthenticationRepository {
    void register(Student  student);

    Student checkExistUserName(String userName);

    Student checkExistEmail(String email);

    Student checkExistPhone(String phone);

    Student login (String email, String password);
}
