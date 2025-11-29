package com.convodesk.backend.Dto;

import lombok.Data;

@Data
public class RegisterBusinessRequest {
    private String email;
    private String password;
    private String businessName;
    private String phone;
    private String address;
}