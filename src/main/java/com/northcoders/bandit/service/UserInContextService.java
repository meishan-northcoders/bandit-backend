package com.northcoders.bandit.service;

import com.northcoders.bandit.model.FireBaseUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserInContextService {
    public FireBaseUser getcurrentUser()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (FireBaseUser) authentication.getPrincipal();
    }

    public FireBaseUser getFireBaseUserMock(){
        FireBaseUser mockedFirebaseUser = new FireBaseUser();
        mockedFirebaseUser.setUserId("c401bxWARycXXcuxdxkN2nf6H2F2");
        mockedFirebaseUser.setUserName("Meishan");
        mockedFirebaseUser.setEmail("nc.meishan@gmail.com");
        mockedFirebaseUser.setGooglePictureLink("https://lh3.googleusercontent.com/a/ACg8ocKF4RuOneJ-H-LD3N4Y63PKapk1ReOf92qbz1Cbb9lglmI06g=s96-c");
        mockedFirebaseUser.setEmailVerified(true);
        return mockedFirebaseUser;
    }





}
