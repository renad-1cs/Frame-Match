package com.example.smart_glasses.repository;

import com.example.smart_glasses.entity.GlassesFrame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlassesFrameRepository extends JpaRepository<GlassesFrame, Integer> {

}