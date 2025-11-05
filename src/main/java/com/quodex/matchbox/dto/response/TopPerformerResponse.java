package com.quodex.matchbox.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopPerformerResponse {
    private String memberId;
    private String fullName;
    private String avatar;
    private double efficiency;
    private long tasksCompletedThisWeek;
}
