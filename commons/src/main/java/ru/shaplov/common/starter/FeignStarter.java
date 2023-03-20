package ru.shaplov.common.starter;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "ru.shaplov.common.client")
public class FeignStarter {
}
