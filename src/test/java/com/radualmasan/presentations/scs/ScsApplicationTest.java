package com.radualmasan.presentations.scs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
class ScsApplicationTest {

    @InjectMocks
    private ScsApplication app;
    
    @Test
    void shouldSupplyRandomNumber() {
        var source = app.randomSource();
        assertThat(source.get(), is(not(nullValue())));
    }

    @Test
    void shouldWrapValue() {
        var objectMapper = new ObjectMapper();
        var processor = app.processor(objectMapper);
        var node = processor.apply(1);
        assertThat(node, is(notNullValue()));
        assertThat(node.at("/value").intValue(), is(1));
    }

    @Test
    void shouldOutput(CapturedOutput output) {
        var sink = app.consoleLog();
        var objectMapper = new ObjectMapper();
        var node = objectMapper.createObjectNode();
        node.put("value", 1);
        sink.accept(node);
        assertThat(output.getAll(), Matchers.containsString(node.toString()));
    }
}