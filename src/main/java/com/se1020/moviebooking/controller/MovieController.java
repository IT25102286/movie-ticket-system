package com.se1020.moviebooking.controller;

import com.se1020.moviebooking.model.Movie;
import com.se1020.moviebooking.model.User;
import com.se1020.moviebooking.service.MovieService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // READ — all movies (user-facing)
    @GetMapping
    public String listMovies(Model model) {
        model.addAttribute("movies", movieService.getAllMovies());
        return "movies/list";
    }

    // READ — single movie detail
    @GetMapping("/{id}")
    public String movieDetail(@PathVariable int id, Model model) {
        model.addAttribute("movie", movieService.getMovieById(id));
        return "movies/detail";
    }

    // CREATE — show add form (admin)
    @GetMapping("/admin/add")
    public String showAddForm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        if (!"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/home";

        model.addAttribute("movie", new Movie());
        return "movies/add";
    }

    // CREATE — submit add form (admin)
    @PostMapping("/admin/add")
    public String addMovie(@ModelAttribute Movie movie, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        if (!"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/home";

        movieService.addMovie(movie);
        return "redirect:/movies";
    }

    // UPDATE — show edit form (admin)
    @GetMapping("/admin/edit/{id}")
    public String showEditForm(@PathVariable int id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        if (!"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/home";

        model.addAttribute("movie", movieService.getMovieById(id));
        return "movies/edit";
    }

    // UPDATE — submit edit form (admin)
    @PostMapping("/admin/edit/{id}")
    public String updateMovie(@PathVariable int id, @ModelAttribute Movie movie, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        if (!"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/home";

        movie.setId(id);
        movieService.updateMovie(movie);
        return "redirect:/movies";
    }

    // DELETE (admin)
    @PostMapping("/admin/delete/{id}")
    public String deleteMovie(@PathVariable int id, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        if (!"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/home";

        movieService.deleteMovie(id);
        return "redirect:/movies";
    }
}