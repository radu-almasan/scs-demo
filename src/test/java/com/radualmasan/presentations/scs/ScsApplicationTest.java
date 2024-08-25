package com.radualmasan.presentations.scs;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.radualmasan.presentations.scs.config.AppProperties;
import com.radualmasan.presentations.scs.service.NodeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.OutputCaptureExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
class ScsApplicationTest {

    @Mock(answer = RETURNS_DEEP_STUBS)
    private AppProperties appProperties;
    @Mock
    private NodeService nodeService;
    @InjectMocks
    private ScsApplication app;

    @Test
    void shouldWrapValue() {
        var processor = app.processor();

        var input1 = Flux.just("1");
        var node1 = mock(ObjectNode.class);

        when(nodeService.newNode(any())).thenReturn(node1);

        var output = processor.apply(input1);

        StepVerifier.create(output.getT1())
                .expectNextMatches(node -> node == node1)
                .verifyComplete();

        StepVerifier.create(output.getT2())
                .expectComplete()
                .verify();
    }

}