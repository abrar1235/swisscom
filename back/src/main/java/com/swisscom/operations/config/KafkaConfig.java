package com.swisscom.operations.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@ConditionalOnProperty(value = "spring.profiles.active", havingValue = "kafka")
public class KafkaConfig {

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name("test")
                .partitions(1)
                .replicas(1)
                .build();
    }


}
