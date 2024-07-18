package com.tanda.payment_api.configs.kafka;


import com.tanda.payment_api.globals.GlobalVariables;
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
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
@ComponentScan(basePackageClasses = {KafkaProperties.class})
@Slf4j
public class KafkaGwRequestConsumerConfig {


    private final KafkaProperties kafkaProperties;

    public KafkaGwRequestConsumerConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    //SimpleNotificationForm
    public Map<String, Object> jsonConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GlobalVariables.KAFKA_GW_GROUP_ID);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());


        System.out.println("==============================  KAKFA CONSUMER ============================");
        return props;

    }

    public ConsumerFactory<String, GwRequest> gwRequestConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(jsonConfig(), new StringDeserializer(), new JsonDeserializer<>(GwRequest.class));
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, GwRequest>> gwRequestKafkaContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, GwRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(gwRequestConsumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

        return factory;
    }

}
