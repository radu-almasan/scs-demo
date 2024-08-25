package com.radualmasan.presentations.scs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties("app")
public record AppProperties(Producer producer) {
    public record Producer(Duration interval) {
    }
}
