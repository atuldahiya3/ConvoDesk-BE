package com.convodesk.backend.Dto;

import lombok.Data;

@Data
public class FaqResponse {
    private Long id;
    private String question;
    private String answer;
}