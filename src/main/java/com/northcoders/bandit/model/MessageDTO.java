package com.northcoders.bandit.model;

import jakarta.persistence.*;

import java.time.Instant;

public class MessageDTO {

    //Sender id taken from context i.e. is the current user
    private String receiverId;
    private String messageBody;
    //Is instant the best date time data type to use?
    private Instant createdDate;

    public MessageDTO() {
    }

    public MessageDTO(String senderId, String receiverId, String messageBody, Instant createdDate) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageBody = messageBody;
        this.createdDate = createdDate;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }
}
