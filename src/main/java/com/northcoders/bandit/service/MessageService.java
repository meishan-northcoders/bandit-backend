package com.northcoders.bandit.service;

import com.northcoders.bandit.model.Message;
import com.northcoders.bandit.model.MessageDTO;

import java.util.List;

public interface MessageService {

    Message saveMessage(MessageDTO message);

    List<Message> getAllMessagesBetweenUsers (String activeUserId, String correspondentId);
}
