package com.example.collectionquest.controller;

import com.example.collectionquest.model.User;
import com.example.collectionquest.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // Redirect root to home
    @GetMapping("/")
    public String root() {
        return "redirect:/home";
    }

    // SHOW LOGIN PAGE
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // SHOW SIGNUP PAGE
    @GetMapping("/signup")
    public String showSignupPage(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    // HANDLE SIGNUP — hash password before saving
    @PostMapping("/signup")
    public String handleSignup(@ModelAttribute User user, Model model) {
        if (userService.existsByUsername(user.getUsername())) {
            model.addAttribute("error", "Username already taken");
            return "signup";
        }
        if (userService.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "Email already registered");
            return "signup";
        }
        // Hash the password before saving — never store plain text
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return "redirect:/login";
    }
}