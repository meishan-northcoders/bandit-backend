package com.northcoders.bandit.service;

import com.northcoders.bandit.model.CorrespondentRequestDTO;
import com.northcoders.bandit.model.Message;
import com.northcoders.bandit.model.MessageRequestDTO;
import com.northcoders.bandit.model.MessageResponseDTO;

import java.util.List;

public interface MessageService {

    MessageResponseDTO saveMessage(MessageRequestDTO messageRequestDTO);

    List<MessageResponseDTO> getAllMessagesBetweenUsers (CorrespondentRequestDTO correspondentRequestDTO);
}
