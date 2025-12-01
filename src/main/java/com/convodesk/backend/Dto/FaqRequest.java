package com.convodesk.backend.Dto;

import lombok.Data;

@Data
public class FaqRequest {
    private String question;
    private String answer;
}