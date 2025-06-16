package ra.edu.repository;

import ra.edu.entity.Course;
import ra.edu.entity.Student;

import java.util.List;

public interface StudentRepository {

    List<Student> findAll();

    List<Student> findAllByPage(int page, int pageSize);
    long countTotalStudents();
}
