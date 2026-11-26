package com.example.smart_glasses.service;

import com.example.smart_glasses.entity.FaceAnalysis;
import com.example.smart_glasses.entity.GlassesFrame;
import com.example.smart_glasses.entity.ShapeFrameMatch;

import com.example.smart_glasses.repository.FaceAnalysisRepository;
import com.example.smart_glasses.repository.GlassesFrameRepository;
import com.example.smart_glasses.repository.ShapeFrameMatchRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FaceAnalysisService {

    private final FaceAnalysisRepository faceAnalysisRepository;
    private final ShapeFrameMatchRepository shapeFrameMatchRepository;
    private final GlassesFrameRepository glassesFrameRepository;

    public FaceAnalysisService(
            FaceAnalysisRepository faceAnalysisRepository,
            ShapeFrameMatchRepository shapeFrameMatchRepository,
            GlassesFrameRepository glassesFrameRepository) {

        this.faceAnalysisRepository = faceAnalysisRepository;
        this.shapeFrameMatchRepository = shapeFrameMatchRepository;
        this.glassesFrameRepository = glassesFrameRepository;
    }

    public List<FaceAnalysis> getAll() {
        return faceAnalysisRepository.findAll();
    }

    public Optional<FaceAnalysis> getById(Integer id) {
        return faceAnalysisRepository.findById(id);
    }

    public FaceAnalysis save(FaceAnalysis faceAnalysis) {
        return faceAnalysisRepository.save(faceAnalysis);
    }

    public void delete(Integer id) {
        faceAnalysisRepository.deleteById(id);
    }

    public List<GlassesFrame> recommendFramesForFaceShape(Integer faceShapeId) {

        List<Integer> frameIds = shapeFrameMatchRepository
                .findByShapeId(faceShapeId)
                .stream()
                .map(ShapeFrameMatch::getFrameId)
                .toList();

        return glassesFrameRepository.findAllById(frameIds);
    }
}