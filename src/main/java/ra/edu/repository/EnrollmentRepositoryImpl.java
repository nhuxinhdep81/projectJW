        package ra.edu.repository;

        import jakarta.transaction.Transactional;
        import org.hibernate.Session;
        import org.hibernate.SessionFactory;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Repository;
        import ra.edu.entity.Enrollment;

        import java.util.List;
        import java.util.stream.Collectors;

        @Repository
        @Transactional
        public class EnrollmentRepositoryImpl implements EnrollmentRepository {

            @Autowired
            private SessionFactory sessionFactory;

            @Override
            public void save(Enrollment enrollment) {
                try (Session session = sessionFactory.openSession()) {
                    session.beginTransaction();
                    session.save(enrollment);
                    session.getTransaction().commit();
                }
            }

            @Override
            public boolean hasEnrolled(int studentId, int courseId) {
                try (Session session = sessionFactory.openSession()) {
                    Long count = session.createQuery("SELECT COUNT(e.id) FROM Enrollment e WHERE e.student.id = :sid AND e.course.id = :cid", Long.class)
                            .setParameter("sid", studentId)
                            .setParameter("cid", courseId)
                            .uniqueResult();
                    return count != null && count > 0;
                }
            }

            @Override
            public List<Integer> getRegisteredCourseIds(int studentId) {
                try (Session session = sessionFactory.openSession()) {
                    List<Enrollment> enrollments = session.createQuery("FROM Enrollment e WHERE e.student.id = :sid", Enrollment.class)
                            .setParameter("sid", studentId)
                            .list();
                    return enrollments.stream().map(e -> e.getCourse().getId()).collect(Collectors.toList());
                }
            }

            @Override
            public long countTotalEnrollments() {
                try (Session session = sessionFactory.openSession()) {
                    return session.createQuery(
                                    "SELECT COUNT(e.id) FROM Enrollment e WHERE e.status = :status", Long.class)
                            .setParameter("status", Enrollment.Status.CONFIRM)
                            .uniqueResult();
                }
            }

            @Override
            public List<Object[]> countStudentByCourse() {
                try (Session session = sessionFactory.openSession()) {
                    return session.createQuery(
                                    "SELECT e.course, COUNT(e.id) " +
                                            "FROM Enrollment e WHERE e.status = :status " +
                                            "GROUP BY e.course " +
                                            "order by count(e.id) DESC", Object[].class)
                            .setParameter("status", Enrollment.Status.CONFIRM)
                            .list();
                }
            }

            @Override
            public List<Object[]> top5CoursesByEnrollment() {
                try (Session session = sessionFactory.openSession()) {
                    return session.createQuery(
                                    "SELECT e.course, COUNT(e.id) " +
                                            "FROM Enrollment e WHERE e.status = :status " +
                                            "GROUP BY e.course " +
                                            "ORDER BY COUNT(e.id) DESC", Object[].class)
                            .setParameter("status", Enrollment.Status.CONFIRM)
                            .setMaxResults(5)
                            .list();
                }
            }

            @Override
            public List<Enrollment> showListEnrollments(int page, int pageSize) {
                try (Session session = sessionFactory.openSession()) {
                    return session.createQuery("from Enrollment e order by e.course.name desc")
                            .setFirstResult((page - 1) * pageSize)
                            .setMaxResults(pageSize)
                            .list();
                }
            }


            @Override
            public long countAllEnrollements(){
                try (Session session = sessionFactory.openSession()) {
                    return session.createQuery("select count(e.id) from Enrollment e", Long.class).uniqueResult();
                }
            }

            @Override
            public List<Enrollment> filterAndSearch(String status, String courseName, int page, int pageSize) {
                try (Session session = sessionFactory.openSession()) {
                    StringBuilder hql = new StringBuilder("FROM Enrollment e WHERE 1=1");
                    if (status != null && !status.isEmpty() && !status.equalsIgnoreCase("Select By Status")) {
                        hql.append(" AND e.status = :status");
                    }
                    if (courseName != null && !courseName.trim().isEmpty()) {
                        hql.append(" AND LOWER(e.course.name) LIKE :courseName");
                    }
                    hql.append(" ORDER BY e.course.name DESC");
                    var query = session.createQuery(hql.toString(), Enrollment.class);
                    if (status != null && !status.isEmpty() && !status.equalsIgnoreCase("Select By Status")) {
                        query.setParameter("status", Enrollment.Status.valueOf(status.toUpperCase()));
                    }
                    if (courseName != null && !courseName.trim().isEmpty()) {
                        query.setParameter("courseName", "%" + courseName.trim().toLowerCase() + "%");
                    }
                    query.setFirstResult((page - 1) * pageSize);
                    query.setMaxResults(pageSize);
                    return query.list();
                }
            }

            @Override
            public long countFilteredAndSearched(String status, String courseName) {
                try (Session session = sessionFactory.openSession()) {
                    StringBuilder hql = new StringBuilder("SELECT COUNT(e.id) FROM Enrollment e WHERE 1=1");
                    if (status != null && !status.isEmpty() && !status.equalsIgnoreCase("Select By Status")) {
                        hql.append(" AND e.status = :status");
                    }
                    if (courseName != null && !courseName.trim().isEmpty()) {
                        hql.append(" AND LOWER(e.course.name) LIKE :courseName");
                    }
                    var query = session.createQuery(hql.toString(), Long.class);
                    if (status != null && !status.isEmpty() && !status.equalsIgnoreCase("Select By Status")) {
                        query.setParameter("status", Enrollment.Status.valueOf(status.toUpperCase()));
                    }
                    if (courseName != null && !courseName.trim().isEmpty()) {
                        query.setParameter("courseName", "%" + courseName.trim().toLowerCase() + "%");
                    }
                    return query.uniqueResult();
                }
            }

            @Override
            public void acceptEnrollment(int id) {
                try (Session session = sessionFactory.openSession()) {
                    session.beginTransaction();
                    Enrollment enrollment = session.get(Enrollment.class, id);
                    enrollment.setStatus(Enrollment.Status.CONFIRM);
                    session.update(enrollment);
                    session.getTransaction().commit();
                }
            }

            @Override
            public void deniedEnrollment(int id){
                try (Session session = sessionFactory.openSession()) {
                    session.beginTransaction();
                    Enrollment enrollment = session.get(Enrollment.class, id);
                    enrollment.setStatus(Enrollment.Status.DENIED);
                    session.update(enrollment);
                    session.getTransaction().commit();
                }
            }

            @Override
            public long countConfirmedEnrollmentsByCourseId(int courseId) {
                try (Session session = sessionFactory.openSession()) {
                    return session.createQuery(
                                    "SELECT COUNT(e.id) FROM Enrollment e WHERE e.status = :status AND e.course.id = :cid", Long.class)
                            .setParameter("status", Enrollment.Status.CONFIRM)
                            .setParameter("cid", courseId)
                            .uniqueResult();
                }
            }
        }