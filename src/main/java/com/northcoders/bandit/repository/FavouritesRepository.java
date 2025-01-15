package com.northcoders.bandit.repository;

import com.northcoders.bandit.model.Favourite;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavouritesRepository extends CrudRepository<Favourite,String> {
    List<Favourite> findByFavProfileId(String id);
}
