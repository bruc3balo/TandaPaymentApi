package com.tanda.payment_api.configs.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan(basePackageClasses = {KafkaProperties.class})
public class KafkaSimpleMailProducerConfig {

    private final KafkaProperties kafkaProperties;

    public KafkaSimpleMailProducerConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;

    }

    //Todo type
    public Map<String, Object> simpleNotificationConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, ?> simpleNotificationProducerFactory() {
        return new DefaultKafkaProducerFactory<>(simpleNotificationConfig());
    }

    //todo ? type
    @Bean(name = "simpleNotificationKafkaTemplate")
    public KafkaTemplate<String, ?> simpleNotificationKafkaTemplate(ProducerFactory<String, ?> simpleNotificationProducerFactory) {
        return new KafkaTemplate<>(simpleNotificationProducerFactory);
    }

}
