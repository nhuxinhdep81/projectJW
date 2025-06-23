package ra.edu.repository;

import ra.edu.entity.Enrollment;

import java.util.List;

public interface EnrollmentUserRepository {
    List<Enrollment> findByStudentId(Integer studentId, int page, int pageSize);

    long countByStudentId(Integer studentId);

    List<Enrollment> searchEnrollments(Integer studentId, String keyword, Enrollment.Status status,
                                       int page, int pageSize);

    long countSearchEnrollments(Integer studentId, String keyword,
                                Enrollment.Status status);

    void cancelEnrollment(int id);

}
