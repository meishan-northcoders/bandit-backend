package com.northcoders.bandit.exception;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public class ApiException {
    private HttpStatus status;
    private String title;
    private String detail;
    private Instant timeStamp;

    public ApiException(HttpStatus status, String title, Throwable e) {
        this.status = status;
        this.title = title;
        this.detail = e.getLocalizedMessage();
        this.timeStamp = Instant.now();
    }

    public ApiException() {
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setTimeStamp(Instant timeStamp) {
        this.timeStamp = timeStamp;
    }
}
