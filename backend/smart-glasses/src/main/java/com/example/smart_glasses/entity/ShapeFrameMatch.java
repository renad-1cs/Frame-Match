package com.example.smart_glasses.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SHAPE_FRAME_MATCH",
    uniqueConstraints = @UniqueConstraint(columnNames = {"shape_id", "frame_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShapeFrameMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Integer matchId;

    @NotNull(message = "Shape ID is required")
    @Column(name = "shape_id", nullable = false)
    private Integer shapeId;

    @NotNull(message = "Frame ID is required")
    @Column(name = "frame_id", nullable = false)
    private Integer frameId;
    public Integer getFrameId() {

        return frameId;

    }
    @NotNull(message = "Suitability score is required")
    @Min(value = 0, message = "Suitability score must be at least 0")
    @Max(value = 100, message = "Suitability score must be at most 100")
    @Column(name = "suitability_score", nullable = false)
    private Integer suitabilityScore;
}