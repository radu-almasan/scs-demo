package com.radualmasan.presentations.scs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radualmasan.presentations.scs.service.NodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
    @MockBean
    private NodeService nodeService;

    @Test
    void shouldProcessMessage() throws IOException {
        when(nodeService.newNode(any())).thenThrow(new RuntimeException("Testing"));

        inputDestination.send(MessageBuilder.withPayload(1).build(), "processor-in-0");
        inputDestination.send(MessageBuilder.withPayload(1).build(), "processor-in-1");

        var errorMessage = outputDestination.receive(1000, "processor-out-1");
        assertThat(errorMessage, is(not(nullValue())));
        assertThat(errorMessage.getHeaders().get("x-exception"), is("Testing"));

        var message = outputDestination.receive(1000, "processor-out-0");
        assertThat(message, is(nullValue()));
    }

}
