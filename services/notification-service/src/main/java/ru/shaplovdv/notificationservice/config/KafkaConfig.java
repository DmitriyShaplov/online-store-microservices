package ru.shaplovdv.notificationservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.shaplov.common.model.event.notification.NotificationPayload;

@Configuration
public class KafkaConfig {

    private final KafkaProperties properties;
    private final ObjectMapper objectMapper;

    public KafkaConfig(KafkaProperties properties,
                       ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    @Bean
    public ConsumerFactory<String, NotificationPayload> consumerFactoryNotification() {
        return new DefaultKafkaConsumerFactory<>(this.properties.buildConsumerProperties(),
                new StringDeserializer(),
                new ErrorHandlingDeserializer<>(new JsonDeserializer<>(NotificationPayload.class, objectMapper, false)));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, NotificationPayload> notificationContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, NotificationPayload> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryNotification());
        return factory;
    }
}
