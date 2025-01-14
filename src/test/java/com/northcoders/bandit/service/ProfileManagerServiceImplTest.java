package com.northcoders.bandit.service;

import com.northcoders.bandit.ProfileTestUtils;
import com.northcoders.bandit.model.Profile;
import com.northcoders.bandit.repository.GenreManagerRepository;
import com.northcoders.bandit.repository.InstrumentManagerRepository;
import com.northcoders.bandit.repository.ProfileManagerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;


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

    @Test
    void postProfile() {
        Profile profile = ProfileTestUtils.createTestProfile(1L);

        when(profileManagerRepository.save(profile)).thenReturn(profile);

        Profile result = profileManagerService.postProfile(profile);

        assertThat(result.getProfile_id()).isEqualTo(1L);
        assertThat(result.getDescription()).isEqualTo(profile.getDescription());
    }

    @Test
    void getAllProfiles() {
        Profile profile_1 = ProfileTestUtils.createTestProfile(1L);
        Profile profile_2 = ProfileTestUtils.createTestProfile(2L);

        Set<Profile> expected = new HashSet<>();

        expected.add(profile_1);
        expected.add(profile_2);

        when(profileManagerRepository.findAll()).thenReturn(expected);

        ArrayList<Profile> actual = profileManagerService.getAllProfiles();

        assertThat(actual).hasSize(expected.size());
        assertThat(actual.getFirst()).isEqualTo(profile_1);
        assertThat(actual.get(1)).isEqualTo(profile_2);
    }

    @Test
    @DisplayName("Test delete profile from service layer")
    void deleteProfile(){
        Profile profile_1 = ProfileTestUtils.createTestProfile(1L);
        Profile profile_2 = ProfileTestUtils.createTestProfile(2L);

        when(profileManagerRepository.findAll()).thenReturn(Set.of(profile_1,profile_2));
        ArrayList<Profile> profiles = profileManagerService.getAllProfiles();
        assertThat(profiles.size()).isEqualTo(2);

        profileManagerService.deleteById(1L);

        when(profileManagerRepository.findAll()).thenReturn(Set.of(profile_2));
        ArrayList<Profile> profilesAfterDelete = profileManagerService.getAllProfiles();
        assertThat(profilesAfterDelete.size()).isEqualTo(1);
    }
}