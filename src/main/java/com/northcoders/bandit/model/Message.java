package com.northcoders.bandit.model;

import jakarta.persistence.*;

import java.time.Instant;
@Table(name = "messages")
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private Long id;

    //Do we need something to say that senderId and receiverId are foreign keys? They aren't really foreign keys in our PostgreSQL database, because they are the primary keys from the Firebase database of users
    @Column(name = "sender_id", updatable = false, nullable = false)
    private String senderId;

    @Column(name = "receiver_id", updatable = false, nullable = false)
    private String receiverId;

    @Column(name = "message_body")
    private String messageBody;

    //Is instant the best date time data type to use?
    @Column(name = "created_at")
    private Instant createdAt;

    public Message() {
    }

    public Message(Long id, String senderId, String receiverId, String messageBody, Instant createdAt) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageBody = messageBody;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
