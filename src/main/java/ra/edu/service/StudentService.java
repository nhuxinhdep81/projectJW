package ra.edu.service;

import ra.edu.entity.Student;

import java.util.List;

public interface StudentService {
    List<Student> findAll();

    List<Student> findAllByPage(int page, int pageSize);
    long countTotalStudents();
}
