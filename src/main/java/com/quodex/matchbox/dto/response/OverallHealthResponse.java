package com.quodex.matchbox.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OverallHealthResponse {
    private String grade;
    private double onTimeDelivery;
    private double teamSatisfaction;
    private String comment;
}
