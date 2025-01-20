package com.northcoders.bandit.service;

import com.northcoders.bandit.ProfileTestUtils;
import com.northcoders.bandit.model.FireBaseUser;
import com.northcoders.bandit.model.Profile;
import com.northcoders.bandit.model.ProfileRequestDTO;
import com.northcoders.bandit.model.ProfileResponseDTO;
import com.northcoders.bandit.repository.GenreManagerRepository;
import com.northcoders.bandit.repository.InstrumentManagerRepository;
import com.northcoders.bandit.repository.ProfileManagerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@DataJpaTest
class ProfileManagerServiceImplTest {

    @Mock
    ProfileManagerRepository profileManagerRepository;

    @Mock
    GenreManagerRepository genreManagerRepository;

    @Mock
    InstrumentManagerRepository instrumentManagerRepository;

    @InjectMocks
    ProfileManagerServiceImpl profileManagerService;

    @Mock
    UserInContextService userInContextService;

    @Test
    void postProfile() {
        Profile profile = ProfileTestUtils.createTestProfile("1L");

        when(profileManagerRepository.save(profile)).thenReturn(profile);

        Profile result = profileManagerService.postProfile(profile);

        assertThat(result.getProfile_id()).isEqualTo(1L);
        assertThat(result.getDescription()).isEqualTo(profile.getDescription());
    }

    @Test
    void getAllProfiles() {
        Profile profile_1 = ProfileTestUtils.createTestProfile("1L");
        Profile profile_2 = ProfileTestUtils.createTestProfile("2L");

        List<Profile> expected = new ArrayList<>();

        expected.add(profile_1);
        expected.add(profile_2);

        when(profileManagerRepository.findAll()).thenReturn(expected);

        ArrayList<ProfileResponseDTO> actual = profileManagerService.getAllProfiles();

        assertThat(actual).hasSize(expected.size());
        assertThat(actual.getFirst()).isEqualTo(profile_1);
        assertThat(actual.get(1)).isEqualTo(profile_2);
    }

    @Test
    void testUpdateProfile() {
        String profileId = "CX405";
        ProfileRequestDTO incomingProfile = new ProfileRequestDTO();
        incomingProfile.setUserName("Vanessa Diaz");
        incomingProfile.setSearchQuery("I am looking for a Guitarist in London");
        Profile existingProfile =new Profile();
        existingProfile.setProfile_name("Vanessa");
        existingProfile.setSearch_query("I am looking for a Guitarist in London");
        Profile updatedProfile  =new Profile();
        updatedProfile.setProfile_name("Vanessa Diaz");
        updatedProfile.setSearch_query("I am looking for a Guitarist in London");
        when(profileManagerRepository.findByfirebaseId(profileId)).thenReturn(Optional.of(existingProfile));
        when(profileManagerRepository.save(any(Profile.class))).thenReturn(updatedProfile);
        Profile updProfile = profileManagerService.updateProfile(incomingProfile);
        assertEquals(updatedProfile,updProfile);
        verify(profileManagerRepository,times(1)).findById(profileId);
        verify(profileManagerRepository,times(1)).save(existingProfile);
    }

    @Test
    void testGetUserProfile() {
        String firebaseUserId = "c401";
        Profile profile = new Profile();
        when(userInContextService.getcurrentUser()).thenReturn(new FireBaseUser(firebaseUserId));
        when(profileManagerRepository.findByfirebaseId(firebaseUserId)).thenReturn(Optional.of(profile));

        Optional<Profile> result = profileManagerService.getUserProfile();

        assertTrue(result.isPresent());
        verify(profileManagerRepository, times(1)).findByfirebaseId(firebaseUserId);
    }

    @Test
    @DisplayName("Test delete profile from service layer")
    void deleteProfile_success(){
        String firebaseId ="c401";
        when(profileManagerRepository.findByfirebaseId(firebaseId)).thenReturn(Optional.of(new Profile()));
        boolean b = profileManagerService.deleteById(firebaseId);
        assertTrue(b);
        verify(profileManagerRepository,times(1)).findByfirebaseId(firebaseId);
        verify(profileManagerRepository,times(1)).deleteByfirebaseId(firebaseId);
    }
    @Test
    void testDeleteById_Failure() {
        String id = "123";
        when(profileManagerRepository.findByfirebaseId(id)).thenReturn(Optional.empty());
        boolean result = profileManagerService.deleteById(id);
        assertFalse(result);
        verify(profileManagerRepository, times(1)).findByfirebaseId(id);
        verify(profileManagerRepository, times(0)).deleteByfirebaseId(id);
    }
}