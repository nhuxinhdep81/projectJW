package ra.edu.repository;

import ra.edu.entity.Enrollment;

import java.util.List;

public interface EnrollmentRepository {
    void save(Enrollment enrollment);
    boolean hasEnrolled(int studentId, int courseId);
    List<Integer> getRegisteredCourseIds(int studentId);

    long countTotalEnrollments();
    List<Object[]> countStudentByCourse();
    List<Object[]> top5CoursesByEnrollment();

    List<Enrollment> showListEnrollments(int page, int pageSize);
    long countAllEnrollements();


    List<Enrollment> filterAndSearch(String status, String courseName, int page, int pageSize);
    long countFilteredAndSearched(String status, String courseName);

    void acceptEnrollment(int id);

    void deniedEnrollment(int id);

    long countConfirmedEnrollmentsByCourseId(int courseId);

}