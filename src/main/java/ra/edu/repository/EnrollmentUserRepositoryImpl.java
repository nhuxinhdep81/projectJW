package ra.edu.repository;

import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ra.edu.entity.Enrollment;

import java.util.List;

@Repository
@Transactional
public class EnrollmentUserRepositoryImpl implements EnrollmentUserRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @SuppressWarnings("unchecked")
    public List<Enrollment> findByStudentId(Integer studentId, int page, int pageSize) {
        Session session = sessionFactory.getCurrentSession();
        String hql = """
                     from Enrollment e
                     join fetch e.course      
                     where e.student.id = :sid
                     order by e.registeredAt desc
                     """;
        return session.createQuery(hql)
                .setParameter("sid", studentId)
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize)
                .list();
    }

    @Override
    public long countByStudentId(Integer studentId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "select count(e.id) from Enrollment e where e.student.id = :sid";
        return (long) session.createQuery(hql)
                .setParameter("sid", studentId)
                .uniqueResult();
    }
}
