package com.northcoders.bandit.service;

import com.northcoders.bandit.model.CorrespondentRequestDTO;
import com.northcoders.bandit.model.Message;
import com.northcoders.bandit.model.MessageRequestDTO;
import com.northcoders.bandit.repository.MessageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
class MessageServiceImplTest {

    @Mock
    private MessageRepository mockMessageRepository;

    @InjectMocks
    private MessageServiceImpl messageServiceImpl;

    private Instant instant1 = Instant.now();
    private Instant instant2 = Instant.now();
    private Instant instant3 = Instant.now();
    private Instant instant4 = Instant.now();
    private Instant instant5 = Instant.now();
    private Instant instant6 = Instant.now();

    private MessageRequestDTO messageRequestDTO1 = new MessageRequestDTO("receiverId1", "valid message");
    private MessageRequestDTO messageRequestDTO1NullSenderId = new MessageRequestDTO(null, "valid message");
    private MessageRequestDTO messageRequestDTO1NullMessageBody = new MessageRequestDTO("receiverId1", null);
    private MessageRequestDTO messageRequestDTO1AllNull = new MessageRequestDTO(null, null);

    private Message message1 = new Message(1L, "senderId1", "receiverId1", "valid message", instant1);

    private String activeUser1 = "activeUserId1";
    private CorrespondentRequestDTO correspondentRequestDTO1 = new CorrespondentRequestDTO("correspondentId1");
    private CorrespondentRequestDTO correspondentRequestDTO1NullCorrespondentId = new CorrespondentRequestDTO(null);

    private List<String> senderIds = List.of(activeUser1, correspondentRequestDTO1.getCorrespondentId());
    private List<String> receiverIds = List.of(activeUser1, correspondentRequestDTO1.getCorrespondentId());

    private Message messageActToCor1 = new Message(1L, "activeUserId1", "correspondentId1", "valid message", instant1);
    private Message messageActToCor2 = new Message(2L, "activeUserId1", "correspondentId1", "valid message", instant2);
    private Message messageActToCor3 = new Message(3L, "activeUserId1", "correspondentId1", "valid message", instant3);

    private Message messageCorToAct1 = new Message(4L, "correspondentId1", "activeUserId1", "valid message", instant4);
    private Message messageCorToAct2 = new Message(5L, "correspondentId1", "activeUserId1", "valid message", instant5);
    private Message messageCorToAct3 = new Message(6L, "correspondentId1", "activeUserId1", "valid message", instant6);

    private List<Message> messagesOneEachWay = new ArrayList<>(
            List.of(messageActToCor1, messageCorToAct1));
    private List<Message> messagesMultipleEachWay = new ArrayList<>(
            List.of(messageActToCor1, messageActToCor2, messageActToCor3, messageCorToAct1, messageCorToAct2, messageCorToAct3));

    @Test
    @DisplayName("saveMessage returns saved message when passed valid messageRequestDTO")
    void saveMessageWhenValid() {
        //Arrange
        when(mockMessageRepository.save(Mockito.any(Message.class))).thenReturn(message1);

        //TODO mock when: mutual favourites repository check is made, returns acceptable

        //Act
        Message savedMessage = messageServiceImpl.saveMessage(messageRequestDTO1);

        //Assert
        assertAll(
                () -> assertEquals(message1.getId(), savedMessage.getId()),
                () -> assertEquals(message1.getSenderId(), savedMessage.getSenderId()),
                () -> assertEquals(message1.getReceiverId(), savedMessage.getReceiverId()),
                () -> assertEquals(message1.getMessageBody(), savedMessage.getMessageBody()),
                () -> assertEquals(message1.getCreatedAt(), savedMessage.getCreatedAt())
        );
    }

    @Test
    @DisplayName("saveMessage returns exception when passed null MessageRequestDTO or MessageRequestDTO with null fields")
    void saveMessageWhenNull() {
        //Arrange

        //Act & Assert
        assertAll(
                () -> assertThrows(NullPointerException.class, () -> messageServiceImpl.saveMessage(null)),
                () -> assertThrows(NullPointerException.class, () -> messageServiceImpl.saveMessage(messageRequestDTO1NullSenderId)),
                () -> assertThrows(NullPointerException.class, () -> messageServiceImpl.saveMessage(messageRequestDTO1NullMessageBody)),
                () -> assertThrows(NullPointerException.class, () -> messageServiceImpl.saveMessage(messageRequestDTO1AllNull))
        );
    }

    @Test
    @DisplayName("saveMessage returns exception when passed messageRequestDTO when users are not mutual favourites")
    void saveMessageWhenNotMutualFavourites() {
        //Arrange
        //TODO need to mock a query to favourites table for sender and receiver but this has not been built yet

        //Act & Assert
        //TODO assert exception when serviceImpl save method is used when favourites repository returns not mutually favourited
    }

    @Test
    @DisplayName("getAllMessagesBetweenUsers returns list of all messages when passed valid Correspondent and one mutual message each way exists in database")
    void getAllMessagesBetweenUsersWhenValidOneMessage() {
        //Arrange
        when(mockMessageRepository.findAllBySenderIdInAndReceiverIdInOrderByCreatedAtDesc(senderIds, receiverIds))
                .thenReturn(messagesOneEachWay);

        //TODO mock when method to get active user is called, return activeUser1
        //TODO mock when: mutual favourites repository check is made, returns acceptable

        //Act
        List<Message> actualMessageList = messageServiceImpl.getAllMessagesBetweenUsers(correspondentRequestDTO1);

        //Assert
        assertAll(
                () -> assertEquals(2, actualMessageList.size()),

                () -> assertEquals(messagesOneEachWay.get(0).getId(), actualMessageList.get(0).getId()),
                () -> assertEquals(messagesOneEachWay.get(0).getSenderId(), actualMessageList.get(0).getSenderId()),
                () -> assertEquals(messagesOneEachWay.get(0).getReceiverId(), actualMessageList.get(0).getReceiverId()),
                () -> assertEquals(messagesOneEachWay.get(0).getMessageBody(), actualMessageList.get(0).getMessageBody()),
                () -> assertEquals(messagesOneEachWay.get(0).getCreatedAt(), actualMessageList.get(0).getCreatedAt()),

                () -> assertEquals(messagesOneEachWay.get(1).getId(), actualMessageList.get(1).getId()),
                () -> assertEquals(messagesOneEachWay.get(1).getSenderId(), actualMessageList.get(1).getSenderId()),
                () -> assertEquals(messagesOneEachWay.get(1).getReceiverId(), actualMessageList.get(1).getReceiverId()),
                () -> assertEquals(messagesOneEachWay.get(1).getMessageBody(), actualMessageList.get(1).getMessageBody()),
                () -> assertEquals(messagesOneEachWay.get(1).getCreatedAt(), actualMessageList.get(1).getCreatedAt())
        );
    }

    @Test
    @DisplayName("getAllMessagesBetweenUsers returns list of all messages when passed valid Correspondent and multiple mutual message each way exist in database")
    void getAllMessagesBetweenUsersWhenValidMultipleMessages() {
        //Arrange
        when(mockMessageRepository.findAllBySenderIdInAndReceiverIdInOrderByCreatedAtDesc(senderIds, receiverIds))
                .thenReturn(messagesMultipleEachWay);

        //TODO mock when method to get active user is called, return activeUser1
        //TODO mock when: mutual favourites repository check is made, returns acceptable

        //Act
        List<Message> actualMessageList = messageServiceImpl.getAllMessagesBetweenUsers(correspondentRequestDTO1);

        //Assert
        assertAll(
                () -> assertEquals(6, actualMessageList.size()),

                () -> assertEquals(messagesMultipleEachWay.get(0).getId(), actualMessageList.get(0).getId()),
                () -> assertEquals(messagesMultipleEachWay.get(0).getSenderId(), actualMessageList.get(0).getSenderId()),
                () -> assertEquals(messagesMultipleEachWay.get(0).getReceiverId(), actualMessageList.get(0).getReceiverId()),
                () -> assertEquals(messagesMultipleEachWay.get(0).getMessageBody(), actualMessageList.get(0).getMessageBody()),
                () -> assertEquals(messagesMultipleEachWay.get(0).getCreatedAt(), actualMessageList.get(0).getCreatedAt()),

                () -> assertEquals(messagesMultipleEachWay.get(1).getId(), actualMessageList.get(1).getId()),
                () -> assertEquals(messagesMultipleEachWay.get(1).getSenderId(), actualMessageList.get(1).getSenderId()),
                () -> assertEquals(messagesMultipleEachWay.get(1).getReceiverId(), actualMessageList.get(1).getReceiverId()),
                () -> assertEquals(messagesMultipleEachWay.get(1).getMessageBody(), actualMessageList.get(1).getMessageBody()),
                () -> assertEquals(messagesMultipleEachWay.get(1).getCreatedAt(), actualMessageList.get(1).getCreatedAt()),

                () -> assertEquals(messagesMultipleEachWay.get(2).getId(), actualMessageList.get(2).getId()),
                () -> assertEquals(messagesMultipleEachWay.get(2).getSenderId(), actualMessageList.get(2).getSenderId()),
                () -> assertEquals(messagesMultipleEachWay.get(2).getReceiverId(), actualMessageList.get(2).getReceiverId()),
                () -> assertEquals(messagesMultipleEachWay.get(2).getMessageBody(), actualMessageList.get(2).getMessageBody()),
                () -> assertEquals(messagesMultipleEachWay.get(2).getCreatedAt(), actualMessageList.get(2).getCreatedAt()),

                () -> assertEquals(messagesMultipleEachWay.get(3).getId(), actualMessageList.get(3).getId()),
                () -> assertEquals(messagesMultipleEachWay.get(3).getSenderId(), actualMessageList.get(3).getSenderId()),
                () -> assertEquals(messagesMultipleEachWay.get(3).getReceiverId(), actualMessageList.get(3).getReceiverId()),
                () -> assertEquals(messagesMultipleEachWay.get(3).getMessageBody(), actualMessageList.get(3).getMessageBody()),
                () -> assertEquals(messagesMultipleEachWay.get(3).getCreatedAt(), actualMessageList.get(3).getCreatedAt()),

                () -> assertEquals(messagesMultipleEachWay.get(4).getId(), actualMessageList.get(4).getId()),
                () -> assertEquals(messagesMultipleEachWay.get(4).getSenderId(), actualMessageList.get(4).getSenderId()),
                () -> assertEquals(messagesMultipleEachWay.get(4).getReceiverId(), actualMessageList.get(4).getReceiverId()),
                () -> assertEquals(messagesMultipleEachWay.get(4).getMessageBody(), actualMessageList.get(4).getMessageBody()),
                () -> assertEquals(messagesMultipleEachWay.get(4).getCreatedAt(), actualMessageList.get(4).getCreatedAt()),

                () -> assertEquals(messagesMultipleEachWay.get(5).getId(), actualMessageList.get(5).getId()),
                () -> assertEquals(messagesMultipleEachWay.get(5).getSenderId(), actualMessageList.get(5).getSenderId()),
                () -> assertEquals(messagesMultipleEachWay.get(5).getReceiverId(), actualMessageList.get(5).getReceiverId()),
                () -> assertEquals(messagesMultipleEachWay.get(5).getMessageBody(), actualMessageList.get(5).getMessageBody()),
                () -> assertEquals(messagesMultipleEachWay.get(5).getCreatedAt(), actualMessageList.get(5).getCreatedAt())
        );
    }

    @Test
    @DisplayName("getAllMessagesBetweenUsers throws exception when passed null CorrespondentRequestDTO or CorrespondentRequestDTO with null fields")
    void getAllMessagesBetweenUsersWhenNull() {
        //Arrange

        //Act & Assert
        assertAll(
                () -> assertThrows(NullPointerException.class, () -> messageServiceImpl.getAllMessagesBetweenUsers(null)),
                () -> assertThrows(NullPointerException.class, () -> messageServiceImpl.getAllMessagesBetweenUsers(correspondentRequestDTO1NullCorrespondentId))
        );
    }

    @Test
    @DisplayName("getAllMessagesBetweenUsers throws exception when passed correspondenRequestDTO when users are not mutual favourites")
    void getAllMessagesBetweenUsersWhenNotMutualFavourites() {
        //Arrange
        //TODO need to mock a query to favourites table for sender and receiver but this has not been built yet

        //Act & Assert
        //TODO assert exception when serviceImpl save method is used when favourites repository returns not mutually favourited
    }
}