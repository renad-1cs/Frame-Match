package com.example.smart_glasses.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "FACE_SHAPES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FaceShape {

    public Integer getShapeId() {
		return shapeId;
	}

	public void setShapeId(Integer shapeId) {
		this.shapeId = shapeId;
	}

	public String getShapeName() {
		return shapeName;
	}

	public void setShapeName(String shapeName) {
		this.shapeName = shapeName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shape_id")
    private Integer shapeId;

    @NotBlank(message = "Shape name cannot be blank")
    @Column(name = "shape_name", nullable = false, length = 50)
    private String shapeName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}