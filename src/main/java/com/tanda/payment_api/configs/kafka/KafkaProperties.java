package com.tanda.payment_api.configs.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "spring.kafka")
@AllArgsConstructor
@Getter
@Setter
public class KafkaProperties {

    private String clientId;

    private List<String> bootstrapServers;

    public KafkaProperties() {

    }
}
