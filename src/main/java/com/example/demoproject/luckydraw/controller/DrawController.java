package com.example.demoproject.luckydraw.controller;

import com.example.demoproject.luckydraw.dto.*;
import com.example.demoproject.luckydraw.service.DrawExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

/**
 * REST Controller for draw operations
 */
@RestController
@RequestMapping("/api/luckydraw/draw")
@CrossOrigin(origins = "*")
public class DrawController {

    @Autowired
    private DrawExecutionService drawExecutionService;

    /**
     * Perform a single draw
     * @param request Draw request
     * @return Draw result
     */
    @PostMapping("/single")
    public ResponseEntity<DrawResult> performDraw(@Valid @RequestBody DrawRequest request) {
        DrawResult result = drawExecutionService.performDraw(request);
        return ResponseEntity.ok(result);
    }

    /**
     * Perform multiple draws
     * @param request Multiple draw request
     * @return Multiple draw results
     */
    @PostMapping("/multiple")
    public ResponseEntity<MultipleDrawResult> performMultipleDraws(@Valid @RequestBody MultipleDrawRequest request) {
        MultipleDrawResult result = drawExecutionService.performMultipleDraws(request);
        return ResponseEntity.ok(result);
    }
} 