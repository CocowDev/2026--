package com.hotel.booking.dto;

import lombok.Data;

@Data
public class LoginResponse {

    private String token;
    private Object user;
    private Boolean isAdmin;

    public LoginResponse(String token, Object user, Boolean isAdmin) {
        this.token = token;
        this.user = user;
        this.isAdmin = isAdmin;
    }

}
