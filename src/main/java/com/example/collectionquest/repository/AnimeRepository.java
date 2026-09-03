package com.example.collectionquest.repository;

import com.example.collectionquest.model.Anime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimeRepository extends JpaRepository<Anime, Long> {
}



//This is your data access layer — its one and only job is talking to the database.
// Your service and controller never touch the database directly; they go through this repository.
// That's Single Responsibility in action: each layer does one thing.

//Just by extending JpaRepository, you instantly have these methods without writing them:
//findAll() → get every anime (for your list page)
//findById(Long id) → get one anime (for edit/details pages)
//save(Anime a) → insert or update (for add/edit)
//deleteById(Long id) → delete one (for the delete action)
//count(), existsById(), and more