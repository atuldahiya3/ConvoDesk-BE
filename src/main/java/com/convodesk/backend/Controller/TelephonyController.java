package com.convodesk.backend.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/telephony")
@RequiredArgsConstructor
public class TelephonyController {

//    private final TelephonyService telephonyService;
//
//    // Exotel/Twilio invokes this when a call arrives
//    @PostMapping("/incoming")
//    public ResponseEntity<?> incomingCall(@RequestBody Map<String, String> payload) {
//        String caller = payload.get("From"); // or payload key from Exotel
//        String callId = payload.get("CallSid"); // provider call id
//        Long callLogId = telephonyService.handleIncomingCall(callId, caller);
//
//        // Return provider-specific response: XML/TwiML or JSON to bridge call
//        // Example: return bridging instruction or 200 OK
//        return ResponseEntity.ok(Map.of("status","accepted","callLogId", callLogId));
//    }
}
