package com.northcoders.bandit.controller;

import com.northcoders.bandit.model.CorrespondentRequestDTO;
import com.northcoders.bandit.model.Message;
import com.northcoders.bandit.model.MessageRequestDTO;
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
    public ResponseEntity<Message> saveMessage(@RequestBody MessageRequestDTO messageRequestDTO) {
        try {
            Message savedMessage = messageService.saveMessage(messageRequestDTO);
            return new ResponseEntity<>(savedMessage, HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping
    public ResponseEntity<List<Message>> getMessagesBetweenUsers(@RequestBody CorrespondentRequestDTO correspondentRequestDTO) {
        try {
            List<Message> messagesBetweenUsers = messageService.getAllMessagesBetweenUsers(correspondentRequestDTO);
            return new ResponseEntity<>(messagesBetweenUsers, HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
    }

}
