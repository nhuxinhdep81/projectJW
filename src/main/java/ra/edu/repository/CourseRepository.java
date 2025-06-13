package ra.edu.repository;

import ra.edu.entity.Course;

import java.util.List;

public interface CourseRepository {

    List<Course> findAllByPage(int page, int pageSize);
    long countTotalCourses();


    List<Course> findAll();

    void addOrUpdateCourse(Course course);

    Course checkExistCourseName(String name); // hàm check trùng tên khoá học

    Course getCourseById(int id);

    List<Course> searchCourseByName(String keyword, int offset, int limit); // hàm tìm kiếm khoá học thoe tên
    long countSearchCourseByName(String keyword);

    void deleteCourse(Course course);
}
