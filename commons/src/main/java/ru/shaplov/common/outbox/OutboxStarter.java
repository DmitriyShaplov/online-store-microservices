package ru.shaplov.common.outbox;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "ru.shaplov.common.outbox")
public class OutboxStarter {
}
