package com.radualmasan.presentations.scs;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.radualmasan.presentations.scs.service.NodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.function.Function;

@SpringBootApplication
@ConfigurationPropertiesScan
@RequiredArgsConstructor
@Slf4j
public class ScsApplication {

    private final NodeService nodeService;

    public static void main(String[] args) {
        SpringApplication.run(ScsApplication.class, args);
    }

    @Bean
    public Function<Flux<String>,
            Tuple2<Flux<ObjectNode>, Flux<Message<Object>>>> processor() {
        return input -> {
            Sinks.Many<Message<Object>> errors = Sinks.many().unicast().onBackpressureBuffer();

            var work = input
                    .map(nodeService::newNode)
                    .onErrorContinue((t, o) -> {
                        var message = MessageBuilder.withPayload(o)
                                .setHeader("x-exception", t.getMessage())
                                .build();

                        errors.tryEmitNext(message);
                    })
                    .doOnComplete(errors::tryEmitComplete);

            return Tuples.of(work, errors.asFlux());
        };
    }


}
