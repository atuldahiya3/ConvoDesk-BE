package com.convodesk.backend.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "call_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CallLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Every call belongs to a business
    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    private String callerNumber;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    private Long durationSeconds;

    @Enumerated(EnumType.STRING)
    private CallStatus status; // ANSWERED / MISSED / ESCALATED

    private String summary; // AI generated summary (optional)
}
