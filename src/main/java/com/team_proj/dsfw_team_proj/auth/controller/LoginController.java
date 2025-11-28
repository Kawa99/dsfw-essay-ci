package com.team_proj.dsfw_team_proj.auth.controller;


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

    @GetMapping("/login")
    public String showLoginPage(Model model, RedirectAttributes redirectAttributes, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser != null && !Objects.equals(currentUser.getEmail(), "")) {                     // User is already logged in + security check
            model.addAttribute("user", currentUser);
            return "redirect:/dashboard";
        }
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes, HttpSession session) {
        User validUser = userService.validateUser(user.getEmail(), user.getPassword());
        if (validUser != null) {
            redirectAttributes.addFlashAttribute("message", "Login successful!");
            session.setAttribute("currentUser", validUser); // Store user in session
            return "redirect:/dashboard";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid email or password. Please try again.");
            return "redirect:/login";
        }
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session,RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || Objects.equals(currentUser.getEmail(), "")) {                     // User is not logged in + security check
            redirectAttributes.addFlashAttribute("error", "Please log in to access the dashboard.");
            return "redirect:/login";
        }
        model.addAttribute("user", currentUser);
        return "dashboard";
    }

}
