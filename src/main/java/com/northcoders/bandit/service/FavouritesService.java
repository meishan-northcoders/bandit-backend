package com.northcoders.bandit.service;

import com.northcoders.bandit.model.Favourite;

import java.util.List;

public interface FavouritesService {

    List<Favourite> getUserFavourites(String id);
    Favourite addFavourite(String yrFavProfileId);
    void removeFavouriteById(String id);
    boolean areMutuallyFavourited(String uid1, String uid2);
    List<Favourite> getAllFavourites();

}
