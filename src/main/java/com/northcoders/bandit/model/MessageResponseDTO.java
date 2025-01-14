package com.northcoders.bandit.model;

import jakarta.persistence.*;
import java.time.Instant;


public class MessageResponseDTO {

    private String senderId;
    private String receiverId;
    private String messageBody;
    private Instant createdAt;

    public MessageResponseDTO() {
    }

    public MessageResponseDTO(String senderId, String receiverId, String messageBody, Instant createdAt) {

        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageBody = messageBody;
        this.createdAt = createdAt;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
