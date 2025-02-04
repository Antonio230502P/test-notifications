package com.trainibit.test_notifications.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trainibit.test_notifications.response.UserDataResponseKafka;
import com.trainibit.test_notifications.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducerServiceImpl implements KafkaProducerService {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final String topicName = "emails_sent";

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void sendMessage(UserDataResponseKafka userResponseKafka) {
        try{
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicName, userResponseKafka.getUuid().toString(), objectMapper.writeValueAsString(userResponseKafka));
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    System.out.println("Sent message=[" + userResponseKafka +
                            "] with offset=[" + result.getRecordMetadata().offset() + "]");
                } else {
                    System.out.println("Unable to send message=[" +
                            userResponseKafka + "] due to : " + ex.getMessage());
                }
            });
        }catch (Exception e){
            throw new RuntimeException("Error converting UserResponseKafkca to JSON", e);
        }
    }
}
