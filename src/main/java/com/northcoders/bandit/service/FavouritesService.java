package com.northcoders.bandit.service;

import com.northcoders.bandit.dto.request.AddToFavouriteRequestDTO;
import com.northcoders.bandit.model.Favourites;
import com.northcoders.bandit.model.Profile;
import com.northcoders.bandit.repository.FavouritesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface FavouritesService {

    List<Favourites> getUserFavourites(String id);
    Favourites addFavourite(AddToFavouriteRequestDTO requestDTO);
    void removeFavourite(AddToFavouriteRequestDTO requestDTO);
    boolean areMutuallyFavourited(String uid1, String uid2);

    List<Favourites> getYrFavouritesProfileByFavProfileId(String favProfileId);
}
