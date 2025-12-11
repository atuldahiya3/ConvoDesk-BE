package com.convodesk.backend.Service;

import com.convodesk.backend.Entity.Business;
import com.convodesk.backend.Entity.CallLog;
import com.convodesk.backend.Entity.CallSegment;
import com.convodesk.backend.Entity.CallStatus;
import com.convodesk.backend.Repository.BusinessRepository;
import com.convodesk.backend.Repository.CallLogRepository;
import com.convodesk.backend.Repository.CallSegmentRepository;
import com.convodesk.backend.Security.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CallService {

    private final CallLogRepository callLogRepository;
    private final CallSegmentRepository callSegmentRepository;
    private final BusinessRepository businessRepository;

    // Create a new call log when call starts
    public CallLog startCall(String callerNumber) {

        Long tenantId = TenantContext.getTenantId();
        Business business = businessRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Business not found"));

        CallLog log = CallLog.builder()
                .business(business)
                .callerNumber(callerNumber)
                .startedAt(LocalDateTime.now())
                .status(CallStatus.ANSWERED)
                .build();

        return callLogRepository.save(log);
    }

    // Add a segment to the call
    public void addSegment(Long callId, String sender, String text) {
        CallSegment segment = CallSegment.builder()
                .callLog(CallLog.builder().id(callId).build())
                .sender(sender)
                .text(text)
                .timestamp(LocalDateTime.now())
                .build();

        callSegmentRepository.save(segment);
    }

    // End the call
    public CallLog endCall(Long callId) {
        CallLog log = callLogRepository.findById(callId)
                .orElseThrow(() -> new RuntimeException("Call not found"));

        log.setEndedAt(LocalDateTime.now());
        log.setDurationSeconds(
                log.getEndedAt().toEpochSecond(null) - log.getStartedAt().toEpochSecond(null)
        );

        return callLogRepository.save(log);
    }

    // Get call logs for the tenant
    public List<CallLog> getMyCallLogs() {
        Long tenantId = TenantContext.getTenantId();
        return callLogRepository.findByBusinessIdOrderByStartedAtDesc(tenantId);
    }

    // Get full call transcript
    public List<CallSegment> getCallTranscript(Long callId) {
        return callSegmentRepository.findByCallLogIdOrderByTimestamp(callId);
    }
    public CallLog startCallForBusiness(Long businessId, String callerNumber) {
        Business business = businessRepository.findById(businessId).orElseThrow();
        CallLog log = CallLog.builder()
                .business(business)
                .callerNumber(callerNumber)
                .startedAt(LocalDateTime.now())
                .status(CallStatus.ANSWERED)
                .build();
        return callLogRepository.save(log);
    }
}
