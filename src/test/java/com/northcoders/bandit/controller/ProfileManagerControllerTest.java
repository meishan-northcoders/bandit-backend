package com.northcoders.bandit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.northcoders.bandit.ProfileTestUtils;
import com.northcoders.bandit.model.*;
import com.northcoders.bandit.service.ProfileManagerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.ArrayList;


@SpringBootTest
@AutoConfigureMockMvc
class ProfileManagerControllerTest {

    @Mock
    private ProfileManagerServiceImpl profileManagerService;

    @InjectMocks
    private ProfileManagerController profileManagerController;

    @Autowired
    private MockMvc mockMvcController;

    ObjectMapper mapper;

    @BeforeEach
    void setup(){
        mockMvcController = MockMvcBuilders.standaloneSetup(profileManagerController).build();
        mapper = new ObjectMapper();
    }


    @Test
    @DisplayName("Tests get all profiles returns all profiles")
    void getAllProfiles() throws Exception {
        //Arrange
        ProfileResponseDTO profile_1 = ProfileTestUtils.createTestProfileResponseDTO(1L);
        ProfileResponseDTO profile_2 = ProfileTestUtils.createTestProfileResponseDTO(2L);


        ArrayList<ProfileResponseDTO> mockData = new ArrayList<>();

        mockData.add(profile_1);
        mockData.add(profile_2);

        when(profileManagerService.getAllProfiles()).thenReturn(mockData);

        //Act
        //Assert
        this.mockMvcController.perform(MockMvcRequestBuilders.get("/profile")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].profile_id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].profile_id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("test"))
                .andReturn();

        verify(profileManagerService, times(1)).getAllProfiles();
    }

    @Test
    @DisplayName("Tests post profile returns posted profile")
    void postProfile() throws Exception {
        //Arrange
        Profile profile = ProfileTestUtils.createTestProfile("C401");

        when(profileManagerService.postProfile(any(Profile.class))).thenReturn(profile);
        //Act
        //Assert
        this.mockMvcController.perform(post("/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(profile)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }


    @Test
    @DisplayName("Tests delete profile returns posted profile")
    void deleteProfile() throws Exception {
        Profile profile = ProfileTestUtils.createTestProfile("1L");

        when(profileManagerService.deleteById("1L")).thenReturn(true);

        this.mockMvcController.perform(delete("/profile?id=1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(profile)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();
    }
}