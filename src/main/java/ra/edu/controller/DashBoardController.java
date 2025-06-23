package ra.edu.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ra.edu.dto.StudentDTO;
import ra.edu.service.CourseService;
import ra.edu.service.EnrollmentService;
import ra.edu.service.StudentService;

import java.util.List;

@Controller
public class DashBoardController {


    @Autowired
    private CourseService courseService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private EnrollmentService enrollmentService;

    // --- TRANG DASHBOARD CHO ADMIN (ĐÃ CẬP NHẬT) ---
    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        StudentDTO loggedInUser = (StudentDTO) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !Boolean.TRUE.equals(loggedInUser.getRole())) {
            return "redirect:/login_form";
        }

        int totalStudent = studentService.countStudents("");

        long totalCourse = courseService.countTotalCourses();

        long totalEnrollment = enrollmentService.countTotalEnrollments();

        List<Object[]> studentByCourse = enrollmentService.countStudentByCourse();

        List<Object[]> top5Courses = enrollmentService.top5CoursesByEnrollment();

        model.addAttribute("totalStudent", totalStudent);
        model.addAttribute("totalCourse", totalCourse);
        model.addAttribute("totalEnrollment", totalEnrollment);
        model.addAttribute("studentByCourse", studentByCourse);
        model.addAttribute("top5Courses", top5Courses);

        return "home_admin";
    }
}
