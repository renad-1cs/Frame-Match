package com.example.smart_glasses.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "GLASSES_FRAMES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlassesFrame {

    public Integer getFrameId() {
		return frameId;
	}

	public void setFrameId(Integer frameId) {
		this.frameId = frameId;
	}

	public String getFrameName() {
		return frameName;
	}

	public void setFrameName(String frameName) {
		this.frameName = frameName;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getFrameStyle() {
		return frameStyle;
	}

	public void setFrameStyle(String frameStyle) {
		this.frameStyle = frameStyle;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getModel3dUrl() {
		return model3dUrl;
	}

	public void setModel3dUrl(String model3dUrl) {
		this.model3dUrl = model3dUrl;
	}

	public Integer getManagedByAdminId() {
		return managedByAdminId;
	}

	public void setManagedByAdminId(Integer managedByAdminId) {
		this.managedByAdminId = managedByAdminId;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "frame_id")
    private Integer frameId;

    @NotBlank(message = "Frame name cannot be blank")
    @Column(name = "frame_name", nullable = false, length = 100)
    private String frameName;

    @Column(name = "brand", length = 50)
    private String brand;

    @Column(name = "frame_style", length = 50)
    private String frameStyle;

    @Column(name = "color", length = 30)
    private String color;

    @NotNull(message = "Price is required")
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "model_3d_url", length = 255)
    private String model3dUrl;

    @Column(name = "managed_by_admin_id")
    private Integer managedByAdminId;
}