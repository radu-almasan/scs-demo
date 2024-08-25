package com.radualmasan.presentations.scs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radualmasan.presentations.scs.config.AppProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
class ScsApplicationTest {

    @Mock(answer = RETURNS_DEEP_STUBS)
    private AppProperties appProperties;
    @InjectMocks
    private ScsApplication app;

    @Test
    void shouldSupplyRandomNumber() {
        when(appProperties.producer().interval()).thenReturn(Duration.ofSeconds(1));
        var source = app.randomSource(appProperties);
        StepVerifier.create(source.get())
                .expectNextCount(1)
                .thenCancel()
                .verify();
    }

    @Test
    void shouldWrapValue() {
        var objectMapper = new ObjectMapper();
        var processor = app.processor(objectMapper);
        var input = Flux.just(1);
        StepVerifier.create(processor.apply(input))
                .expectNextMatches(node -> (node.at("/value").intValue() == 1))
                .verifyComplete();
    }

    @Test
    void shouldOutput(CapturedOutput output) {
        var objectMapper = new ObjectMapper();
        var sink = app.consoleLog();
        var node = objectMapper.createObjectNode();
        node.put("value", 1);

        sink.accept(Flux.just(node));
        assertThat(output.getAll(), containsString(node.toString()));
    }
    
}