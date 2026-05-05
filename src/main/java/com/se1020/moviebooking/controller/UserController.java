package com.se1020.moviebooking.controller;

import com.se1020.moviebooking.model.User;
import com.se1020.moviebooking.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/*
REST API - 
CREATE - POST
UPDATE - PUT
VIEW - GET
DELETE - DELETE
*/

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // READ — login page
    @GetMapping("/login")
    public String loginPage() {
        return "users/login";
    }

    // READ — login submit
    // UserController.java — updated login
    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session) {
        User user = userService.login(email, password);
        if (user != null) {
            session.setAttribute("loggedInUser", user);
            return "redirect:" + user.getDashboardPath(); // polymorphism in action
        }
        return "redirect:/login?error";
    }
    // CREATE — register page
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "users/register";
    }

    // CREATE — register submit
    @PostMapping("/register")
    public String register(@ModelAttribute User user) {
        if (userService.emailExists(user.getEmail())) {
            return "redirect:/register?error";
        }
        userService.registerUser(user);
        return "redirect:/login";
    }

    // READ — view own profile
    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        model.addAttribute("user", userService.getUserById(user.getId()));
        return "users/profile";
    }

    // UPDATE — update profile
    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute User user, HttpSession session) {
        userService.updateUser(user);
        session.setAttribute("loggedInUser", userService.getUserById(user.getId()));
        return "redirect:/profile";
    }

    // DELETE — delete own account
    @PostMapping("/profile/delete")
    public String deleteAccount(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user != null) {
            userService.deleteUser(user.getId());
            session.invalidate();
        }
        return "redirect:/login";
    }

    // Admin — list all users
    @GetMapping("/admin/users")
    public String listUsers(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        if (!"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/home";

        model.addAttribute("users", userService.getAllUsers());
        return "users/adminList";
    }

    // Admin — delete a user
    @PostMapping("/admin/users/delete/{id}")
    public String adminDeleteUser(@PathVariable int id, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        if (!"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/home";
        if (user.getId() == id) return "redirect:/admin/users?error=self-delete";

        userService.deleteUser(id);
        return "redirect:/admin/users";
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}