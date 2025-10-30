package com.quodex.matchbox.dto.response;

import com.quodex.matchbox.model.Project;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamResponse {
    private String id;
    private String name;
    private String description;
    private String avatar;
    private String createdBy;
    private List<MemberResponse> members;
    private List<ProjectSummaryResponse> projects = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
