package com.northcoders.bandit.controller;

import com.northcoders.bandit.model.Favourites;
import com.northcoders.bandit.service.FavouritesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/favourites")
public class FavouritesController {

    private final FavouritesService favouritesService;

    @Autowired
    public FavouritesController(FavouritesService favouritesService) {
        this.favouritesService = favouritesService;
    }

    @GetMapping
    public List<Favourites> getUserFavourites(Long id) {
        return favouritesService.getUserFavourites(id);
    }

    @PostMapping
    public Favourites addFavourite(Long id) {
        return favouritesService.addFavourite(id);
    }
    @DeleteMapping
    public void removeFavouriteById(Long id){
        favouritesService.removeFavouriteById(id);
    }

}
