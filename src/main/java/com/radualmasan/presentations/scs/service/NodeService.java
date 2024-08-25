package com.radualmasan.presentations.scs.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.util.function.Tuple2;

@Service
@RequiredArgsConstructor
public class NodeService {

    private final ObjectMapper objectMapper;

    public ObjectNode newNode(Tuple2<Integer, Integer> tuple) {
        var node = objectMapper.createObjectNode();
        node.put("value1", tuple.getT1());
        node.put("value2", tuple.getT2());
        return node;
    }

}
