package ra.edu.service;

import ra.edu.entity.Student;

import java.util.List;

public interface StudentService {
    List<Student> findAll();

    List<Student> findAllByPage(int page, int pageSize);
    long countTotalStudents();

    Student findById(int id);

    void lockAndUnlockStudent(int id);


    List<Student> findStudents(int page, int size, String sortField, String sortDir, String keyword);
    int countStudents(String keyword);
}
