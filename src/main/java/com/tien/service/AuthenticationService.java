package com.tien.service;

import com.tien.dto.StudentDTO;
import com.tien.entity.Student;

public interface AuthenticationService {
    void register(StudentDTO studentDTO);

    Student checkExistUserName(String userName);

    Student checkExistEmail(String email);

    Student checkExistPhone(String phone);

    StudentDTO login (String email, String password);

}
