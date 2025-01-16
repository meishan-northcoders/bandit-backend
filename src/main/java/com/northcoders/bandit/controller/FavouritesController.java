package com.northcoders.bandit.controller;

import com.northcoders.bandit.model.Favourite;
import com.northcoders.bandit.model.Profile;
import com.northcoders.bandit.service.FavouritesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/favourites")
public class FavouritesController {

    //MVP:
    private final FavouritesService favouritesService;
    private final ProfileManagerController profileManagerController;


    // Some functions need refactoring to return profiles when integrated into system.

    @Autowired
    public FavouritesController(FavouritesService favouritesService, ProfileManagerController profileManagerController) {
        this.favouritesService = favouritesService;
        this.profileManagerController = profileManagerController;
    }

    @GetMapping
    public ResponseEntity<List<Favourite>>getAllFavourites(){
        return new ResponseEntity<>(favouritesService.getAllFavourites(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Profile>> getUserFavourites(@PathVariable String id) {
        List<Favourite> getFavouriteFromId = favouritesService.getUserFavourites(id);
                return profileManagerController.getUserFavourites(getFavouriteFromId);
    }

    @PostMapping("/{yrFavProfileId}")
    public ResponseEntity<Favourite> addFavourite(@PathVariable String yrFavProfileId) {
        return new ResponseEntity<>(favouritesService.addFavourite(yrFavProfileId),HttpStatus.OK);
    }

    @DeleteMapping
    public void removeFavouriteById(String id){
        favouritesService.removeFavouriteById(id);
    }


    // Nice to have functions

    // Function to check if two users are mutually favourited:

//    public ResponseEntity<Boolean> areMutuallyFavourited(String uid1, String uid2){
//        return new ResponseEntity<>(favouritesService.areMutuallyFavourited(uid1, uid2), HttpStatus.OK);
//    }



}
