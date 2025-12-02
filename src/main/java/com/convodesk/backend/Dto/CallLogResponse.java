package com.convodesk.backend.Dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CallLogResponse {
    private Long id;
    private String callerNumber;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private Long durationSeconds;
    private String status;
    private String summary;
}
