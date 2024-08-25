package com.radualmasan.presentations.scs;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.radualmasan.presentations.scs.service.NodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Consumer;

@SpringBootApplication
@ConfigurationPropertiesScan
@RequiredArgsConstructor
@Slf4j
public class ScsApplication {

    private final NodeService nodeService;
    private final Sinks.Many<ObjectNode> messages;

    public static void main(String[] args) {
        SpringApplication.run(ScsApplication.class, args);
    }

    @Bean
    public Consumer<Flux<String>> consumer() {
        return input -> input
                .map(nodeService::newNode)
                .onErrorContinue((t, o) -> log.error("Error encountered for message {}", o, t))
                .subscribe(messages::tryEmitNext);
    }

}
