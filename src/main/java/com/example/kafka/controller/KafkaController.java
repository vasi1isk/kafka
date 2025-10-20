package com.example.kafka.controller;

import com.example.kafka.consumer.KafkaConsumerService;
import com.example.kafka.model.Message;
import com.example.kafka.producer.KafkaProducerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/kafka")
public class KafkaController {

    private final KafkaProducerService producerService;
    private final KafkaConsumerService consumerService;

    public KafkaController(KafkaProducerService producerService,
                          KafkaConsumerService consumerService) {
        this.producerService = producerService;
        this.consumerService = consumerService;
    }

    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendMessage(@RequestParam String message) {
        producerService.sendMessage(message);

        Map<String, String> response = new HashMap<>();
        response.put("status", "Message sent to Kafka topic");
        response.put("message", message);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/send-object")
    public ResponseEntity<Map<String, String>> sendMessageObject(@RequestBody Message message) {
        producerService.sendMessage(message);

        Map<String, String> response = new HashMap<>();
        response.put("status", "Message object sent to Kafka topic");
        response.put("messageId", message.getId());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getReceivedMessages() {
        return ResponseEntity.ok(consumerService.getReceivedMessages());
    }

    @GetMapping("/messages/count")
    public ResponseEntity<Map<String, Integer>> getMessagesCount() {
        Map<String, Integer> response = new HashMap<>();
        response.put("count", consumerService.getReceivedMessagesCount());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/messages")
    public ResponseEntity<Map<String, String>> clearMessages() {
        consumerService.clearReceivedMessages();

        Map<String, String> response = new HashMap<>();
        response.put("status", "All messages cleared");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Kafka Test Application");

        return ResponseEntity.ok(response);
    }
}
