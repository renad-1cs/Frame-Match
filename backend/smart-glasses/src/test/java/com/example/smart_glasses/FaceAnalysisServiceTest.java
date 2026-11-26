package com.example.smart_glasses;

import com.example.smart_glasses.entity.GlassesFrame;
import com.example.smart_glasses.entity.ShapeFrameMatch;
import com.example.smart_glasses.repository.FaceAnalysisRepository;
import com.example.smart_glasses.repository.GlassesFrameRepository;
import com.example.smart_glasses.repository.ShapeFrameMatchRepository;
import com.example.smart_glasses.service.FaceAnalysisService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FaceAnalysisServiceTest {

    @Mock
    private FaceAnalysisRepository faceAnalysisRepository;

    @Mock
    private ShapeFrameMatchRepository shapeFrameMatchRepository;

    @Mock
    private GlassesFrameRepository glassesFrameRepository;

    @InjectMocks
    private FaceAnalysisService faceAnalysisService;

    @Test
    void testRecommendFramesForFaceShape() {

        ShapeFrameMatch match = mock(ShapeFrameMatch.class);
        GlassesFrame frame = mock(GlassesFrame.class);

        when(match.getFrameId()).thenReturn(1);

        when(shapeFrameMatchRepository.findByShapeId(1))
                .thenReturn(List.of(match));

        when(glassesFrameRepository.findAllById(List.of(1)))
                .thenReturn(List.of(frame));

        List<GlassesFrame> frames =
                faceAnalysisService.recommendFramesForFaceShape(1);

        assertEquals(1, frames.size());
        assertSame(frame, frames.get(0));
    }
}