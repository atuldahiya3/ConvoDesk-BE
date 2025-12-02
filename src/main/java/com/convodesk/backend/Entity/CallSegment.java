package com.convodesk.backend.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "call_segments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CallSegment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "call_log_id", nullable = false)
    private CallLog callLog;

    private String sender;
    private String text;
    private LocalDateTime timestamp;
}
