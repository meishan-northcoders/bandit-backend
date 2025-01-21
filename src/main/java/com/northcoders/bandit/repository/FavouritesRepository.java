package com.northcoders.bandit.repository;

import com.northcoders.bandit.model.Favourites;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavouritesRepository extends CrudRepository<Favourites,Long> {
    List<Favourites> findAllByFavProfileId(String favProfileId);
    Favourites findByFavProfileIdAndYrFavProfileId(String favProfileId, String yrFavProfileId);
    List<Favourites> findByYrFavProfileId(String yrFavProfileId);
    List<Favourites> findByFavProfileId(String id);
    void deleteByFavProfileId(String id);

}
