package com.trainibit.test_notifications.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trainibit.test_notifications.request.UserDataRequestKafka;
import com.trainibit.test_notifications.response.UserDataResponseKafka;
import com.trainibit.test_notifications.service.KafkaConsumerService;
import com.trainibit.test_notifications.service.KafkaProducerService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class KafkaConsumerServiceImpl implements KafkaConsumerService{
    private final String TOPIC = "user_registered";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Override
    @KafkaListener(topics = TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void readMessages(ConsumerRecord<String, String> message) {
        try {
            UserDataRequestKafka userDataRequestKafka = objectMapper.readValue(message.value(), UserDataRequestKafka.class);

            UserDataResponseKafka userDataResponseKafka = new UserDataResponseKafka();
            userDataResponseKafka.setEmail(userDataRequestKafka.getEmail());
            userDataResponseKafka.setFirstToken(userDataRequestKafka.getFirstToken());
            userDataResponseKafka.setUuid(userDataRequestKafka.getUuid());
            userDataResponseKafka.setTemplateUuid(UUID.randomUUID());

            System.out.printf("Correo enviado a %s correctamente\n", userDataRequestKafka.getEmail());
            kafkaProducerService.sendMessage(userDataResponseKafka);

        } catch (Exception e) {
            System.err.println("Error al deserializar el mensaje: " + e.getMessage());
        }
    }
}
