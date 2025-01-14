package com.northcoders.bandit.service;

import com.northcoders.bandit.model.CorrespondentRequestDTO;
import com.northcoders.bandit.model.Message;
import com.northcoders.bandit.model.MessageRequestDTO;

import java.util.List;

public interface MessageService {

    Message saveMessage(MessageRequestDTO messageRequestDTO);

    List<Message> getAllMessagesBetweenUsers (CorrespondentRequestDTO correspondentRequestDTO);
}
