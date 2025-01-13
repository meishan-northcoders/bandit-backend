package com.northcoders.bandit.controller;

import com.northcoders.bandit.model.Profile;
import com.northcoders.bandit.service.ProfileManagerService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;

@RestController
@RequestMapping("/profile")
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

    //TODO refactor to use firebase id
    @DeleteMapping
    public ResponseEntity<String> deleteProfile(@RequestParam(value = "id") long id){
        boolean isDeleted = profileManagerService.deleteById(id);
        if(isDeleted){
            return new ResponseEntity<>("Successfully deleted profile with id :" + id, HttpStatus.OK);
        }
        return new ResponseEntity<>("Failed to delete profile with id :" + id,HttpStatus.NOT_FOUND);
    }

    //TODO discuss name scheme for filtered profiles (e.g. the recommended profiles based on service layer algorithm)
    //I have kept no request param as filtering will take place using the user's firebase id entirely in backend service layer
    @GetMapping("/filtered")
    public ResponseEntity<ArrayList<Profile>> getFilteredProfiles(){
        return new ResponseEntity<>(profileManagerService.getFilteredProfiles(), HttpStatus.OK);
    }


}
