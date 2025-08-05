package com.example.demoproject.admin.controller;

import com.example.demoproject.admin.dto.*;
import com.example.demoproject.admin.service.AdminPrizeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin/activities/{activityId}/prizes")
@Tag(name = "Admin Prize Management", description = "Admin operations for prize management")
public class AdminPrizeController {

    @Autowired
    private AdminPrizeService adminPrizeService;

    @PutMapping("/{prizeId}")
    @Operation(summary = "Update prize", description = "Update prize details (name, quantity, probability)")
    public ResponseEntity<PrizeResponse> updatePrize(
            @PathVariable Long activityId,
            @PathVariable Long prizeId,
            @Valid @RequestBody UpdatePrizeRequest request) {
        return ResponseEntity.ok(adminPrizeService.updatePrize(activityId, prizeId, request));
    }

    @GetMapping
    @Operation(summary = "Get all prizes for activity", description = "Retrieve all prizes for an activity")
    public ResponseEntity<List<PrizeResponse>> getPrizes(@PathVariable Long activityId) {
        return ResponseEntity.ok(adminPrizeService.getPrizes(activityId));
    }
} 