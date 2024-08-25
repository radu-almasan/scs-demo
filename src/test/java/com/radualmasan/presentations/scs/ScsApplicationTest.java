package com.radualmasan.presentations.scs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radualmasan.presentations.scs.config.AppProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.OutputCaptureExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.function.Tuples;

import static org.mockito.Answers.RETURNS_DEEP_STUBS;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
class ScsApplicationTest {

    @Mock(answer = RETURNS_DEEP_STUBS)
    private AppProperties appProperties;
    @InjectMocks
    private ScsApplication app;

    @Test
    void shouldWrapValue() {
        var objectMapper = new ObjectMapper();
        var processor = app.processor(objectMapper);

        var input1 = Flux.just(1, 2, 3);
        var input2 = Flux.just(1, 2, 3);

        StepVerifier.create(processor.apply(Tuples.of(input1, input2)))
                .expectNextMatches(node -> node.at("/value1").intValue() == 1 && node.at("/value2").intValue() == 1)
                .expectNextMatches(node -> node.at("/value1").intValue() == 2 && node.at("/value2").intValue() == 2)
                .expectNextMatches(node -> node.at("/value1").intValue() == 3 && node.at("/value2").intValue() == 3)
                .verifyComplete();
    }

}