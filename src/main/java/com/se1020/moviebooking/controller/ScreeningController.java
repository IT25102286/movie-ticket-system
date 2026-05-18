package com.se1020.moviebooking.controller;

import com.se1020.moviebooking.model.Screening;
import com.se1020.moviebooking.model.User;
import com.se1020.moviebooking.model.Movie;
import com.se1020.moviebooking.service.MovieService;
import com.se1020.moviebooking.service.ScreeningService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

//sa
@Controller
@RequestMapping("/screenings")
public class ScreeningController {

    private final ScreeningService screeningService;
    private final MovieService movieService;

    public ScreeningController(ScreeningService screeningService, MovieService movieService) {
        this.screeningService = screeningService;
        this.movieService = movieService;
    }

    // READ — all screenings (admin)
    @GetMapping("/admin")
    public String listAll(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        if (!"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/home";

        model.addAttribute("screenings", screeningService.getAllScreenings());
        model.addAttribute("movies", movieService.getAllMovies());
        Map<Integer, String> movieTitleById = movieService.getAllMovies().stream()
                .collect(Collectors.toMap(Movie::getId, Movie::getTitle, (a, b) -> a));
        model.addAttribute("movieTitleById", movieTitleById);
        return "screenings/list";
    }

    // READ — screenings for a specific movie (user)
    @GetMapping("/movie/{movieId}")
    public String listByMovie(@PathVariable int movieId, Model model) {
        model.addAttribute("screenings", screeningService.getScreeningsByMovie(movieId));
        model.addAttribute("movie", movieService.getMovieById(movieId));
        return "screenings/byMovie";
    }

    // CREATE — show add form (admin)
    @GetMapping("/admin/add")
    public String showAddForm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        if (!"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/home";

        model.addAttribute("screening", new Screening());
        model.addAttribute("movies", movieService.getAllMovies());
        return "screenings/add";
    }

    // CREATE — submit (admin)
    @PostMapping("/admin/add")
    public String addScreening(@ModelAttribute Screening screening, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        if (!"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/home";

        screeningService.addScreening(screening);
        return "redirect:/screenings/admin";
    }

    // UPDATE — show edit form (admin)
    @GetMapping("/admin/edit/{id}")
    public String showEditForm(@PathVariable int id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        if (!"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/home";

        model.addAttribute("screening", screeningService.getScreeningById(id));
        model.addAttribute("movies", movieService.getAllMovies());
        return "screenings/edit";
    }

    // UPDATE — submit (admin)
    @PostMapping("/admin/edit/{id}")
    public String updateScreening(@PathVariable int id, @ModelAttribute Screening screening, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        if (!"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/home";

        screening.setId(id);
        screeningService.updateScreening(screening);
        return "redirect:/screenings/admin";
    }

    // DELETE (admin)
    @PostMapping("/admin/delete/{id}")
    public String deleteScreening(@PathVariable int id, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        if (!"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/home";

        screeningService.deleteScreening(id);
        return "redirect:/screenings/admin";
    }
}