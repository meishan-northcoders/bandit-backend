package com.northcoders.bandit.controller;

import com.northcoders.bandit.model.CorrespondentDTO;
import com.northcoders.bandit.model.Message;
import com.northcoders.bandit.model.MessageDTO;
import com.northcoders.bandit.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping
    public ResponseEntity<Message> saveMessage(@RequestBody MessageDTO messageDTO) {
        try {
            Message savedMessage = messageService.saveMessage(messageDTO);
            return new ResponseEntity<>(savedMessage, HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping
    public ResponseEntity<List<Message>> getMessagesBetweenUsers(@RequestBody CorrespondentDTO correspondentDTO) {
        try {
            List<Message> messagesBetweenUsers = messageService.getAllMessagesBetweenUsers(correspondentDTO);
            return new ResponseEntity<>(messagesBetweenUsers, HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
    }

}
