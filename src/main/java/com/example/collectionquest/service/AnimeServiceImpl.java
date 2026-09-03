package com.example.collectionquest.service;

import com.example.collectionquest.model.Anime;
import com.example.collectionquest.repository.AnimeRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class AnimeServiceImpl implements AnimeService {

    private final AnimeRepository repository;

    public AnimeServiceImpl(AnimeRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Anime> getAll() {
        return repository.findAll();
    }

    @Override
    public Anime getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void save(Anime anime) {
        repository.save(anime);
    }

    @Override
    public void delete(Long id) {
        // Get the anime first so we know the image filename
        Anime anime = repository.findById(id).orElse(null);

        if (anime != null && anime.getImageUrl() != null && !anime.getImageUrl().isEmpty()) {
            // Delete the image file from static/
            Path imagePath = Paths.get("uploads/" + anime.getImageUrl());
            try {
                Files.deleteIfExists(imagePath);
            } catch (IOException e) {
                // Log but don't crash — still delete from DB even if file missing
                System.out.println("Could not delete image file: " + anime.getImageUrl());
            }
        }

        // Now delete from database
        repository.deleteById(id);
    }
}