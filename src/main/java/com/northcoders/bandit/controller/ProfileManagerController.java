package com.northcoders.bandit.controller;

import com.northcoders.bandit.model.Favourites;
import com.northcoders.bandit.model.Profile;
import com.northcoders.bandit.service.ProfileManagerService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/profiles")
public class ProfileManagerController {

    @Autowired
    ProfileManagerService profileManagerService;

    @GetMapping
    public ResponseEntity<ArrayList<Profile>> getAllProfiles(){
        return new ResponseEntity<>(profileManagerService.getAllProfiles(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Profile> postProfile(@RequestBody Profile profile){
        return new ResponseEntity<>(profileManagerService.postProfile(profile), HttpStatus.OK);
    }

    @GetMapping("/favourites/{favProfileId}")
    public ResponseEntity<List<Profile>> getListOfFavProfile(@PathVariable(name = "favProfileId") String favProfileId) {
        List<Profile> profileList = profileManagerService.getListOfFavProfile(favProfileId);

        return new ResponseEntity<>(profileList, HttpStatus.OK);
    }

    //TODO refactor to use firebase id
    @DeleteMapping
    public ResponseEntity<String> deleteProfile(@RequestParam(value = "id") String id){
        boolean isDeleted = profileManagerService.deleteById(id);
        if(isDeleted){
            return new ResponseEntity<>("Successfully deleted profile with id :" + id, HttpStatus.OK);
        }
        return new ResponseEntity<>("Failed to delete profile with id :" + id,HttpStatus.NOT_FOUND);
    }


    public ResponseEntity<List<Profile>> getUserFavourites(List<Favourites> favourites){
        return new ResponseEntity<>(profileManagerService.getUserFavourites(favourites), HttpStatus.OK);
    }

    //TODO discuss name scheme for filtered profiles (e.g. the recommended profiles based on service layer algorithm)
    //I have kept no request param as filtering will take place using the user's firebase id entirely in backend service layer
    @GetMapping("/filtered")
    public ResponseEntity<ArrayList<Profile>> getFilteredProfiles(){
        return new ResponseEntity<>(profileManagerService.getFilteredProfiles(), HttpStatus.OK);
    }

}
