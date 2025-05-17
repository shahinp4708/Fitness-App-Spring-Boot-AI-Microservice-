package com.fitness.aiservice.service;

import com.fitness.aiservice.model.Activity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListener {
//    @Value("${app.rabbitmq.queue}")
//    private String rabbitQueue;
    private  final ActivityAiService activityAiService;
    @RabbitListener(queues ="${app.rabbitmq.queue}" )
    public  void processActivity(Activity activity){
        log.info("received activity id is   : {}",activity.getId());
        log.info("generated recommendation : {}",activityAiService.generateRecommendation(activity));
    }
}
