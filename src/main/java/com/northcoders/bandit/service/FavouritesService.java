package com.northcoders.bandit.service;

import com.northcoders.bandit.dto.request.AddToFavouriteRequestDTO;
import com.northcoders.bandit.model.Favourites;
import com.northcoders.bandit.model.Profile;
import com.northcoders.bandit.repository.FavouritesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FavouritesService {

    List<Favourites> getUserFavourites();
    Favourites addFavourite(AddToFavouriteRequestDTO requestDTO);
    void removeFavourite(AddToFavouriteRequestDTO requestDTO);
    boolean areMutuallyFavourited(String uid1, String uid2);

    List<Favourites> getYrFavouritesProfileByFavProfileId(String favProfileId);

    List<Favourites> getAllFavourites();

}
