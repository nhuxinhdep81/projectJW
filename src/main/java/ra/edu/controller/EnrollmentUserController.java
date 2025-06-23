package ra.edu.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ra.edu.dto.StudentDTO;
import ra.edu.service.EnrollmentUserService;

@Controller
@RequestMapping("/enrollments")
public class EnrollmentUserController {

    @Autowired
    private EnrollmentUserService enrollmentUserService;

    private static final int PAGE_SIZE = 5;


    @GetMapping("/list")
    public String list(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model, HttpSession session) {

        StudentDTO user = (StudentDTO) session.getAttribute("loggedInUser");
        if (user == null || Boolean.TRUE.equals(user.getRole())) {
            return "redirect:/login_form";
        }

        final int SIZE = 5;

        // Trước khi truyền keyword cho Service/Repo, hãy trim nó:
        keyword = (keyword == null) ? "" : keyword.trim();

        var enrollments = enrollmentUserService.searchEnrollments(
                user.getId(), keyword, status, page, SIZE);

        long total = enrollmentUserService.countSearchEnrollments(
                user.getId(), keyword, status);

        int totalPages = (int) Math.ceil((double) total / SIZE);

        /* Bảo đảm luôn ≥ 1 để Thymeleaf không tạo sequence “1,0” */
        if (totalPages < 1) totalPages = 1;

        /* Ghìm trang hiện tại trong khoảng hợp lệ */
        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;

        model.addAttribute("enrollments", enrollments);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "list_enrollment";
    }

    @GetMapping("/cancel_enrollment/{id}")
    public String cancelEnrollment(
            @PathVariable("id") int id,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "page", defaultValue = "1") int page) {

        enrollmentUserService.cancelEnrollment(id);

        return "redirect:/enrollments/list?page=" + page +
                (keyword != null ? "&keyword=" + keyword : "") +
                (status != null ? "&status=" + status : "");
    }

}
