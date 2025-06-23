package ra.edu.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ra.edu.dto.StudentDTO;
import ra.edu.entity.Student;
import ra.edu.service.StudentService;

import java.util.List;

@Controller
@RequestMapping("/student_manager")
public class StudentManagerController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/show")
    public String showStudents(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "sortField", defaultValue = "id") String sortField,
            @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
            @RequestParam(name = "keyword", required = false) String keyword,
            Model model,
            HttpSession session) {

        // 1. Kiểm tra đăng nhập & quyền
        StudentDTO loggedInUser = (StudentDTO) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !Boolean.TRUE.equals(loggedInUser.getRole())) {
            return "redirect:/login_form";
        }

        final int pageSize = 5;

        // Trước khi truyền keyword cho Service/Repo, hãy trim nó:
        keyword = (keyword == null) ? "" : keyword.trim();

        /* Đếm trước để biết tổng trang */
        int total = studentService.countStudents(keyword);
        int totalPages = (int) Math.ceil(total / (double) pageSize);

        /* Bảo đảm luôn ≥ 1 để Thymeleaf không tạo sequence “1,0” */
        if (totalPages < 1) totalPages = 1;

        /* Ghìm trang hiện tại trong khoảng hợp lệ */
        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;

        /* Lấy danh sách sau khi đã hiệu chỉnh page */
        List<Student> students =
                studentService.findStudents(page, pageSize, sortField, sortDir, keyword);

        model.addAttribute("students", students);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("keyword", keyword == null ? "" : keyword);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return "student_manager";
    }


    @GetMapping("/lock_student/{id}")
    public String lockStudent(@PathVariable("id") int id,
                              @RequestParam(name = "page", defaultValue = "1") int page,
                              @RequestParam(name = "keyword", required = false) String keyword,
                              RedirectAttributes redirectAttributes) {
        studentService.lockAndUnlockStudent(id);
        redirectAttributes.addAttribute("page", page);
        redirectAttributes.addAttribute("keyword", keyword);
        return "redirect:/student_manager/show";
    }

}


