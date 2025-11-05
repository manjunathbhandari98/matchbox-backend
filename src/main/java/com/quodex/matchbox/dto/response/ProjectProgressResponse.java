package com.quodex.matchbox.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectProgressResponse {
    private String projectId;
    private String projectName;
    private double completion; // %
    private double onTimeRate; // %
}