package com.fitness.activityservice.dto;

import com.fitness.activityservice.model.ActivityType;
import lombok.Data;

import javax.print.attribute.standard.JobKOctets;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ActivityRequest {
    private String userId;
    private ActivityType activityType;
    private Integer duration;
    private Integer caloriesBurned;
    private LocalDateTime startTime;
    private Map<String, Object> additionalMetrics;
}
