package com.northcoders.bandit.model;

public class CorrespondentDTO {
    private String correspondentId;

    public CorrespondentDTO(String correspondentId) {
        this.correspondentId = correspondentId;
    }

    public CorrespondentDTO() {
    }

    public String getCorrespondentId() {
        return correspondentId;
    }

    public void setCorrespondentId(String correspondentId) {
        this.correspondentId = correspondentId;
    }
}
