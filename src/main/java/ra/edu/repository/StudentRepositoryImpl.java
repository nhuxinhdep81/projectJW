package ra.edu.repository;

import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ra.edu.entity.Student ;

import java.util.List;

@Repository
@Transactional
public class StudentRepositoryImpl implements StudentRepository {


    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Student> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Student").list();
        }
    }

    @Override
    public List<Student > findAllByPage(int page, int pageSize) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Student ORDER BY id", Student .class)
                    .setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .list();
        }
    }

    @Override
    public long countTotalStudents() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("SELECT COUNT(s.id) FROM Student s", Long.class)
                    .uniqueResult();
        }
    }
}
