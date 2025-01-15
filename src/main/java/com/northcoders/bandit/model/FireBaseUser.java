package com.northcoders.bandit.model;

public class FireBaseUser {
    private String userId;
    private String email;
    private String userName;
    private Boolean isEmailVerified;
    private String googlePictureLink;

    public FireBaseUser() {

    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public FireBaseUser(String userName) {
        this.userName = userName;
    }

    public Boolean getEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGooglePictureLink() {
        return googlePictureLink;
    }

    public void setGooglePictureLink(String googlePictureLink) {
        this.googlePictureLink = googlePictureLink;
    }
}
