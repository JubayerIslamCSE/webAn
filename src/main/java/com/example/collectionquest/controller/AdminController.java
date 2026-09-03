package com.example.collectionquest.controller;

import com.example.collectionquest.model.Anime;
import com.example.collectionquest.service.AnimeService;
import com.example.collectionquest.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AnimeService animeService;
    private final UserService userService;

    public AdminController(AnimeService animeService, UserService userService) {
        this.animeService = animeService;
        this.userService = userService;
    }

    // ADMIN HOME — list all anime
    @GetMapping
    public String adminHome(Model model) {
        model.addAttribute("animes", animeService.getAll());
        return "admin/admin-list";
    }

    // USERS LIST
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAll());
        return "admin/admin-users";
    }

    // SHOW ADD FORM
    @GetMapping("/anime/new")
    public String showAddForm(Model model) {
        model.addAttribute("anime", new Anime());
        return "admin/admin-form";
    }

    // SHOW EDIT FORM
    @GetMapping("/anime/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("anime", animeService.getById(id));
        return "admin/admin-form";
    }

    // SAVE — handles image upload
    @PostMapping("/anime/save")
    public String saveAnime(@ModelAttribute Anime anime,
                            @RequestParam("imageFile") MultipartFile imageFile) throws IOException {

        if (!imageFile.isEmpty()) {
            String filename = imageFile.getOriginalFilename()
                    .replaceAll("\\s+", "_")
                    .replaceAll("[^a-zA-Z0-9._-]", "");

            Path uploadPath = Paths.get("uploads");
            Files.createDirectories(uploadPath);
            try (InputStream inputStream = imageFile.getInputStream()) {
                Files.copy(inputStream,
                        uploadPath.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
            }
            anime.setImageUrl(filename);
        }

        animeService.save(anime);
        return "redirect:/admin";
    }

    // DELETE
    @GetMapping("/anime/delete/{id}")
    public String deleteAnime(@PathVariable Long id) {
        animeService.delete(id);
        return "redirect:/admin";
    }
}