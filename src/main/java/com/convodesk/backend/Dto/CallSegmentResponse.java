package com.convodesk.backend.Dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CallSegmentResponse {
    private String sender;
    private String text;
    private LocalDateTime timestamp;
}
