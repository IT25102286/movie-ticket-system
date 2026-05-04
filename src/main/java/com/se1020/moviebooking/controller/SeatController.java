package com.se1020.moviebooking.controller;

import com.se1020.moviebooking.model.User;
import com.se1020.moviebooking.service.SeatService;
import com.se1020.moviebooking.service.ScreeningService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/seats")
public class SeatController {

    private final SeatService seatService;
    private final ScreeningService screeningService;

    public SeatController(SeatService seatService, ScreeningService screeningService) {
        this.seatService = seatService;
        this.screeningService = screeningService;
    }

    // READ — seat map for a screening
    @GetMapping("/screening/{screeningId}")
    public String seatMap(@PathVariable int screeningId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user != null && "ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/seats/admin/screening/" + screeningId;
        }

        model.addAttribute("seats", seatService.getSeatsByScreening(screeningId));
        model.addAttribute("seatRows", seatService.getSeatRowsByScreening(screeningId));
        model.addAttribute("screening", screeningService.getScreeningById(screeningId));
        return "seats/map";
    }

    // READ — seat arrangement view (admin only)
    @GetMapping("/admin/screening/{screeningId}")
    public String adminSeatMap(@PathVariable int screeningId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        if (!"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/home";

        model.addAttribute("seats", seatService.getSeatsByScreening(screeningId));
        model.addAttribute("seatRows", seatService.getSeatRowsByScreening(screeningId));
        model.addAttribute("screening", screeningService.getScreeningById(screeningId));
        return "seats/admin-map";
    }

    // CREATE — show generate form (admin)
    @GetMapping("/admin/generate")
    public String showGenerateForm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        if (!"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/home";

        model.addAttribute("screenings", screeningService.getAllScreenings());
        return "seats/generate";
    }

    // CREATE — submit seat generation (admin)
    @PostMapping("/admin/generate")
    public String generateSeats(@RequestParam int screeningId,
                                @RequestParam int rows,
                                @RequestParam int cols,
                                HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        if (!"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/home";

        seatService.generateSeats(screeningId, rows, cols);
        return "redirect:/seats/screening/" + screeningId;
    }

    // UPDATE — toggle seat status (admin)
    @PostMapping("/admin/update/{seatId}")
    public String updateSeatStatus(@PathVariable int seatId,
                                   @RequestParam boolean booked,
                                   @RequestParam int screeningId,
                                   HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        if (!"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/home";

        seatService.updateSeatStatus(seatId, booked);
        return "redirect:/seats/screening/" + screeningId;
    }

    // DELETE — remove all seats for a screening (admin)
    @PostMapping("/admin/delete/{screeningId}")
    public String deleteSeats(@PathVariable int screeningId, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        if (!"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/home";

        seatService.deleteSeatsByScreening(screeningId);
        return "redirect:/seats/admin/generate";
    }
}