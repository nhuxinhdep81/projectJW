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

    Course findCourseByName(String name);

    void deleteCourse(Course course);
    // Thêm hàm kiểm tra số lượng đơn CONFIRM của khoá học

    List<Course> searchAndSortCourses(String keyword, String sortBy, String sortDir, int page, int pageSize);
    long countSearchedCourses(String keyword);
}