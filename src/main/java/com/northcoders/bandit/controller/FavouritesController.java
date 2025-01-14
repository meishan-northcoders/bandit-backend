package com.northcoders.bandit.controller;

import com.northcoders.bandit.model.Favourites;
import com.northcoders.bandit.service.FavouritesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/favourites")
public class FavouritesController {

    //MVP:
    private final FavouritesService favouritesService;

    // Some functions need refactoring to return profiles when integrated into system.

    @Autowired
    public FavouritesController(FavouritesService favouritesService) {
        this.favouritesService = favouritesService;
    }

    @GetMapping
    public ResponseEntity<List<Favourites>> getUserFavourites(Long id) {
        return new ResponseEntity<>(favouritesService.getUserFavourites(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Favourites> addFavourite(Long id) {
        return favouritesService.addFavourite(id);
    }
    @DeleteMapping
    public void removeFavouriteById(Long id){
        favouritesService.removeFavouriteById(id);
    }


    // Nice to have functions

    // Function to check if two users are mutually favourited:

    public ResponseEntity<Boolean> areMutuallyFavourited(String uid1, String uid2){
        return new ResponseEntity<>(favouritesService.areMutuallyFavourited(uid1, uid2), HttpStatus.OK);
    }



}
