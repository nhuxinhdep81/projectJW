package ra.edu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.edu.entity.Enrollment;
import ra.edu.repository.EnrollmentRepository;

import java.util.List;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Override
    public void save(Enrollment enrollment) {
        enrollmentRepository.save(enrollment);
    }

    @Override
    public boolean hasEnrolled(int studentId, int courseId) {
        return enrollmentRepository.hasEnrolled(studentId, courseId);
    }

    @Override
    public List<Integer> getRegisteredCourseIds(int studentId) {
        return enrollmentRepository.getRegisteredCourseIds(studentId);
    }

    @Override
    public long countTotalEnrollments() {
        return enrollmentRepository.countTotalEnrollments();
    }

    @Override
    public List<Object[]> countStudentByCourse() {
        return enrollmentRepository.countStudentByCourse();
    }

    @Override
    public List<Object[]> top5CoursesByEnrollment() {
        return enrollmentRepository.top5CoursesByEnrollment();
    }

    @Override
    public List<Enrollment> filterAndSearch(String status, String courseName, int page, int pageSize) {
        return enrollmentRepository.filterAndSearch(status, courseName, page, pageSize);
    }
    @Override
    public long countFilteredAndSearched(String status, String courseName) {
        return enrollmentRepository.countFilteredAndSearched(status, courseName);
    }

    @Override
    public void acceptEnrollment(int id) {
        enrollmentRepository.acceptEnrollment(id);
    }

    @Override
    public void deniedEnrollment(int id) {
        enrollmentRepository.deniedEnrollment(id);
    }

    @Override
    public long countConfirmedEnrollmentsByCourseId(int courseId) {
        return enrollmentRepository.countConfirmedEnrollmentsByCourseId(courseId);
    }

}