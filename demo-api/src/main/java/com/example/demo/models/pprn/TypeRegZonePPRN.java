package com.example.demo.models.pprn;

import com.example.demo.utils.json.Jsonable;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class TypeRegZonePPRN implements Jsonable {

    public final String code;
    public final String label;
    public final String color;

    public TypeRegZonePPRN(String code, String label, String color) {
        this.code = code;
        this.label = label;
        this.color = color;
    }

    public static TypeRegZonePPRN defaultTypeRef() {
        return new TypeRegZonePPRN("--", "Non communiqué", "#ccc");
    }

    @Override
    public JsonNode toJson(ObjectMapper objectMapper) {
        ObjectNode node = objectMapper.createObjectNode();
        node
                .put("code", code)
                .put("label", label)
                .put("color", color);
        return node;
    }
}
