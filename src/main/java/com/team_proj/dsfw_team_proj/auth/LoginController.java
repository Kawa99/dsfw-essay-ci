package com.team_proj.dsfw_team_proj.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new UserEntity());
        return "register_form";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserEntity user, Model model) {
        try {
            // SERVER-SIDE PASSWORD VALIDATION (NIST SP 800-63B compliant)
            List<String> passwordErrors = PasswordValidationUtil.validatePassword(
                    user.getPassword(),
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName()
            );

            if (!passwordErrors.isEmpty()) {
                String errorMessage = String.join(". ", passwordErrors);
                model.addAttribute("error", errorMessage);
                model.addAttribute("user", user);
                return "register_form";
            }

            userService.save(user);
            return "redirect:/login?success";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register_form";
        }
    }
}