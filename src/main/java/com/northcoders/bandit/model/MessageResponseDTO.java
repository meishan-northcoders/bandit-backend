package com.northcoders.bandit.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Random;
import java.util.UUID;


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

//    public static void main(String[] args) {
////        UUID uuid = UUID.randomUUID();
////        System.out.println(uuid+ "  "    +uuid.toString().length());
//
//        Random random = new Random();
//        int randomInt = random.nextInt();
//        String artist_id = "";
//        StringBuilder sb = new StringBuilder();
//        System.out.println(randomInt);
//        for(int  i = 0 ; i<9 ; i++){
//            if(i<4 || i ==8){
//                char randomChar = (char) ('A' + random.nextInt(26));
//                sb.append(randomChar);
//            }
//            if(i>=4 && i<8){
//                int randomNumber = random.nextInt(100);
//                sb.append(randomNumber);
//            }
//
//        }
//        System.out.println(sb.toString());
//
//    }
}
