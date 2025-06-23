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
import ra.edu.entity.Enrollment;
import ra.edu.service.EnrollmentService;

import java.util.List;

@Controller
@RequestMapping("enrollment_manager")
public class EnrollmenManagerController {

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping("/show")
    public String showEnrollment(Model model,
                                 HttpSession session,
                                 @RequestParam(value = "page", defaultValue = "1") int page,
                                 @RequestParam(value = "status", required = false) String status,
                                 @RequestParam(value = "courseName", required = false) String courseName) {

        StudentDTO user = (StudentDTO) session.getAttribute("loggedInUser");
        if (user == null || !Boolean.TRUE.equals(user.getRole())) {
            return "redirect:/login_form";
        }

        final int pageSize = 5;

        // Trước khi truyền keyword cho Service/Repo, hãy trim nó:
        courseName = (courseName == null) ? "" : courseName.trim();


        List<Enrollment> enrollmentList = enrollmentService.filterAndSearch(status, courseName, page, pageSize);
        long total = enrollmentService.countFilteredAndSearched(status, courseName);
        int totalPages = (int) Math.ceil((double) total / pageSize);

        /* Bảo đảm luôn ≥ 1 để Thymeleaf không tạo sequence “1,0” */
        if (totalPages < 1) totalPages = 1;

        /* Ghìm trang hiện tại trong khoảng hợp lệ */
        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;

        model.addAttribute("enrollmentList", enrollmentList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("status", status);
        model.addAttribute("courseName", courseName);
        return "enrollment_manager";
    }


    @GetMapping("accept_enrollment/{id}")
    public String acceptEnrollment(
            @PathVariable("id") int id,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "courseName", required = false) String courseName
    ) {
        enrollmentService.acceptEnrollment(id);
        // Redirect về đúng trang và filter đang xem
        return "redirect:/enrollment_manager/show?page=" + page +
                (status != null ? "&status=" + status : "") +
                (courseName != null ? "&courseName=" + courseName : "");
    }

    @GetMapping("denied_enrollment/{id}")
    public String deniedEnrollment(
            @PathVariable("id") int id,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "courseName", required = false) String courseName
    ) {
        enrollmentService.deniedEnrollment(id);
        // Redirect về đúng trang và filter đang xem
        return "redirect:/enrollment_manager/show?page=" + page +
                (status != null ? "&status=" + status : "") +
                (courseName != null ? "&courseName=" + courseName : "");
    }
}
