package ru.shaplov.common.consumer.order;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Arrays;
import java.util.List;

public class OrderConsumerCondition extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        ConditionMessage.Builder message = ConditionMessage.forCondition("OpenID Connect Issuer URI Condition");
        Environment environment = context.getEnvironment();
        List<String> consumers = Arrays.asList(environment.getProperty("consumers", "").split("\\s"));
        if (consumers.contains("order")) {
            return ConditionOutcome.match(message.foundExactly("order consumer"));
        }
        return ConditionOutcome.noMatch(message.didNotFind("order consumer").atAll());
    }
}
