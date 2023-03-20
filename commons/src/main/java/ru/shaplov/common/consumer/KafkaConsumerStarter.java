package ru.shaplov.common.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;
import ru.shaplov.common.model.event.order.OrderEventPayload;

@Configuration
@ComponentScan
@Slf4j
public class KafkaConsumerStarter {

    private final KafkaProperties properties;
    private final ObjectMapper objectMapper;

    public KafkaConsumerStarter(KafkaProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    @Bean
    public ConsumerFactory<String, OrderEventPayload> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(this.properties.buildConsumerProperties(),
                new StringDeserializer(),
                new ErrorHandlingDeserializer<>(new JsonDeserializer<>(OrderEventPayload.class, objectMapper, false)));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderEventPayload> orderContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OrderEventPayload> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setCommonErrorHandler(errorHandler());
        return factory;
    }

    @Bean
    public DefaultErrorHandler errorHandler() {
        BackOff fixedBackOff = new FixedBackOff();
        return new DefaultErrorHandler((consumerRecord, exception) ->
                log.error("NON ACHIEVABLE"), fixedBackOff);
    }
}
