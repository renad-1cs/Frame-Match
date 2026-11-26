package com.example.smart_glasses.controller;

import com.example.smart_glasses.entity.GlassesFrame;
import com.example.smart_glasses.entity.ShapeFrameMatch;
import com.example.smart_glasses.service.ShapeFrameMatchService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@CrossOrigin(origins = "*")
public class ShapeFrameMatchController {

    private final ShapeFrameMatchService shapeFrameMatchService;

    public ShapeFrameMatchController(ShapeFrameMatchService shapeFrameMatchService) {
        this.shapeFrameMatchService = shapeFrameMatchService;
    }

    @GetMapping
    public List<ShapeFrameMatch> getAllMatches() {
        return shapeFrameMatchService.getAllMatches();
    }

    @PostMapping
    public ShapeFrameMatch createMatch(@Valid @RequestBody ShapeFrameMatch match) {
        return shapeFrameMatchService.saveMatch(match);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable Integer id) {
        if (!shapeFrameMatchService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        shapeFrameMatchService.deleteMatch(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/frames/{faceShapeId}")
    public List<GlassesFrame> getFramesByFaceShapeId(@PathVariable Integer faceShapeId) {
        return shapeFrameMatchService.getFramesByFaceShapeId(faceShapeId);
    }
}