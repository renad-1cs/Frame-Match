package com.example.smart_glasses.controller;

import com.example.smart_glasses.entity.FaceAnalysis;
import com.example.smart_glasses.entity.GlassesFrame;
import com.example.smart_glasses.service.FaceAnalysisService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/face-analysis")
@CrossOrigin(origins = "*")
public class FaceAnalysisController {

    private final FaceAnalysisService faceAnalysisService;

    public FaceAnalysisController(FaceAnalysisService faceAnalysisService) {
        this.faceAnalysisService = faceAnalysisService;
    }

    @GetMapping
    public List<FaceAnalysis> getAllAnalyses() {
        return faceAnalysisService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FaceAnalysis> getById(@PathVariable Integer id) {
        return faceAnalysisService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public FaceAnalysis createAnalysis(@Valid @RequestBody FaceAnalysis faceAnalysis) {
        return faceAnalysisService.save(faceAnalysis);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnalysis(@PathVariable Integer id) {
        if (faceAnalysisService.getById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        faceAnalysisService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/recommend/{faceShapeId}")
    public List<GlassesFrame> recommendFrames(@PathVariable Integer faceShapeId) {
        return faceAnalysisService.recommendFramesForFaceShape(faceShapeId);
    }
}