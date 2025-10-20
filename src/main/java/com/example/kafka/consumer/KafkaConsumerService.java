package com.example.kafka.consumer;

import com.example.kafka.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    private final List<Message> receivedMessages = Collections.synchronizedList(new ArrayList<>());

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(Message message) {
        logger.info("Consumed message: {}", message);
        receivedMessages.add(message);
    }

    public List<Message> getReceivedMessages() {
        return new ArrayList<>(receivedMessages);
    }

    public void clearReceivedMessages() {
        receivedMessages.clear();
        logger.info("Cleared all received messages");
    }

    public int getReceivedMessagesCount() {
        return receivedMessages.size();
    }
}
