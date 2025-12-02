package com.convodesk.backend.Controller;

import com.convodesk.backend.Entity.CallLog;
import com.convodesk.backend.Entity.CallSegment;
import com.convodesk.backend.Service.CallService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/business/calls")
@RequiredArgsConstructor
public class CallController {

    private final CallService callService;

    @PostMapping("/start")
    public CallLog startCall(@RequestParam String callerNumber) {
        return callService.startCall(callerNumber);
    }

    @PostMapping("/{id}/segment")
    public String addSegment(@PathVariable Long id,
                             @RequestParam String sender,
                             @RequestParam String text) {
        callService.addSegment(id, sender, text);
        return "OK";
    }

    @PostMapping("/{id}/end")
    public CallLog endCall(@PathVariable Long id) {
        return callService.endCall(id);
    }

    @GetMapping
    public List<CallLog> getCallLogs() {
        return callService.getMyCallLogs();
    }

    @GetMapping("/{id}/transcript")
    public List<CallSegment> getTranscript(@PathVariable Long id) {
        return callService.getCallTranscript(id);
    }
}
