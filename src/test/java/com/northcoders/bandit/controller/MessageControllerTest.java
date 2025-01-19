package com.northcoders.bandit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.northcoders.bandit.exception.InvalidDTOException;
import com.northcoders.bandit.model.CorrespondentRequestDTO;
import com.northcoders.bandit.model.Message;
import com.northcoders.bandit.model.MessageRequestDTO;
import com.northcoders.bandit.model.MessageResponseDTO;
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

    private MessageRequestDTO messageRequestDTO1 = new MessageRequestDTO("receiverId1", "valid message");
    private MessageRequestDTO messageRequestDTO1NullSenderId = new MessageRequestDTO(null, "valid message");
    private MessageRequestDTO messageRequestDTO1NullMessageBody = new MessageRequestDTO("receiverId1", null);
    private MessageRequestDTO messageRequestDTO1AllNull = new MessageRequestDTO(null, null);

    private MessageResponseDTO messageResponseDTO1= new MessageResponseDTO("senderId1", "receiverId1", "valid message",instant1);

    private String activeUser1 = "activeUserId1";

    private CorrespondentRequestDTO correspondentRequestDTO1 = new CorrespondentRequestDTO("correspondentId1");
    private CorrespondentRequestDTO correspondentRequestDTO1NullCorrespondentId = new CorrespondentRequestDTO(null);

    private MessageResponseDTO messageResponseDTOActToCor1 = new MessageResponseDTO("activeUserId1", "correspondentId1", "valid message", instant1);
    private MessageResponseDTO messageResponseDTOActToCor2 = new MessageResponseDTO("activeUserId1", "correspondentId1", "valid message", instant2);
    private MessageResponseDTO messageResponseDTOActToCor3 = new MessageResponseDTO("activeUserId1", "correspondentId1", "valid message", instant3);

    private MessageResponseDTO messageResponseDTOCorToAct1 = new MessageResponseDTO("correspondentId1", "activeUserId1", "valid message", instant4);
    private MessageResponseDTO messageResponseDTOCorToAct2 = new MessageResponseDTO("correspondentId1", "activeUserId1", "valid message", instant5);
    private MessageResponseDTO messageResponseDTOCorToAct3 = new MessageResponseDTO("correspondentId1", "activeUserId1", "valid message", instant6);

    private List<MessageResponseDTO> messageResponseDTOsOneEachWay = new ArrayList<>(
            List.of(messageResponseDTOActToCor1, messageResponseDTOCorToAct1));
    private List<MessageResponseDTO> messageResponseDTOsMultipleEachWay = new ArrayList<>(
            List.of(messageResponseDTOActToCor1, messageResponseDTOActToCor2, messageResponseDTOActToCor3, messageResponseDTOCorToAct1, messageResponseDTOCorToAct2, messageResponseDTOCorToAct3));


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
        when(this.mockMessageService.saveMessage(Mockito.any(MessageRequestDTO.class))).thenReturn(messageResponseDTO1);

        //Act & Assert
        mockMvcController.perform(
                        post(messagesEndpointURI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(messageRequestDTO1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.senderId").value(messageResponseDTO1.getSenderId()))
                .andExpect(jsonPath("$.receiverId").value(messageRequestDTO1.getReceiverId()))
                .andExpect(jsonPath("$.messageBody").value(messageRequestDTO1.getMessageBody()))
                .andExpect(jsonPath("$.createdAt").value(messageResponseDTO1.getCreatedAt().getEpochSecond()));

        verify(mockMessageService, times(1)).saveMessage(Mockito.any(MessageRequestDTO.class));
    }

    @Test
    @DisplayName("saveMessage returns 406 when passed message with null values")
    public void testSaveMessage() throws Exception {
        //Arrange
        when(mockMessageService.saveMessage(Mockito.any(MessageRequestDTO.class))).thenThrow(InvalidDTOException.class);

        //Act & Assert
        mockMvcController.perform(
                        post(messagesEndpointURI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(messageRequestDTO1NullSenderId)))
                .andExpect(status().isNotAcceptable());

        mockMvcController.perform(
                        post(messagesEndpointURI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(messageRequestDTO1NullMessageBody)))
                .andExpect(status().isNotAcceptable());

        mockMvcController.perform(
                        post(messagesEndpointURI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(messageRequestDTO1AllNull)))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @DisplayName("saveMessage returns http 403 when passed messageRequestDTO when users are not mutual favourites")
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
        when(mockMessageService.getAllMessagesBetweenUsers(Mockito.any(CorrespondentRequestDTO.class))).thenReturn(messageResponseDTOsOneEachWay);

        //Act & Assert
        mockMvcController.perform(
                        post(messagesEndpointURI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(correspondentRequestDTO1)))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$[0].senderId").value(messageResponseDTOActToCor1.getSenderId()))
                .andExpect(jsonPath("$[0].receiverId").value(messageResponseDTOActToCor1.getReceiverId()))
                .andExpect(jsonPath("$[0].messageBody").value(messageResponseDTOActToCor1.getMessageBody()))
                .andExpect(jsonPath("$[0].createdAt").value(messageResponseDTOActToCor1.getCreatedAt().getEpochSecond()))

                .andExpect(jsonPath("$[1].senderId").value(messageResponseDTOCorToAct1.getSenderId()))
                .andExpect(jsonPath("$[1].receiverId").value(messageResponseDTOCorToAct1.getReceiverId()))
                .andExpect(jsonPath("$[1].messageBody").value(messageResponseDTOCorToAct1.getMessageBody()))
                .andExpect(jsonPath("$[1].createdAt").value(messageResponseDTOCorToAct1.getCreatedAt().getEpochSecond()));

        verify(mockMessageService, times(1)).getAllMessagesBetweenUsers(Mockito.any(CorrespondentRequestDTO.class));
    }

    @Test
    @DisplayName("getAllMessagesBetweenUsers returns http 200 and all messages when multiple mutual message each way exists in database")
    void getAllMessagesBetweenUsersWhenValidMultipleMessages() throws Exception {
        //Arrange
        when(mockMessageService.getAllMessagesBetweenUsers(Mockito.any(CorrespondentRequestDTO.class))).thenReturn(messageResponseDTOsMultipleEachWay);

        //Act & Assert
        mockMvcController.perform(
                        post(messagesEndpointURI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(correspondentRequestDTO1)))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$[0].senderId").value(messageResponseDTOActToCor1.getSenderId()))
                .andExpect(jsonPath("$[0].receiverId").value(messageResponseDTOActToCor1.getReceiverId()))
                .andExpect(jsonPath("$[0].messageBody").value(messageResponseDTOActToCor1.getMessageBody()))
                .andExpect(jsonPath("$[0].createdAt").value(messageResponseDTOActToCor1.getCreatedAt().getEpochSecond()))

                .andExpect(jsonPath("$[1].senderId").value(messageResponseDTOActToCor2.getSenderId()))
                .andExpect(jsonPath("$[1].receiverId").value(messageResponseDTOActToCor2.getReceiverId()))
                .andExpect(jsonPath("$[1].messageBody").value(messageResponseDTOActToCor2.getMessageBody()))
                .andExpect(jsonPath("$[1].createdAt").value(messageResponseDTOActToCor2.getCreatedAt().getEpochSecond()))

                .andExpect(jsonPath("$[2].senderId").value(messageResponseDTOActToCor3.getSenderId()))
                .andExpect(jsonPath("$[2].receiverId").value(messageResponseDTOActToCor3.getReceiverId()))
                .andExpect(jsonPath("$[2].messageBody").value(messageResponseDTOActToCor3.getMessageBody()))
                .andExpect(jsonPath("$[2].createdAt").value(messageResponseDTOActToCor3.getCreatedAt().getEpochSecond()))

                .andExpect(jsonPath("$[3].senderId").value(messageResponseDTOCorToAct1.getSenderId()))
                .andExpect(jsonPath("$[3].receiverId").value(messageResponseDTOCorToAct1.getReceiverId()))
                .andExpect(jsonPath("$[3].messageBody").value(messageResponseDTOCorToAct1.getMessageBody()))
                .andExpect(jsonPath("$[3].createdAt").value(messageResponseDTOCorToAct1.getCreatedAt().getEpochSecond()))

                .andExpect(jsonPath("$[4].senderId").value(messageResponseDTOCorToAct2.getSenderId()))
                .andExpect(jsonPath("$[4].receiverId").value(messageResponseDTOCorToAct2.getReceiverId()))
                .andExpect(jsonPath("$[4].messageBody").value(messageResponseDTOCorToAct2.getMessageBody()))
                .andExpect(jsonPath("$[4].createdAt").value(messageResponseDTOCorToAct2.getCreatedAt().getEpochSecond()))
                
                .andExpect(jsonPath("$[5].senderId").value(messageResponseDTOCorToAct3.getSenderId()))
                .andExpect(jsonPath("$[5].receiverId").value(messageResponseDTOCorToAct3.getReceiverId()))
                .andExpect(jsonPath("$[5].messageBody").value(messageResponseDTOCorToAct3.getMessageBody()))
                .andExpect(jsonPath("$[5].createdAt").value(messageResponseDTOCorToAct3.getCreatedAt().getEpochSecond()));

        verify(mockMessageService, times(1)).getAllMessagesBetweenUsers(Mockito.any(CorrespondentRequestDTO.class));
    }

    @Test
    @DisplayName("getAllMessagesBetweenUsers returns 406 when passed CorrespondentRequestDTO with null fields")
    void getAllMessagesBetweenUsersWhenNull() throws Exception{
        //Arrange
        when(mockMessageService.getAllMessagesBetweenUsers(Mockito.any(CorrespondentRequestDTO.class))).thenThrow(InvalidDTOException.class);

        //Act & Assert
        mockMvcController.perform(
                        post(messagesEndpointURI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(correspondentRequestDTO1NullCorrespondentId)))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @DisplayName("getAllMessagesBetweenUsers returns http 403 when passed correspondentRequestDTO when users are not mutual favourites")
    void getAllMessagesBetweenUsersWhenNotMutualFavourites() {
        //Arrange
        //TODO need to mock messageService.getAllMessagesBetweenUsers throws exception when sender and receiver are not mutually favourited

        //Act & Assert
        //TODO return 403 when not mutually favourited
    }

}