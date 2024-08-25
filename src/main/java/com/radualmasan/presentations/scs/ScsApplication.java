package com.radualmasan.presentations.scs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.util.function.Function;

@SpringBootApplication
@ConfigurationPropertiesScan
@RequiredArgsConstructor
@Slf4j
public class ScsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScsApplication.class, args);
    }

    @Bean
    public Function<Tuple2<Flux<Integer>, Flux<Integer>>, Flux<ObjectNode>> processor(ObjectMapper objectMapper) {
        return input -> Flux.zip(input.getT1(), input.getT2()).map(tuple -> {
            var node = objectMapper.createObjectNode();
            node.put("value1", tuple.getT1());
            node.put("value2", tuple.getT2());
            return node;
        });
    }

}
