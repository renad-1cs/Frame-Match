package com.example.smart_glasses.repository;

import com.example.smart_glasses.entity.FaceShape;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaceShapeRepository extends JpaRepository<FaceShape, Integer> {

}