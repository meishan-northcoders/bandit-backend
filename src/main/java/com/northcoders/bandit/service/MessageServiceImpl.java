package com.northcoders.bandit.service;

import com.northcoders.bandit.model.CorrespondentDTO;
import com.northcoders.bandit.model.Message;
import com.northcoders.bandit.model.MessageDTO;
import com.northcoders.bandit.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;



    @Override
    public Message saveMessage(MessageDTO messageDTO) {
        //check if null or null attributes
        if (messageDTO == null || messageDTO.getReceiverId() == null || messageDTO.getMessageBody() == null) {
            throw new NullPointerException("MessageDTO cannot be null or have null attributes");
        }

        //TODO throw exception if not mutual favourites

        //Convert MessageDTO to Message
        String senderId = getActiveUserId();
        String receiverId = messageDTO.getReceiverId();
        String messageBody = messageDTO.getMessageBody();
        Instant createdDate = Instant.now();

        Message message = new Message(null, senderId,receiverId , messageBody, createdDate);

        return messageRepository.save(message);
    }

    @Override
    public List<Message> getAllMessagesBetweenUsers(CorrespondentDTO correspondentDTO) {
        //Check if null or null attributes
        if (correspondentDTO == null || correspondentDTO.getCorrespondentId() == null) {
            throw new NullPointerException("CorrespondentDTO cannot be null or have null attributes");
        }

        //TODO throw exception if not mutual favourites

        String correspondendId = correspondentDTO.getCorrespondentId();
        String activeUserId = getActiveUserId();

        List<Message> messagesActToCor = messageRepository.findBySenderIdAndReceiverId(activeUserId, correspondendId);
        List<Message> messagesCorToAct = messageRepository.findBySenderIdAndReceiverId(correspondendId, activeUserId);

        List<Message> combinedMessages = new ArrayList<>();
        combinedMessages.addAll(messagesActToCor);
        combinedMessages.addAll(messagesCorToAct);

        return combinedMessages;
    }

    private String getActiveUserId() {
        return "activeUserId1";
    }

}
