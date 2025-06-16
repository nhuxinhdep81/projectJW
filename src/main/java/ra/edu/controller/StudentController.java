package ra.edu.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ra.edu.dto.StudentDTO;
import ra.edu.entity.Student;
import ra.edu.service.StudentService;

import java.util.List;

@Controller
@RequestMapping("/student_manager")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/show")
    public String showStudentManager(Model model,
                                     HttpSession session,
                                     @RequestParam(name = "page", defaultValue = "1") int page) {

        // 1. Kiểm tra đăng nhập & quyền
        StudentDTO loggedInUser = (StudentDTO) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !Boolean.TRUE.equals(loggedInUser.getRole())) {
            return "redirect:/login_form";
        }

        // 2. Cấu hình phân trang
        final int pageSize = 8;
        long totalStudents = studentService.countTotalStudents();
        long totalPages   = (long) Math.ceil((double) totalStudents / pageSize);

        // Bảo vệ khi người dùng gõ page “ngoài vùng phủ sóng”
        if (page < 1)           page = 1;
        if (page > totalPages)  page = (int) totalPages;

        // 3. Lấy dữ liệu trang hiện tại
        List<Student> students = studentService.findAllByPage(page, pageSize);

        // 4. Đẩy lên view
        model.addAttribute("students", students);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages",  totalPages);

        return "student_manager";
    }

}
