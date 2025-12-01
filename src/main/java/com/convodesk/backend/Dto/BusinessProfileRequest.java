package com.convodesk.backend.Dto;

import lombok.Data;

@Data
public class BusinessProfileRequest {
    private String businessName;
    private String phone;
    private String address;
    private String timezone;
}
