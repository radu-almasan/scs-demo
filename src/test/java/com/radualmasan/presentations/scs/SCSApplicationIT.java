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

@SpringBootTest(properties = "spring.cloud.function.definition=processor")
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
        inputDestination.send(MessageBuilder.withPayload(1).build(), "processor-in-0");
        inputDestination.send(MessageBuilder.withPayload(1).build(), "processor-in-1");

        var message = outputDestination.receive(1000, "processor-out-0");

        assertThat(message, is(not(nullValue())));
        var node = objectMapper.readValue(message.getPayload(), ObjectNode.class);
        assertThat(node, is(not(nullValue())));
        assertThat(node.at("/value1").intValue(), is(1));
        assertThat(node.at("/value2").intValue(), is(1));
    }

}
