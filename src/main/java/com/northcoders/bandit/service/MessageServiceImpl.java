package com.northcoders.bandit.service;

import com.northcoders.bandit.model.CorrespondentDTO;
import com.northcoders.bandit.model.Message;
import com.northcoders.bandit.model.MessageDTO;
import com.northcoders.bandit.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
        Instant createdAt = Instant.now();

        Message message = new Message(null, senderId,receiverId , messageBody, createdAt);

        return messageRepository.save(message);
    }

    @Override
    public List<Message> getAllMessagesBetweenUsers(CorrespondentDTO correspondentDTO) {
        //Check if null or null attributes
        if (correspondentDTO == null || correspondentDTO.getCorrespondentId() == null) {
            throw new NullPointerException("CorrespondentDTO cannot be null or have null attributes");
        }

        //TODO throw exception if not mutual favourites

        String correspondentId = correspondentDTO.getCorrespondentId();
        String activeUserId = getActiveUserId();

        List<String> senderIds = List.of(activeUserId, correspondentId);
        List<String> receiverIds = List.of(activeUserId, correspondentId);

        return messageRepository.findAllBySenderIdInAndReceiverIdInOrderByCreatedAtDesc(senderIds, receiverIds);
    }

    private String getActiveUserId() {
        return "activeUserId1";
    }

}
