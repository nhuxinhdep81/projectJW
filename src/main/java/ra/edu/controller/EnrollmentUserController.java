package ra.edu.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String showEnrollments(Model model,
                                  HttpSession session,
                                  @RequestParam(name = "page",defaultValue = "1") int page) {

        StudentDTO loggedInUser = (StudentDTO) session.getAttribute("loggedInUser");
        if (loggedInUser == null || Boolean.TRUE.equals(loggedInUser.getRole())) {
            return "redirect:/login_form";
        }

        var enrollments = enrollmentUserService.getEnrollments(loggedInUser.getId(), page, PAGE_SIZE);
        long total = enrollmentUserService.getTotalEnrollments(loggedInUser.getId());
        int totalPages = (int) Math.ceil((double) total / PAGE_SIZE);

        model.addAttribute("enrollments", enrollments);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "list_enrollment";
    }
}
