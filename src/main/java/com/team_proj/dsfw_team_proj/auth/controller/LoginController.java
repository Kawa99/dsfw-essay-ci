package com.team_proj.dsfw_team_proj.auth.controller;

import org.h2.engine.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.team_proj.dsfw_team_proj.auth.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import com.team_proj.dsfw_team_proj.auth.entity.User;
import java.util.Objects;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    // --- REGISTER HANDLERS ---

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register_form"; // Ensure this matches your HTML filename
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user,
                               HttpSession session,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        //check if user with same email exists
        User existingUser = userService.findByEmail(user.getEmail());
        if (existingUser != null) {
            model.addAttribute("error", "An account with this email already exists.");
            return "register_form";
        }

        userService.save(user);

        // auto-login after registration
        //session.setAttribute("currentUser", user);

        // 3. redirect to dashboard with success message
        redirectAttributes.addFlashAttribute("message", "Account created successfully!");
        return "redirect:/dashboard";
    }

    // --- LOGIN HANDLERS ---

    @GetMapping("/login")
    public String showLoginPage(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser != null && !Objects.equals(currentUser.getEmail(), "")) {
            return "redirect:/dashboard";
        }
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") User user,
                        RedirectAttributes redirectAttributes,
                        HttpSession session) {

        User validUser = userService.validateUser(user.getEmail(), user.getPassword());

        if (validUser != null) {
            redirectAttributes.addFlashAttribute("message", "Login successful!");
            session.setAttribute("currentUser", validUser);
            return "redirect:/dashboard";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid email or password.");
            return "redirect:/login";
        }
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null || Objects.equals(currentUser.getEmail(), "")) {
            redirectAttributes.addFlashAttribute("error", "Please log in to access the dashboard.");
            return "redirect:/login";
        }

        model.addAttribute("user", currentUser);
        return "dashboard";
    }
}