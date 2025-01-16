package com.northcoders.bandit.controller;

import com.northcoders.bandit.model.FireBaseUser;
import com.northcoders.bandit.service.UserInContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/p1")
public class GreetingController {
    @Autowired
    UserInContextService userInContextService;
    @GetMapping("/greeting")
    public ResponseEntity<?> getGreetings(@RequestHeader("Authorization") String authHeader) {
        FireBaseUser user = userInContextService.getcurrentUser();
        if(user != null)
        {
            return new ResponseEntity<>(user,HttpStatus.OK);
        }
        return new ResponseEntity<>( "Hello World",HttpStatus.OK);
    }
}
