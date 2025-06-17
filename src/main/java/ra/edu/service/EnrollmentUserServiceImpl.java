package ra.edu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.edu.entity.Enrollment;
import ra.edu.repository.EnrollmentUserRepository;

import java.util.List;

@Service
public class EnrollmentUserServiceImpl implements EnrollmentUserService {

    @Autowired
    private EnrollmentUserRepository enrollmentUserRepository;

    @Override
    public List<Enrollment> getEnrollments(Integer studentId, int page, int pageSize) {
        return enrollmentUserRepository.findByStudentId(studentId, page, pageSize);
    }

    @Override
    public long getTotalEnrollments(Integer studentId) {
        return enrollmentUserRepository.countByStudentId(studentId);
    }
}
