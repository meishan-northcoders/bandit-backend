package com.northcoders.bandit.service;

import com.northcoders.bandit.model.CorrespondentDTO;
import com.northcoders.bandit.model.Message;
import com.northcoders.bandit.model.MessageDTO;

import java.util.List;

public interface MessageService {

    Message saveMessage(MessageDTO messageDTO);

    List<Message> getAllMessagesBetweenUsers (CorrespondentDTO correspondentDTO);
}
