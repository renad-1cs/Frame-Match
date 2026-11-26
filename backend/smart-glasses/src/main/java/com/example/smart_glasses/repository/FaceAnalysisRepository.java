package com.example.smart_glasses.repository;

import com.example.smart_glasses.entity.FaceAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaceAnalysisRepository extends JpaRepository<FaceAnalysis, Integer> {

}