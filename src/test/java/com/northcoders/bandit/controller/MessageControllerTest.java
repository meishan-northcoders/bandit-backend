package com.northcoders.bandit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.northcoders.bandit.model.CorrespondentDTO;
import com.northcoders.bandit.model.Message;
import com.northcoders.bandit.model.MessageDTO;
import com.northcoders.bandit.service.MessageService;
import com.northcoders.bandit.service.MessageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class MessageControllerTest {

    @Mock
    private MessageServiceImpl mockMessageService;

    @InjectMocks
    private MessageController messageController;

    @Autowired
    MockMvc mockMvcController;

    private ObjectMapper mapper;

    private String messagesEndpointURI = "/api/v1/messages";

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

    private String activeUser1 = "activeUserId1";

    private CorrespondentDTO correspondentDTO1 = new CorrespondentDTO("correspondentId1");
    private CorrespondentDTO correspondentDTO1NullCorrespondentId = new CorrespondentDTO(null);

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


    @BeforeEach
    public void setup() {
        mockMvcController = MockMvcBuilders.standaloneSetup(messageController).build();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("saveMessage returns http 200 and saved message when passed valid message")
    public void testSaveMessageWhenValid() throws Exception {
        //Arrange
        when(this.mockMessageService.saveMessage(Mockito.any(MessageDTO.class))).thenReturn(message1);

        //Act & Assert
        mockMvcController.perform(
                        post(messagesEndpointURI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(messageDTO1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(message1.getId()))
                .andExpect(jsonPath("$.senderId").value(message1.getSenderId()))
                .andExpect(jsonPath("$.receiverId").value(messageDTO1.getReceiverId()))
                .andExpect(jsonPath("$.messageBody").value(messageDTO1.getMessageBody()))
                .andExpect(jsonPath("$.createdAt").value(message1.getCreatedAt().getEpochSecond()));

        verify(mockMessageService, times(1)).saveMessage(Mockito.any(MessageDTO.class));
    }

    @Test
    @DisplayName("saveMessage returns 406 when passed message with null values")
    public void testSaveMessage() throws Exception {
        //Arrange
        when(mockMessageService.saveMessage(Mockito.any(MessageDTO.class))).thenThrow(NullPointerException.class);

        //Act & Assert
        mockMvcController.perform(
                        post(messagesEndpointURI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(messageDTO1NullSenderId)))
                .andExpect(status().isNotAcceptable());

        mockMvcController.perform(
                        post(messagesEndpointURI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(messageDTO1NullMessageBody)))
                .andExpect(status().isNotAcceptable());

        mockMvcController.perform(
                        post(messagesEndpointURI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(messageDTO1AllNull)))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @DisplayName("saveMessage returns http 403 when passed messageDTO when users are not mutual favourites")
    void saveMessageWhenNotMutualFavourites() {
        //Arrange
        //TODO need to mock messageService.getAllMessagesBetweenUsers throws exception when sender and receiver are not mutually favourited

        //Act & Assert
        //TODO return 403 when not mutually favourited
    }

    @Test
    @DisplayName("getAllMessagesBetweenUsers returns http 200 and both messages when mutual message each way exists in database")
    void getAllMessagesBetweenUsersWhenValidOneMessage() throws Exception {
        //Arrange
        when(mockMessageService.getAllMessagesBetweenUsers(Mockito.any(CorrespondentDTO.class))).thenReturn(messagesOneEachWay);

        //Act & Assert
        mockMvcController.perform(
                        get(messagesEndpointURI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(correspondentDTO1)))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$[0].id").value(messageActToCor1.getId()))
                .andExpect(jsonPath("$[0].senderId").value(messageActToCor1.getSenderId()))
                .andExpect(jsonPath("$[0].receiverId").value(messageActToCor1.getReceiverId()))
                .andExpect(jsonPath("$[0].messageBody").value(messageActToCor1.getMessageBody()))
                .andExpect(jsonPath("$[0].createdAt").value(messageActToCor1.getCreatedAt().getEpochSecond()))

                .andExpect(jsonPath("$[1].id").value(messageCorToAct1.getId()))
                .andExpect(jsonPath("$[1].senderId").value(messageCorToAct1.getSenderId()))
                .andExpect(jsonPath("$[1].receiverId").value(messageCorToAct1.getReceiverId()))
                .andExpect(jsonPath("$[1].messageBody").value(messageCorToAct1.getMessageBody()))
                .andExpect(jsonPath("$[1].createdAt").value(messageCorToAct1.getCreatedAt().getEpochSecond()));

        verify(mockMessageService, times(1)).getAllMessagesBetweenUsers(Mockito.any(CorrespondentDTO.class));
    }

    @Test
    @DisplayName("getAllMessagesBetweenUsers returns http 200 and all messages when multiple mutual message each way exists in database")
    void getAllMessagesBetweenUsersWhenValidMultipleMessages() throws Exception {
        //Arrange
        when(mockMessageService.getAllMessagesBetweenUsers(Mockito.any(CorrespondentDTO.class))).thenReturn(messagesMultipleEachWay);

        //Act & Assert
        mockMvcController.perform(
                        get(messagesEndpointURI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(correspondentDTO1)))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$[0].id").value(messageActToCor1.getId()))
                .andExpect(jsonPath("$[0].senderId").value(messageActToCor1.getSenderId()))
                .andExpect(jsonPath("$[0].receiverId").value(messageActToCor1.getReceiverId()))
                .andExpect(jsonPath("$[0].messageBody").value(messageActToCor1.getMessageBody()))
                .andExpect(jsonPath("$[0].createdAt").value(messageActToCor1.getCreatedAt().getEpochSecond()))

                .andExpect(jsonPath("$[1].id").value(messageActToCor2.getId()))
                .andExpect(jsonPath("$[1].senderId").value(messageActToCor2.getSenderId()))
                .andExpect(jsonPath("$[1].receiverId").value(messageActToCor2.getReceiverId()))
                .andExpect(jsonPath("$[1].messageBody").value(messageActToCor2.getMessageBody()))
                .andExpect(jsonPath("$[1].createdAt").value(messageActToCor2.getCreatedAt().getEpochSecond()))

                .andExpect(jsonPath("$[2].id").value(messageActToCor3.getId()))
                .andExpect(jsonPath("$[2].senderId").value(messageActToCor3.getSenderId()))
                .andExpect(jsonPath("$[2].receiverId").value(messageActToCor3.getReceiverId()))
                .andExpect(jsonPath("$[2].messageBody").value(messageActToCor3.getMessageBody()))
                .andExpect(jsonPath("$[2].createdAt").value(messageActToCor3.getCreatedAt().getEpochSecond()))

                .andExpect(jsonPath("$[3].id").value(messageCorToAct1.getId()))
                .andExpect(jsonPath("$[3].senderId").value(messageCorToAct1.getSenderId()))
                .andExpect(jsonPath("$[3].receiverId").value(messageCorToAct1.getReceiverId()))
                .andExpect(jsonPath("$[3].messageBody").value(messageCorToAct1.getMessageBody()))
                .andExpect(jsonPath("$[3].createdAt").value(messageCorToAct1.getCreatedAt().getEpochSecond()))

                .andExpect(jsonPath("$[4].id").value(messageCorToAct2.getId()))
                .andExpect(jsonPath("$[4].senderId").value(messageCorToAct2.getSenderId()))
                .andExpect(jsonPath("$[4].receiverId").value(messageCorToAct2.getReceiverId()))
                .andExpect(jsonPath("$[4].messageBody").value(messageCorToAct2.getMessageBody()))
                .andExpect(jsonPath("$[4].createdAt").value(messageCorToAct2.getCreatedAt().getEpochSecond()))

                .andExpect(jsonPath("$[5].id").value(messageCorToAct3.getId()))
                .andExpect(jsonPath("$[5].senderId").value(messageCorToAct3.getSenderId()))
                .andExpect(jsonPath("$[5].receiverId").value(messageCorToAct3.getReceiverId()))
                .andExpect(jsonPath("$[5].messageBody").value(messageCorToAct3.getMessageBody()))
                .andExpect(jsonPath("$[5].createdAt").value(messageCorToAct3.getCreatedAt().getEpochSecond()));

        verify(mockMessageService, times(1)).getAllMessagesBetweenUsers(Mockito.any(CorrespondentDTO.class));
    }

    @Test
    @DisplayName("getAllMessagesBetweenUsers returns 406 when passed CorrespondentDTO with null fields")
    void getAllMessagesBetweenUsersWhenNull() throws Exception{
        //Arrange
        when(mockMessageService.getAllMessagesBetweenUsers(Mockito.any(CorrespondentDTO.class))).thenThrow(NullPointerException.class);

        //Act & Assert
        mockMvcController.perform(
                        get(messagesEndpointURI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(correspondentDTO1NullCorrespondentId)))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @DisplayName("getAllMessagesBetweenUsers returns http 403 when passed correspondentDTO when users are not mutual favourites")
    void getAllMessagesBetweenUsersWhenNotMutualFavourites() {
        //Arrange
        //TODO need to mock messageService.getAllMessagesBetweenUsers throws exception when sender and receiver are not mutually favourited

        //Act & Assert
        //TODO return 403 when not mutually favourited
    }

}