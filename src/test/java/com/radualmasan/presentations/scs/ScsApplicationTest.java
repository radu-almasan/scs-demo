package com.radualmasan.presentations.scs;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.radualmasan.presentations.scs.service.NodeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.OutputCaptureExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
class ScsApplicationTest {

    @Mock
    private NodeService nodeService;
    @Mock
    private Sinks.Many<ObjectNode> messages;
    @InjectMocks
    private ScsApplication app;


    @Test
    void shouldWrapValue() {
        var processor = app.consumer();

        var input1 = Flux.just("1");
        var node1 = mock(ObjectNode.class);
        when(nodeService.newNode(any())).thenReturn(node1);

        processor.accept(input1);

        verify(messages).tryEmitNext(node1);
    }

}