package com.example.kafka.producer;

import com.example.kafka.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    private final KafkaTemplate<String, Message> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String topicName;

    public KafkaProducerService(KafkaTemplate<String, Message> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String content) {
        Message message = new Message(
                UUID.randomUUID().toString(),
                content,
                LocalDateTime.now()
        );

        CompletableFuture<SendResult<String, Message>> future = kafkaTemplate.send(topicName, message);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                logger.info("Sent message=[{}] with offset=[{}]",
                    message,
                    result.getRecordMetadata().offset());
            } else {
                logger.error("Unable to send message=[{}] due to : {}",
                    message,
                    ex.getMessage());
            }
        });
    }

    public void sendMessage(Message message) {
        if (message.getId() == null) {
            message.setId(UUID.randomUUID().toString());
        }
        if (message.getTimestamp() == null) {
            message.setTimestamp(LocalDateTime.now());
        }

        CompletableFuture<SendResult<String, Message>> future = kafkaTemplate.send(topicName, message);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                logger.info("Sent message=[{}] with offset=[{}]",
                    message,
                    result.getRecordMetadata().offset());
            } else {
                logger.error("Unable to send message=[{}] due to : {}",
                    message,
                    ex.getMessage());
            }
        });
    }
}
