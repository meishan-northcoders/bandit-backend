package com.northcoders.bandit.repository;

import com.northcoders.bandit.model.Genre;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreManagerRepository extends CrudRepository<Genre, Long> {
}
