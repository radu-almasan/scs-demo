package com.radualmasan.presentations.scs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class ScsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScsApplication.class, args);
    }

    @Bean
    public Supplier<Integer> randomSource() {
        var rand = new Random();
        return rand::nextInt;
    }

    @Bean
    public Function<Integer, ObjectNode> processor(ObjectMapper objectMapper) {
        return input -> {
            var node = objectMapper.createObjectNode();
            node.put("value", input);
            return node;
        };
    }

    @Bean
    public Consumer<ObjectNode> consoleLog() {
        return node -> log.info("{}", node);
    }

}
