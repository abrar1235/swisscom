package com.swisscom.operations.listeners;

import org.springframework.kafka.annotation.KafkaListener;

public class KafkaListeners {

    @KafkaListener(id = "id", topics = "test")
    public void maintenance(String message) {
        System.out.println("message: " + message);
    }
}
