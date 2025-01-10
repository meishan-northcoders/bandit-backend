package com.northcoders.bandit.service;

import com.northcoders.bandit.model.CorrespondentDTO;
import com.northcoders.bandit.model.Message;
import com.northcoders.bandit.model.MessageDTO;
import com.northcoders.bandit.repository.MessageRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

    private MessageDTO messageDTO1 = new MessageDTO("receiverId1", "valid message");
    private MessageDTO messageDTO1NullSenderId = new MessageDTO(null, "valid message");
    private MessageDTO messageDTO1NullMessageBody = new MessageDTO("receiverId1", null);
    private MessageDTO messageDTO1AllNull = new MessageDTO(null, null);

    private Message message1 = new Message(1L, "senderId1", "receiverId1", "valid message", instant1);
    private Message message1NullId = new Message(null, "senderId1", "receiverId1", "valid message", instant1);

    private String activeUser1 = "activeUserId1";
    private CorrespondentDTO correspondentDTO1 = new CorrespondentDTO("correspondentId1");
    private CorrespondentDTO correspondentDTO1NullCorrespondentId = new CorrespondentDTO(null);


    private Message messageActToCor1 = new Message(1L, "activeUserId1", "correspondentId1", "valid message", instant1);
    private Message messageActToCor2 = new Message(2L, "activeUserId1", "correspondentId1", "valid message", instant2);
    private Message messageActToCor3 = new Message(3L, "activeUserId1", "correspondentId1", "valid message", instant3);

    private List<Message> messageActToCorListOneItem = new ArrayList<>(
            List.of(messageActToCor1));
    private List<Message> messageActToCorListMultipleItems = new ArrayList<>(
            List.of(messageActToCor1, messageActToCor2, messageActToCor3));

    private Message messageCorToAct1 = new Message(4L, "correspondentId1", "activeUserId1", "valid message", instant4);
    private Message messageCorToAct2 = new Message(5L, "correspondentId1", "activeUserId1", "valid message", instant5);
    private Message messageCorToAct3 = new Message(6L, "correspondentId1", "activeUserId1", "valid message", instant6);

    private List<Message> messageCorToActListOneItem = new ArrayList<>(
            List.of(messageCorToAct1));
    private List<Message> messageCorToActListMultipleItems = new ArrayList<>(
            List.of(messageCorToAct1, messageCorToAct2, messageCorToAct3));

    @Test
    @DisplayName("saveMessage returns saved message when passed valid messageDTO")
    void saveMessageWhenValid() {
        //Arrange
        when(mockMessageRepository.save(message1NullId)).thenReturn(message1);
        //TODO mock when: mutual favourites repository check is made, returns acceptable

        //Act
        Message savedMessage = messageServiceImpl.saveMessage(messageDTO1);

        //Assert
        assertAll(
                () -> assertEquals(message1.getId(), savedMessage.getId()),
                () -> assertEquals(message1.getSenderId(), savedMessage.getSenderId()),
                () -> assertEquals(message1.getReceiverId(), savedMessage.getReceiverId()),
                () -> assertEquals(message1.getMessageBody(), savedMessage.getMessageBody()),
                () -> assertEquals(message1.getCreatedDate(), savedMessage.getCreatedDate())
        );
    }

    @Test
    @DisplayName("saveMessage returns exception when passed null MessageDTO or MessageDTO with null fields")
    void saveMessageWhenNull() {
        //Arrange

        //Act & Assert
        assertAll(
                () -> assertThrows(NullPointerException.class, () -> messageServiceImpl.saveMessage(null)),
                () -> assertThrows(NullPointerException.class, () -> messageServiceImpl.saveMessage(messageDTO1NullSenderId)),
                () -> assertThrows(NullPointerException.class, () -> messageServiceImpl.saveMessage(messageDTO1NullMessageBody)),
                () -> assertThrows(NullPointerException.class, () -> messageServiceImpl.saveMessage(messageDTO1AllNull))
        );
    }

    @Test
    @DisplayName("saveMessage returns exception when passed messageDTO when users are not mutual favourites")
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
        when(mockMessageRepository.findBySenderIdAndReceiverId(activeUser1, correspondentDTO1.getCorrespondentId()))
                .thenReturn(messageActToCorListOneItem);
        when(mockMessageRepository.findBySenderIdAndReceiverId(correspondentDTO1.getCorrespondentId(), activeUser1))
                .thenReturn(messageCorToActListOneItem);
        //TODO mock when method to get active user is called, return activeUser1
        //TODO mock when: mutual favourites repository check is made, returns acceptable

        //Act
        List<Message> actualMessageList = messageServiceImpl.getAllMessagesBetweenUsers(correspondentDTO1);

        //Assert
        assertAll(
                () -> assertEquals(2, actualMessageList.size()),

                () -> assertEquals(messageActToCorListOneItem.get(0).getId(), actualMessageList.get(0).getId()),
                () -> assertEquals(messageActToCorListOneItem.get(0).getSenderId(), actualMessageList.get(0).getSenderId()),
                () -> assertEquals(messageActToCorListOneItem.get(0).getReceiverId(), actualMessageList.get(0).getReceiverId()),
                () -> assertEquals(messageActToCorListOneItem.get(0).getMessageBody(), actualMessageList.get(0).getMessageBody()),
                () -> assertEquals(messageActToCorListOneItem.get(0).getCreatedDate(), actualMessageList.get(0).getCreatedDate()),

                () -> assertEquals(messageCorToActListOneItem.get(0).getId(), actualMessageList.get(1).getId()),
                () -> assertEquals(messageCorToActListOneItem.get(0).getSenderId(), actualMessageList.get(1).getSenderId()),
                () -> assertEquals(messageCorToActListOneItem.get(0).getReceiverId(), actualMessageList.get(1).getReceiverId()),
                () -> assertEquals(messageCorToActListOneItem.get(0).getMessageBody(), actualMessageList.get(1).getMessageBody()),
                () -> assertEquals(messageCorToActListOneItem.get(0).getCreatedDate(), actualMessageList.get(1).getCreatedDate())
        );
    }

    @Test
    @DisplayName("getAllMessagesBetweenUsers returns list of all messages when passed valid Correspondent and multiple mutual message each way exist in database")
    void getAllMessagesBetweenUsersWhenValidMultipleMessages() {
        //Arrange
        when(mockMessageRepository.findBySenderIdAndReceiverId(activeUser1, correspondentDTO1.getCorrespondentId()))
                .thenReturn(messageActToCorListMultipleItems);
        when(mockMessageRepository.findBySenderIdAndReceiverId(correspondentDTO1.getCorrespondentId(), activeUser1))
                .thenReturn(messageCorToActListMultipleItems);
        //TODO mock when method to get active user is called, return activeUser1
        //TODO mock when: mutual favourites repository check is made, returns acceptable

        //Act
        List<Message> actualMessageList = messageServiceImpl.getAllMessagesBetweenUsers(correspondentDTO1);

        //Assert
        assertAll(
                () -> assertEquals(6, actualMessageList.size()),

                () -> assertEquals(messageActToCorListMultipleItems.get(0).getId(), actualMessageList.get(0).getId()),
                () -> assertEquals(messageActToCorListMultipleItems.get(0).getSenderId(), actualMessageList.get(0).getSenderId()),
                () -> assertEquals(messageActToCorListMultipleItems.get(0).getReceiverId(), actualMessageList.get(0).getReceiverId()),
                () -> assertEquals(messageActToCorListMultipleItems.get(0).getMessageBody(), actualMessageList.get(0).getMessageBody()),
                () -> assertEquals(messageActToCorListMultipleItems.get(0).getCreatedDate(), actualMessageList.get(0).getCreatedDate()),

                () -> assertEquals(messageActToCorListMultipleItems.get(1).getId(), actualMessageList.get(1).getId()),
                () -> assertEquals(messageActToCorListMultipleItems.get(1).getSenderId(), actualMessageList.get(1).getSenderId()),
                () -> assertEquals(messageActToCorListMultipleItems.get(1).getReceiverId(), actualMessageList.get(1).getReceiverId()),
                () -> assertEquals(messageActToCorListMultipleItems.get(1).getMessageBody(), actualMessageList.get(1).getMessageBody()),
                () -> assertEquals(messageActToCorListMultipleItems.get(1).getCreatedDate(), actualMessageList.get(1).getCreatedDate()),

                () -> assertEquals(messageActToCorListMultipleItems.get(2).getId(), actualMessageList.get(2).getId()),
                () -> assertEquals(messageActToCorListMultipleItems.get(2).getSenderId(), actualMessageList.get(2).getSenderId()),
                () -> assertEquals(messageActToCorListMultipleItems.get(2).getReceiverId(), actualMessageList.get(2).getReceiverId()),
                () -> assertEquals(messageActToCorListMultipleItems.get(2).getMessageBody(), actualMessageList.get(2).getMessageBody()),
                () -> assertEquals(messageActToCorListMultipleItems.get(2).getCreatedDate(), actualMessageList.get(2).getCreatedDate()),

                () -> assertEquals(messageCorToActListMultipleItems.get(0).getId(), actualMessageList.get(3).getId()),
                () -> assertEquals(messageCorToActListMultipleItems.get(0).getSenderId(), actualMessageList.get(3).getSenderId()),
                () -> assertEquals(messageCorToActListMultipleItems.get(0).getReceiverId(), actualMessageList.get(3).getReceiverId()),
                () -> assertEquals(messageCorToActListMultipleItems.get(0).getMessageBody(), actualMessageList.get(3).getMessageBody()),
                () -> assertEquals(messageCorToActListMultipleItems.get(0).getCreatedDate(), actualMessageList.get(3).getCreatedDate()),

                () -> assertEquals(messageCorToActListMultipleItems.get(1).getId(), actualMessageList.get(4).getId()),
                () -> assertEquals(messageCorToActListMultipleItems.get(1).getSenderId(), actualMessageList.get(4).getSenderId()),
                () -> assertEquals(messageCorToActListMultipleItems.get(1).getReceiverId(), actualMessageList.get(4).getReceiverId()),
                () -> assertEquals(messageCorToActListMultipleItems.get(1).getMessageBody(), actualMessageList.get(4).getMessageBody()),
                () -> assertEquals(messageCorToActListMultipleItems.get(1).getCreatedDate(), actualMessageList.get(4).getCreatedDate()),

                () -> assertEquals(messageCorToActListMultipleItems.get(2).getId(), actualMessageList.get(5).getId()),
                () -> assertEquals(messageCorToActListMultipleItems.get(2).getSenderId(), actualMessageList.get(5).getSenderId()),
                () -> assertEquals(messageCorToActListMultipleItems.get(2).getReceiverId(), actualMessageList.get(5).getReceiverId()),
                () -> assertEquals(messageCorToActListMultipleItems.get(2).getMessageBody(), actualMessageList.get(5).getMessageBody()),
                () -> assertEquals(messageCorToActListMultipleItems.get(2).getCreatedDate(), actualMessageList.get(5).getCreatedDate())
        );
    }

    @Test
    @DisplayName("getAllMessagesBetweenUsers throws exception when passed null CorrespondentDTO or CorrespondentDTO with null fields")
    void getAllMessagesBetweenUsersWhenNull() {
        //Arrange

        //Act & Assert
        assertAll(
                () -> assertThrows(NullPointerException.class, () -> messageServiceImpl.getAllMessagesBetweenUsers(null)),
                () -> assertThrows(NullPointerException.class, () -> messageServiceImpl.getAllMessagesBetweenUsers(correspondentDTO1NullCorrespondentId))
        );
    }

    @Test
    @DisplayName("getAllMessagesBetweenUsers throws exception when passed correspondentDTO when users are not mutual favourites")
    void getAllMessagesBetweenUsersWhenNotMutualFavourites() {
        //Arrange
        //TODO need to mock a query to favourites table for sender and receiver but this has not been built yet

        //Act & Assert
        //TODO assert exception when serviceImpl save method is used when favourites repository returns not mutually favourited
    }
}