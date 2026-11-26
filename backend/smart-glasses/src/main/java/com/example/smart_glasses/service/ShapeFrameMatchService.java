package com.example.smart_glasses.service;

import com.example.smart_glasses.entity.GlassesFrame;
import com.example.smart_glasses.entity.ShapeFrameMatch;
import com.example.smart_glasses.repository.GlassesFrameRepository;
import com.example.smart_glasses.repository.ShapeFrameMatchRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShapeFrameMatchService {

    private final ShapeFrameMatchRepository shapeFrameMatchRepository;
    private final GlassesFrameRepository glassesFrameRepository;

    public ShapeFrameMatchService(
            ShapeFrameMatchRepository shapeFrameMatchRepository,
            GlassesFrameRepository glassesFrameRepository) {

        this.shapeFrameMatchRepository = shapeFrameMatchRepository;
        this.glassesFrameRepository = glassesFrameRepository;
    }

    public List<ShapeFrameMatch> getAllMatches() {
        return shapeFrameMatchRepository.findAll();
    }

    public List<ShapeFrameMatch> getMatchesByFaceShapeId(Integer faceShapeId) {
        return shapeFrameMatchRepository.findByShapeId(faceShapeId);
    }

    public ShapeFrameMatch saveMatch(ShapeFrameMatch match) {
        return shapeFrameMatchRepository.save(match);
    }

    public void deleteMatch(Integer id) {
        shapeFrameMatchRepository.deleteById(id);
    }
    public boolean existsById(Integer id) {
        return shapeFrameMatchRepository.existsById(id);
    }
    
    public List<GlassesFrame> getFramesByFaceShapeId(Integer faceShapeId) {

        List<Integer> frameIds = shapeFrameMatchRepository
                .findByShapeId(faceShapeId)
                .stream()
                .map(ShapeFrameMatch::getFrameId)
                .toList();

        return glassesFrameRepository.findAllById(frameIds);
    }
}