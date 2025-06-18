package ra.edu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.edu.entity.Student;
import ra.edu.repository.StudentRepository;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public List<Student> findAllByPage(int page, int pageSize) {
        return studentRepository.findAllByPage(page, pageSize);
    }

    @Override
    public long countTotalStudents() {
        return studentRepository.countTotalStudents();
    }

    @Override
    public Student findById(int id) {
        return studentRepository.findById(id);
    }

    @Override
    public void lockAndUnlockStudent(int id) {
        studentRepository.lockAndUnlockStudent(id);
    }

    @Override
    public List<Student> findStudents(int page, int size, String sortField, String sortDir, String keyword) {
        return studentRepository.findAll(page, size, sortField, sortDir, keyword);
    }

    @Override
    public int countStudents(String keyword) {
        return studentRepository.count(keyword);
    }


}
