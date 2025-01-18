package com.northcoders.bandit.controller;

import com.northcoders.bandit.dto.request.AddToFavouriteRequestDTO;
import com.northcoders.bandit.model.Favourites;
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
    // favourites
    private final FavouritesService favouritesService;
    private final ProfileManagerController profileManagerController;


    // Some functions need refactoring to return profiles when integrated into system.

    @Autowired
    public FavouritesController(FavouritesService favouritesService, ProfileManagerController profileManagerController) {
        this.favouritesService = favouritesService;
        this.profileManagerController = profileManagerController;
    }

//    @GetMapping("/{favProfileId}")
//    public ResponseEntity<List<Profile>> getUserFavourites(@PathVariable(name = "favProfileId") String favProfileId) {
//        List<Favourites> getFavouritesFromId = favouritesService.getUserFavourites(favProfileId);
//        return profileManagerController.getUserFavourites(getFavouritesFromId);
//    }

    @PostMapping("")
    public ResponseEntity<Favourites> addFavourite(@RequestBody AddToFavouriteRequestDTO requestDTO) {
        return new ResponseEntity<>(favouritesService.addFavourite(requestDTO), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> removeFavourite(@RequestBody AddToFavouriteRequestDTO requestDTO){
        favouritesService.removeFavourite(requestDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    // Nice to have functions

    // Function to check if two users are mutually favourited:

//    public ResponseEntity<Boolean> areMutuallyFavourited(String uid1, String uid2){
//        return new ResponseEntity<>(favouritesService.areMutuallyFavourited(uid1, uid2), HttpStatus.OK);
//    }



}
