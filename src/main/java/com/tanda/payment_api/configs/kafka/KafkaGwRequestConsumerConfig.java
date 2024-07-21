package com.tanda.payment_api.configs.kafka;


import com.tanda.payment_api.globals.GPaymentVariables;
import com.tanda.payment_api.models.GwRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.tanda.payment_api.globals.GPaymentVariables.KAFKA_GW_REQUEST_CONTAINER_FACTORY;

@Configuration
@ComponentScan(basePackageClasses = {KafkaProperties.class})
@Slf4j
public class KafkaGwRequestConsumerConfig {

    private final KafkaProperties kafkaProperties;

    public KafkaGwRequestConsumerConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    public Map<String, Object> jsonConfig() {
        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GPaymentVariables.KAFKA_GW_GROUP_ID);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, GwRequest.class.getName());
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());

        System.out.println("====================== KAKFA CONSUMER ==========================");
        return props;
    }

    @Bean
    public ConsumerFactory<String, GwRequest> gwRequestConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(jsonConfig(), new StringDeserializer(), new ErrorHandlingDeserializer<>(new JsonDeserializer<>(GwRequest.class, false)));
    }

    @Bean(name = KAFKA_GW_REQUEST_CONTAINER_FACTORY)
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, GwRequest>> gwRequestKafkaContainerFactory(ConsumerFactory<String, GwRequest> gwRequestConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, GwRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(gwRequestConsumerFactory);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

}
