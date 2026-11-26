package com.example.smart_glasses.service;

import com.example.smart_glasses.entity.FaceShape;
import com.example.smart_glasses.repository.FaceShapeRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FaceShapeService {

    private final FaceShapeRepository faceShapeRepository;

    public FaceShapeService(FaceShapeRepository faceShapeRepository) {
        this.faceShapeRepository = faceShapeRepository;
    }

    public List<FaceShape> getAllFaceShapes() {
        return faceShapeRepository.findAll();
    }

    public Optional<FaceShape> getFaceShapeById(Integer id) {
        return faceShapeRepository.findById(id);
    }

    public FaceShape saveFaceShape(FaceShape faceShape) {
        return faceShapeRepository.save(faceShape);
    }

    public void deleteFaceShape(Integer id) {
        faceShapeRepository.deleteById(id);
    }
}