package com.convodesk.backend.Dto;

import lombok.Data;

@Data
public class AgentCallRequest {
    private Long callLogId;
    private String roomName;   // livekit room
    private String token;      // token for agent to join (if needed)
}
