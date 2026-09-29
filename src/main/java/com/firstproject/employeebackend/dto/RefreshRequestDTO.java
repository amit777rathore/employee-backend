package com.firstproject.employeebackend.dto;

public class RefreshRequestDTO {

    private String refreshToken;

    public RefreshRequestDTO() {
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}