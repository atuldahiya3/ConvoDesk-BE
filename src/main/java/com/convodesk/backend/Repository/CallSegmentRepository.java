package com.convodesk.backend.Repository;

import com.convodesk.backend.Entity.CallSegment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CallSegmentRepository extends JpaRepository<CallSegment, Long> {
    List<CallSegment> findByCallLogIdOrderByTimestamp(Long callId);
}
