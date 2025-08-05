package com.example.demoproject.luckydraw.dto;

import java.util.List;

/**
 * Result DTO for multiple draw operations
 */
public class MultipleDrawResult {
    private final List<DrawResult> results;
    private final Integer totalDraws;

    public MultipleDrawResult(List<DrawResult> results) {
        this.results = results;
        this.totalDraws = results.size();
    }

    public List<DrawResult> getResults() {
        return results;
    }

    public Integer getTotalDraws() {
        return totalDraws;
    }
} 