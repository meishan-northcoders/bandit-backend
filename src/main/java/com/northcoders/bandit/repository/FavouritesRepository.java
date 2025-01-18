package com.northcoders.bandit.repository;

import com.northcoders.bandit.model.Favourites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavouritesRepository extends CrudRepository<Favourites,String> {
    List<Favourites> findAllByFavProfileId(String favProfileId);
}
