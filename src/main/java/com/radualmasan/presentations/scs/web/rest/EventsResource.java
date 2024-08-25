package com.radualmasan.presentations.scs.web.rest;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

@RestController
@RequiredArgsConstructor
public class EventsResource {

    private final Sinks.Many<ObjectNode> messages;

    @GetMapping(path = "/events", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<ObjectNode>> getEvents() {
        return messages.asFlux()
                .map(o -> ServerSentEvent.<ObjectNode>builder()
                        .event("node")
                        .data(o)
                        .build());
    }

}
