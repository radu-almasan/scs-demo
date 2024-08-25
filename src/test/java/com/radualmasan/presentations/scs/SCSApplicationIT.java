package com.radualmasan.presentations.scs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
@ActiveProfiles("test")
class SCSApplicationIT {

    @Autowired
    private InputDestination inputDestination;
    @Autowired
    private OutputDestination outputDestination;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldProcessMessage() throws IOException {
        inputDestination.send(MessageBuilder.withPayload("1").build(), "processor-in-0");

        var message = outputDestination.receive(1000, "processor-out-0");
        assertThat(message, is(not(nullValue())));
        var node = objectMapper.readValue(message.getPayload(), ObjectNode.class);
        assertThat(node.at("/value").textValue(), is("1"));

    }

}
