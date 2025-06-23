package ra.edu.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ra.edu.dto.CourseDTO;
import ra.edu.dto.StudentDTO;
import ra.edu.entity.Course;
import ra.edu.service.CourseService;
import ra.edu.service.EnrollmentService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 30
)
@RequestMapping("/course_manager")
public class CourseManagerController {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping("/show")
    public String showCourseManager(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
            @RequestParam(name = "add", required = false) String add,
            @RequestParam(name = "edit", required = false) Integer editId,
            @RequestParam(name = "confirm", required = false) Integer confirmId,
            HttpSession session, Model model) {

        StudentDTO loggedInUser = (StudentDTO) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !Boolean.TRUE.equals(loggedInUser.getRole())) {
            return "redirect:/login_form";
        }

        int pageSize = 5;

        // Trim keyword
        keyword = (keyword == null) ? "" : keyword.trim();

        long totalCourses = courseService.countSearchedCourses(keyword);
        int totalPages = (totalCourses == 0) ? 0 : (int) Math.ceil((double) totalCourses / pageSize);

        if (page < 1) page = 1;
        if (page > totalPages && totalPages > 0) page = totalPages;

        List<Course> courses = (totalCourses > 0)
                ? courseService.searchAndSortCourses(keyword, sortBy, sortDir, page, pageSize)
                : List.of();

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("listCourse", courses);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);

        // Modal add course
        if (add != null) {
            model.addAttribute("showAddModal", true);
            model.addAttribute("courseDTO", new CourseDTO());
        }

        // Modal edit course
        if (editId != null) {
            Course course = courseService.getCourseById(editId);
            CourseDTO courseDTO = new CourseDTO();
            courseDTO.setId(course.getId());
            courseDTO.setName(course.getName());
            courseDTO.setDuration(course.getDuration());
            courseDTO.setInstructor(course.getInstructor());
            courseDTO.setCreateAt(course.getCreateAt());
            courseDTO.setImage(course.getImage());
            model.addAttribute("showEditModal", true);
            model.addAttribute("courseDTO", courseDTO);
        }

        // Modal confirm delete
        if (confirmId != null) {
            Course course = courseService.getCourseById(confirmId);
            model.addAttribute("showConfirmModal", true);
            model.addAttribute("confirmCourse", course);
        }

        return "course_manager";
    }

    // Validate ảnh khi thêm mới
    @PostMapping("/save")
    public String saveAddCourse(@Valid @ModelAttribute("courseDTO") CourseDTO courseDTO,
                                BindingResult bindingResult,
                                @RequestParam(name = "page", defaultValue = "1") int page,
                                Model model) {
        MultipartFile fileImage = courseDTO.getImageFile();

        // Nếu không có file ảnh, báo lỗi
        if (fileImage == null || fileImage.isEmpty()) {
            bindingResult.rejectValue("imageFile", "error.courseDTO", "Ảnh không được để trống");
        }

        if (bindingResult.hasErrors()) {
            int pageSize = 5;
            long totalCourses = courseService.countTotalCourses();
            int totalPages = (totalCourses == 0) ? 0 : (int) Math.ceil((double) totalCourses / pageSize);

            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("listCourse", courseService.findAllByPage(page, pageSize));
            model.addAttribute("showAddModal", true);
            return "course_manager";
        }

        if (courseService.isCourseNameDuplicate(courseDTO.getName())) {
            bindingResult.rejectValue("name", "error.courseDTO", "Tên khoá học đã tồn tại");

            int pageSize = 5;
            long totalCourses = courseService.countTotalCourses();
            int totalPages = (totalCourses == 0) ? 0 : (int) Math.ceil((double) totalCourses / pageSize);

            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("listCourse", courseService.findAllByPage(page, pageSize));
            model.addAttribute("showAddModal", true);

            return "course_manager";
        }

        try {
            Map uploadResult = cloudinary.uploader().upload(fileImage.getBytes(), ObjectUtils.emptyMap());
            String url = uploadResult.get("url").toString();
            courseDTO.setImage(url);

            courseService.addOrUpdateCourse(courseDTO);
        } catch (IOException exception) {
            model.addAttribute("error", "Lỗi upload ảnh: " + exception.getMessage());
            model.addAttribute("showAddModal", true);
            return "course_manager";
        }

        return "redirect:/course_manager/show";
    }

    // Edit: Giữ lại ảnh cũ nếu không chọn ảnh mới
    @PostMapping("/save_edit_course")
    public String saveEditCourse(@Valid @ModelAttribute("courseDTO") CourseDTO courseDTO,
                                 BindingResult bindingResult,
                                 @RequestParam(name = "page", defaultValue = "1") int page,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            int pageSize = 5;
            long totalCourses = courseService.countTotalCourses();
            int totalPages = (totalCourses == 0) ? 0 : (int) Math.ceil((double) totalCourses / pageSize);

            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("listCourse", courseService.findAllByPage(page, pageSize));
            model.addAttribute("showEditModal", true);
            return "course_manager";
        }

        if (courseService.isCourseNameDuplicate(courseDTO.getName(), courseDTO.getId())) {
            bindingResult.rejectValue("name", "error.courseDTO", "Tên khoá học đã tồn tại");

            int pageSize = 5;
            long totalCourses = courseService.countTotalCourses();
            int totalPages = (totalCourses == 0) ? 0 : (int) Math.ceil((double) totalCourses / pageSize);

            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("listCourse", courseService.findAllByPage(page, pageSize));
            model.addAttribute("showEditModal", true);

            return "course_manager";
        }

        MultipartFile fileImage = courseDTO.getImageFile();
        try {
            if (fileImage != null && !fileImage.isEmpty()) {
                // Nếu có file ảnh mới
                Map uploadResult = cloudinary.uploader().upload(fileImage.getBytes(), ObjectUtils.emptyMap());
                String url = uploadResult.get("url").toString();
                courseDTO.setImage(url);
            }
            // Nếu không có file ảnh mới, giữ nguyên giá trị image từ hidden field
            courseService.addOrUpdateCourse(courseDTO);
        } catch (IOException exception) {
            model.addAttribute("error", "Lỗi upload ảnh: " + exception.getMessage());
            model.addAttribute("showEditModal", true);
            return "course_manager";
        }

        return "redirect:/course_manager/show";
    }

    @PostMapping("/delete_course/{id}")
    public String deleteCourse(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        Course course = courseService.getCourseById(id);
        long confirmedCount = enrollmentService.countConfirmedEnrollmentsByCourseId(id);
        if (confirmedCount > 0) {
            redirectAttributes.addFlashAttribute("error", "Khoá học không thể xoá vì đã có học sinh theo học");
        } else {
            courseService.deleteCourse(course);
            redirectAttributes.addFlashAttribute("success", "Xoá khoá học thành công");
        }
        return "redirect:/course_manager/show";
    }
}