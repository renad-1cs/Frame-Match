package com.example.smart_glasses.repository;

import com.example.smart_glasses.entity.ShapeFrameMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ShapeFrameMatchRepository extends JpaRepository<ShapeFrameMatch, Integer> {

    List<ShapeFrameMatch> findByShapeId(Integer shapeId);
}