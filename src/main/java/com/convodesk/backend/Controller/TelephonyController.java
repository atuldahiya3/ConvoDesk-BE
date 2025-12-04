package com.convodesk.backend.Controller;

import com.convodesk.backend.Service.TelephonyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/telephony")
@RequiredArgsConstructor
public class TelephonyController {

    private final TelephonyService telephonyService;

    @PostMapping("/incoming")
    public ResponseEntity<?> incomingCall(@RequestParam Map<String, String> params) {
        // Twilio or Exotel will POST here (form-encoded).
        // Key params: From (caller), To (business number), CallSid (provider call id)
        String providerCallId = params.get("CallSid");
        String from = params.get("From");
        String to = params.get("To");

        Long callLogId = telephonyService.handleIncomingCall(providerCallId, from, to);

        // For Twilio: respond with TwiML XML to instruct Twilio to connect to your LiveKit or SIP endpoint.
        // For now, return simple JSON with callLogId (provider-specific instructions handled in TelephonyService).
        return ResponseEntity.ok(Map.of("callLogId", callLogId));
    }
}
