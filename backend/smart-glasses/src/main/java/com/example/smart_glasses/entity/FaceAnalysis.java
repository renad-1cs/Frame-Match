package com.example.smart_glasses.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "FACE_ANALYSIS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FaceAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "analysis_id")
    private Integer analysisId;

    @NotBlank(message = "Sessions ID must not be blank")
    @Column(name = "sessions_id", nullable = false, length = 100)
    private String sessionsId;

    @Column(name = "detected_shape_id")
    private Integer detectedShapeId;

    @Column(name = "scan_date")
    private LocalDateTime scanDate;

    @Column(name = "confidence_score", precision = 5, scale = 2)
    private BigDecimal confidenceScore;
}