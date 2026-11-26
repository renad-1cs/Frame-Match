package com.example.smart_glasses.service;

import com.example.smart_glasses.entity.GlassesFrame;
import com.example.smart_glasses.repository.GlassesFrameRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GlassesFrameService {

    private final GlassesFrameRepository glassesFrameRepository;

    public GlassesFrameService(GlassesFrameRepository glassesFrameRepository) {
        this.glassesFrameRepository = glassesFrameRepository;
    }

    public List<GlassesFrame> getAllFrames() {
        return glassesFrameRepository.findAll();
    }

    public GlassesFrame getFrameById(Integer id) {
        return glassesFrameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Glasses frame not found with id: " + id));
    }

    public GlassesFrame saveFrame(GlassesFrame frame) {
        return glassesFrameRepository.save(frame);
    }

    public void deleteFrame(Integer id) {
        glassesFrameRepository.deleteById(id);
    }
}