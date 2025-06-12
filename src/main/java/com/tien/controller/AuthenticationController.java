package com.tien.controller;

import com.tien.dto.LoginDTO;
import com.tien.dto.StudentDTO;
import com.tien.service.AuthenticationService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/login_form")
    public String loginForm(Model model) {
        model.addAttribute("loginDTO", new LoginDTO()); // Đổi thành loginDTO
        return "login";
    }

    @PostMapping("/login_account")
    public String login(@Valid @ModelAttribute("loginDTO") LoginDTO loginDTO, // Đổi thành loginDTO
                        BindingResult bindingResult, Model model, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        StudentDTO authenticatedStudent = authenticationService.login(loginDTO.getEmail(), loginDTO.getPassword());


        if (authenticatedStudent != null) {
            if (authenticatedStudent.getRole() != null && authenticatedStudent.getRole()) {
                return "home_admin";
            } else {
                return "home_user";
            }
        } else {
            model.addAttribute("messageError", "Email hoặc mật khẩu không đúng. Vui lòng thử lại.");

            return "login";
        }
    }
    @GetMapping("/register_form")
    public String registerForm(Model model) {
        StudentDTO studentDTO = new StudentDTO();
        model.addAttribute("studentDTO", studentDTO);
        return "register";
    }

    @PostMapping("/register_save")
    public String register(@Valid @ModelAttribute("studentDTO") StudentDTO studentDTO
            , BindingResult bindingResult, Model model,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        authenticationService.register(studentDTO);
        redirectAttributes.addFlashAttribute("messageSuccess", "Đăng ký tài khoản thành công! Vui lòng đăng nhập.");
        return "redirect:/login_form";
    }
}