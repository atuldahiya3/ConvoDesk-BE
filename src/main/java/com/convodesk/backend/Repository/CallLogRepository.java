package com.convodesk.backend.Repository;


import com.convodesk.backend.Entity.CallLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CallLogRepository extends JpaRepository<CallLog, Long> {
    List<CallLog> findByBusinessIdOrderByStartedAtDesc(Long businessId);
}
