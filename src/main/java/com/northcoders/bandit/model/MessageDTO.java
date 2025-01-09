package com.northcoders.bandit.model;

import jakarta.persistence.*;

import java.time.Instant;

public class MessageDTO {

    private Long senderId;
    private Long receiverId;
    private String messageBody;
    //Is instant the best date time data type to use?
    private Instant createdDate;

    public MessageDTO() {
    }

    public MessageDTO(Long senderId, Long receiverId, String messageBody, Instant createdDate) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageBody = messageBody;
        this.createdDate = createdDate;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
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
