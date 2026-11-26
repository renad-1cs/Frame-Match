package com.example.smart_glasses.controller;

import com.example.smart_glasses.entity.FaceShape;
import com.example.smart_glasses.service.FaceShapeService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faceshapes")
@CrossOrigin(origins = "*")
public class FaceShapeController {

    private final FaceShapeService faceShapeService;

    public FaceShapeController(FaceShapeService faceShapeService) {
        this.faceShapeService = faceShapeService;
    }

    @GetMapping
    public List<FaceShape> getAllFaceShapes() {
        return faceShapeService.getAllFaceShapes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FaceShape> getFaceShapeById(@PathVariable Integer id) {
        return faceShapeService.getFaceShapeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public FaceShape createFaceShape(@Valid @RequestBody FaceShape faceShape) {
        return faceShapeService.saveFaceShape(faceShape);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaceShape(@PathVariable Integer id) {
        if (faceShapeService.getFaceShapeById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        faceShapeService.deleteFaceShape(id);
        return ResponseEntity.noContent().build();
    }
}