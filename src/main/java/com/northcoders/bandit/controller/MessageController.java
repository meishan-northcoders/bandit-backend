package com.northcoders.bandit.controller;

import com.northcoders.bandit.model.CorrespondentRequestDTO;
import com.northcoders.bandit.model.Message;
import com.northcoders.bandit.model.MessageRequestDTO;
import com.northcoders.bandit.model.MessageResponseDTO;
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
    public ResponseEntity<MessageResponseDTO> saveMessage(@RequestHeader("Authorization") String authHeader, @RequestBody MessageRequestDTO messageRequestDTO) {

            MessageResponseDTO savedMessageResponseDTO = messageService.saveMessage(messageRequestDTO);
            return new ResponseEntity<>(savedMessageResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity<List<MessageResponseDTO>> getMessagesBetweenUsers(@RequestHeader("Authorization") String authHeader, @RequestBody CorrespondentRequestDTO correspondentRequestDTO) {
            List<MessageResponseDTO> messageResponseDTOsBetweenUsers = messageService.getAllMessagesBetweenUsers(correspondentRequestDTO);
            return new ResponseEntity<>(messageResponseDTOsBetweenUsers, HttpStatus.OK);
    }

}
