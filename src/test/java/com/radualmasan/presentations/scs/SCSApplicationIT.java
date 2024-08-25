package com.radualmasan.presentations.scs;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Sinks;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
@ActiveProfiles("test")
class SCSApplicationIT {

    @Autowired
    private InputDestination inputDestination;
    @MockBean
    private Sinks.Many<ObjectNode> messages;

    @Test
    void shouldProcessMessage() {
        inputDestination.send(MessageBuilder.withPayload("1").build(), "consumer-in-0");
        verify(messages).tryEmitNext(argThat(o -> o.at("/value").textValue().equals("1")));
    }

}
