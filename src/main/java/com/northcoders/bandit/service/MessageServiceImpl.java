package com.northcoders.bandit.service;

import com.northcoders.bandit.model.Message;
import com.northcoders.bandit.model.MessageDTO;
import com.northcoders.bandit.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    MessageRepository messageRepository;

    @Override
    public Message saveMessage(MessageDTO message) {
        return null;
    }

    @Override
    public List<Message> getAllMessagesBetweenUsers(Long userId1, Long userId2) {
        return List.of();
    }
}
