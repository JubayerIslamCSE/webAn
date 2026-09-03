package com.example.collectionquest.service;

import com.example.collectionquest.model.Anime;
import java.util.List;

public interface AnimeService {
    List<Anime> getAll();
    Anime getById(Long id);
    void save(Anime anime);
    void delete(Long id);
}