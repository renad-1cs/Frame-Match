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