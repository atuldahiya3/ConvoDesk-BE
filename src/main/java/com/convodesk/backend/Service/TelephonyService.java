package com.convodesk.backend.Service;

import com.convodesk.backend.Entity.Business;
import com.convodesk.backend.Repository.BusinessRepository;
import com.convodesk.backend.Service.CallService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelephonyService {

    private final BusinessRepository businessRepository;
    private final CallService callService;
    private final LiveKitService liveKitService;
    private final VoiceAgentService voiceAgentService;

    /**
     * Handle an incoming call from provider.
     * - Map the 'to' number to a business
     * - Set tenant context (if needed)
     * - Create CallLog
     * - Create/ensure LiveKit room
     * - Start voice agent
     * - Return callLogId for tracking
     */
    public Long handleIncomingCall(String providerCallId, String from, String to) {
        // find business by assignedNumber
        Business biz = businessRepository.findByAssignedNumber(to)
                .orElseThrow(() -> new RuntimeException("Business not found for number: " + to));

        // Note: set tenant context if your CallService requires it, or set it before call creation
        // For example, you can have a lightweight method that creates call with business id
        Long callLogId = callService.startCallForBusiness(biz.getId(), from).getId();

        // Create a room name
        String roomName = "call-" + callLogId;
        liveKitService.ensureRoom(roomName);

        // Start voice agent that will handle STT/LLM/TTS and publish to room
        voiceAgentService.startAgent(callLogId, roomName);

        // TODO: instruct telephony provider to bridge call to LiveKit room. This requires provider specific API.
        // For Twilio, you could return TwiML <Connect><WebRtc /></Connect> or use Twilio's SIP interface.
        // For Exotel, follow Exotel docs to connect to your WebRTC gateway.

        return callLogId;
    }
}
