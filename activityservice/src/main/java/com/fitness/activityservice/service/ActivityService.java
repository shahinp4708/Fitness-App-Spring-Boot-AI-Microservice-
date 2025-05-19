package com.fitness.activityservice.service;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.model.Activity;
import com.fitness.activityservice.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserValidationService userValidationService;
    private final RabbitTemplate rabbitTemplate;
    @Value("${app.rabbitmq.exchange}")
    private String exchange;
    @Value("${app.rabbitmq.queue}")
    private String queue;
    @Value("${app.rabbitmq.routingkey}")
    private String routingKey;

    public ActivityResponse trackActivity(ActivityRequest activityRequest){
        log.info("Activity received for registration to db");
        log.info("Activity Type {}" ,activityRequest.getActivityType());


        boolean isValidUser = userValidationService.validateUser(activityRequest.getUserId());
        if(!isValidUser)
            throw new RuntimeException("Invalid user"+activityRequest.getUserId());
        Activity activity = Activity.builder()
                .userId(activityRequest.getUserId())
                .type(activityRequest.getActivityType())
                .duration(activityRequest.getDuration())
                .caloriesBurned(activityRequest.getCaloriesBurned())
                .startTime(activityRequest.getStartTime())
                .additionalMetrics(activityRequest.getAdditionalMetrics())
                .build();
        Activity savedActivity = activityRepository.save(activity);
        //publish to rabbitmq
        try{
            rabbitTemplate.convertAndSend(exchange,routingKey,savedActivity);
            log.info("Successfully published activity to RabbitMQ");

        }catch (Exception e ){
            log.error("Failed to publish to rabbitMQ"+e);

        }
        return mapToResponse(savedActivity);
    }
    private ActivityResponse mapToResponse(Activity activity){
        ActivityResponse response = new ActivityResponse();
        response.setId(activity.getId());
        response.setUserId(activity.getUserId());
        response.setType(activity.getType());
        response.setDuration(activity.getDuration());
        response.setCaloriesBurned(activity.getCaloriesBurned());
        response.setStartTime(activity.getStartTime());
        response.setAdditionalMetrics(activity.getAdditionalMetrics());
        response.setCreatedAt(activity.getCreatedAt());
        response.setUpdatedAt(activity.getUpdatedAt());
        return response;
    }

    public List<ActivityResponse> getUserActivities(String userId) {
       List<Activity> activities= activityRepository.findByUserId(userId);
       return activities.stream()
               .map(this::mapToResponse)
               .collect(Collectors.toList());
    }

    public ActivityResponse getActivityById(String activityId) {
        return activityRepository.findById(activityId)
                .map(this:: mapToResponse)
                .orElseThrow(() -> new RuntimeException("activity not found"+activityId));
    }
}
