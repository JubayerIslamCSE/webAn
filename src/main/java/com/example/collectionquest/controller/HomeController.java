package com.example.collectionquest.controller;

import com.example.collectionquest.model.Anime;
import com.example.collectionquest.service.AnimeService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    private final AnimeService animeService;

    public HomeController(AnimeService animeService) {
        this.animeService = animeService;
    }

    // PUBLIC HOME — no login needed
    @GetMapping("/home")
    public String homePage(Model model, Authentication authentication) {
        model.addAttribute("animes", animeService.getAll());
        // Pass username to template so nav shows Login/Logout correctly
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("loggedInUser", authentication.getName());
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            model.addAttribute("isAdmin", isAdmin);
        }
        return "home";
    }

    // ANIME DETAIL — Spring Security requires login via SecurityConfig
    @GetMapping("/anime/{id}")
    public String animeDetail(@PathVariable Long id,
                              Model model,
                              Authentication authentication) {
        Anime anime = animeService.getById(id);
        if (anime == null) return "redirect:/home";

        List<Integer> episodes = new ArrayList<>();
        for (int i = 1; i <= anime.getTotalEpisodes(); i++) {
            episodes.add(i);
        }

        model.addAttribute("anime", anime);
        model.addAttribute("episodes", episodes);
        model.addAttribute("loggedInUser", authentication.getName());
        return "anime-detail";
    }
}