package com.convodesk.backend.Dto;

import lombok.Data;

import java.util.Map;

@Data
public class TelephonyIncomingDto {
    private Map<String, String> params;
}
