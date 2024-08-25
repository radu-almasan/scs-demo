package com.radualmasan.presentations.scs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.radualmasan.presentations.scs.config.AppProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SpringBootApplication
@ConfigurationPropertiesScan
@RequiredArgsConstructor
@Slf4j
public class ScsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScsApplication.class, args);
    }

    @Bean
    public Supplier<Flux<Integer>> randomSource(AppProperties appProperties) {
        var rand = new Random();
        return () -> Flux.interval(appProperties.producer().interval())
                .map(_ -> rand.nextInt());
    }

    @Bean
    public Function<Flux<Integer>, Flux<ObjectNode>> processor(ObjectMapper objectMapper) {
        return input -> input.map(i -> {
            var node = objectMapper.createObjectNode();
            node.put("value", i);
            return node;
        });
    }

    @Bean
    public Consumer<Flux<ObjectNode>> consoleLog() {
        return flux -> flux.subscribe(node -> log.info("{}", node));
    }

}
