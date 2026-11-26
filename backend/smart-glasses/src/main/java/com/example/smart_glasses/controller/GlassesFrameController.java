package com.example.smart_glasses.controller;

import com.example.smart_glasses.entity.GlassesFrame;
import com.example.smart_glasses.service.GlassesFrameService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/frames")
@CrossOrigin(origins = "*")
public class GlassesFrameController {

    private final GlassesFrameService glassesFrameService;

    public GlassesFrameController(GlassesFrameService glassesFrameService) {
        this.glassesFrameService = glassesFrameService;
    }

    @GetMapping
    public List<GlassesFrame> getAllFrames() {
        return glassesFrameService.getAllFrames();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlassesFrame> getFrameById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(glassesFrameService.getFrameById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public GlassesFrame createFrame(@Valid @RequestBody GlassesFrame frame) {
        return glassesFrameService.saveFrame(frame);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFrame(@PathVariable Integer id) {
        try {
            glassesFrameService.getFrameById(id);
            glassesFrameService.deleteFrame(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}