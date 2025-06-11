package com.tien.controller;

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

@Controller
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/login_form")
    public String loginForm(Model model) {
        model.addAttribute("studentDTO", new StudentDTO());
        return "login";
    }

    @PostMapping("/login_account")
    public String login(@Valid @ModelAttribute("studentDTO") StudentDTO studentDTO,
                        BindingResult bindingResult, Model model, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        StudentDTO authenticatedStudent = authenticationService.login(studentDTO.getEmail(), studentDTO.getPassword());

        if (authenticatedStudent != null) {
            if (authenticatedStudent.getRole() != null && authenticatedStudent.getRole()) {
                return "home_admin";
            } else {
                return "home_user";
            }
        } else {
            model.addAttribute("messageError", "Email hoặc mật khẩu không đúng. Vui lòng thử lại.");
            System.out.println("Đăng nhập sai: " + studentDTO.getEmail());
            return "login";
        }

    }
}