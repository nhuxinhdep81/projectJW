package ra.edu.repository;

import ra.edu.entity.Course;
import ra.edu.entity.Student;

import java.util.List;

public interface StudentRepository {

    List<Student> findAll();

    List<Student> findAllByPage(int page, int pageSize);
    long countTotalStudents();

    void lockAndUnlockStudent(int id);

    List<Student> findAll(int page, int size, String sortField, String sortDir, String keyword);
    int count(String keyword);
    Student findById(int id);


    void update(Student student);
    void changePassword(int id, String newPassword);

    boolean checkPassword(int id, String password);

    boolean existsByEmailExceptId(String email, int exceptId);
    boolean existsByPhoneExceptId(String phone, int exceptId);
}
