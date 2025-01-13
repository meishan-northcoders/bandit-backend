package com.northcoders.bandit.controller;

import com.northcoders.bandit.model.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1")
public class GreetingController {
    @GetMapping("greeting")
    public ResponseEntity<?> getGreetings(@RequestHeader("Authorization") String authHeader) {
        System.out.println(authHeader);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Profile principal = (Profile) authentication.getPrincipal();
        if(principal != null){
            return new ResponseEntity<>( principal.getUserName(),HttpStatus.OK);
        }
        return new ResponseEntity<>( "Hello World",HttpStatus.OK);
    }


}
