package com.quodex.matchbox.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectSummaryResponse {
    private String id;
    private String name;
    private String slug;
}
