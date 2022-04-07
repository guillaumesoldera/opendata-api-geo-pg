package com.example.demo.mocks;

import com.example.demo.utils.json.Jsonable;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class MockJsonable implements Jsonable {

    public final String id;
    public final String name;

    public MockJsonable(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public JsonNode toJson(ObjectMapper objectMapper) {
        ObjectNode jsonNode = objectMapper.createObjectNode();
        return jsonNode
                .put("id", id)
                .put("name", name);
    }

}
