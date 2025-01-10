package com.northcoders.bandit.controller;

import com.northcoders.bandit.model.Profile;
import com.northcoders.bandit.service.ProfileManagerService;
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

    @DeleteMapping
    public ResponseEntity<Profile> deleteProfile(@RequestParam(value = "id") long id){
        Profile profile = profileManagerService.deleteById(id);
        if(profile!= null){
            return new ResponseEntity<>(profile, HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }


}
