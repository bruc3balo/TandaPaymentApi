package com.tanda.payment_api.configs.kafka;

import com.tanda.payment_api.globals.GlobalVariables;
import com.tanda.payment_api.models.GwRequest;
import com.tanda.payment_api.models.GwResponse;
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
public class KafkaGwResponseProducerConfig {

    private final KafkaProperties kafkaProperties;

    public KafkaGwResponseProducerConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;

    }

    public Map<String, Object> jsonConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    //GwRequest
    @Bean
    public ProducerFactory<String, GwResponse> gwResponseProducerFactory() {
        return new DefaultKafkaProducerFactory<>(jsonConfig(), new StringSerializer(), new JsonSerializer<>());
    }

    @Bean(name = GlobalVariables.GW_RESPONSE_KAFKA_TEMPLATE)
    public KafkaTemplate<String, GwResponse> gwResponseKafkaTemplate(ProducerFactory<String, GwResponse> gwResponseProducerFactory) {
        return new KafkaTemplate<>(gwResponseProducerFactory);
    }

}