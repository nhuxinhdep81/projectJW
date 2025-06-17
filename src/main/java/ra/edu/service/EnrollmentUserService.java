package ra.edu.service;

import ra.edu.entity.Enrollment;

import java.util.List;

public interface EnrollmentUserService {

    List<Enrollment> getEnrollments(Integer studentId, int page, int pageSize);

    long getTotalEnrollments(Integer studentId);
}
