package com.radualmasan.presentations.scs.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NodeService {

    private final ObjectMapper objectMapper;

    public ObjectNode newNode(String input) {
        var node = objectMapper.createObjectNode();
        node.put("value", input);
        return node;
    }

}
