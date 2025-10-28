package com.quodex.matchbox.dto.request;
import com.quodex.matchbox.model.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamRequest {
    private String name;
    private String description;
    private String avatar;
    private String createdBy;
    private Set<String> members = new HashSet<>();
    private Map<String, String> roles = new HashMap<>();
    private List<Project> projects = new ArrayList<>();

}
