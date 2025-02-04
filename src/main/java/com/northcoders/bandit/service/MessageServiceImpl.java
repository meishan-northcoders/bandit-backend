package com.northcoders.bandit.service;

import com.northcoders.bandit.exception.InvalidDTOException;
import com.northcoders.bandit.model.*;
import com.northcoders.bandit.repository.MessageRepository;
import com.northcoders.bandit.repository.ProfileManagerRepository;
import jakarta.persistence.EntityExistsException;
import org.apache.catalina.User;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ProfileManagerRepository profileManagerRepository;
    @Autowired
    private UserInContextService userInContextService;



    @Override
    public MessageResponseDTO saveMessage(MessageRequestDTO messageRequestDTO) {
        //check if null or null attributes
        if (messageRequestDTO == null || messageRequestDTO.getReceiverId() == null || messageRequestDTO.getMessageBody() == null) {
            throw new InvalidDTOException("MessageRequestDTO cannot be null or have null attributes");
        }

        //TODO throw exception if not mutual favourites

        //Convert MessageRequestDTO to Message
        String senderId = getActiveUserId();
        String receiverId = messageRequestDTO.getReceiverId();
        String messageBody = messageRequestDTO.getMessageBody();
        Instant createdAt = Instant.now();

        Message message = new Message(null, senderId, receiverId, messageBody, createdAt);

        Message savedMessage = messageRepository.save(message);

        //convert Message to MessageResponseDTO
        senderId = savedMessage.getSenderId();
        receiverId = savedMessage.getReceiverId();
        messageBody = savedMessage.getMessageBody();
        createdAt = savedMessage.getCreatedAt();

        MessageResponseDTO messageResponseDTO = new MessageResponseDTO(senderId, receiverId , messageBody, createdAt);

        return messageResponseDTO;
    }

    @Override
    public List<MessageResponseDTO> getAllMessagesBetweenUsers(CorrespondentRequestDTO correspondentRequestDTO) {
        //Check if null or null attributes
        if (correspondentRequestDTO == null || correspondentRequestDTO.getCorrespondentId() == null) {
            throw new InvalidDTOException("CorrespondentRequestDTO cannot be null or have null attributes");
        }

        //TODO throw exception if not mutual favourites

        String correspondentId = correspondentRequestDTO.getCorrespondentId();
        String activeUserId = getActiveUserId();

        List<String> senderIds = List.of(activeUserId, correspondentId);
        List<String> receiverIds = List.of(activeUserId, correspondentId);

        List<Message> messages = messageRepository.findAllBySenderIdInAndReceiverIdInOrderByCreatedAtDesc(senderIds, receiverIds);

        //convert List<Message> into List<MessageResponseDTO
        List<MessageResponseDTO> messageResponseDTOList = messages.stream()
                .map(m-> (new MessageResponseDTO(m.getSenderId(), m.getReceiverId(), m.getMessageBody(), m.getCreatedAt())))
                .toList();

        return messageResponseDTOList;
    }

    private String getActiveUserId() {
        String firebaseId = userInContextService.getcurrentUser().getUserId();
        Profile currentUserProfile = profileManagerRepository.findByfirebaseId(firebaseId).orElse(null);
        if (currentUserProfile == null) {
            throw new EntityExistsException("No profile found with provided Firebase ID");
        } else {
            return currentUserProfile.getProfile_id();
        }
    }

}
