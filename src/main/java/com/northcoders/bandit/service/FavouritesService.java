package com.northcoders.bandit.service;

import com.northcoders.bandit.model.Favourites;
import com.northcoders.bandit.model.Profile;
import com.northcoders.bandit.repository.FavouritesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface FavouritesService {

    List<Favourites> getUserFavourites(String id);
    Favourites addFavourite(String id);
    void removeFavouriteById(String id);
    boolean areMutuallyFavourited(String uid1, String uid2);


}
