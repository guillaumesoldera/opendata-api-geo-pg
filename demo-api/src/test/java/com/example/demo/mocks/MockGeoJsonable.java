package com.example.demo.mocks;

import com.example.demo.utils.json.GeoJsonable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class MockGeoJsonable implements GeoJsonable {

    @Override
    public ObjectNode geoJsonProperties(ObjectMapper objectMapper) {
        ObjectNode properties = objectMapper.createObjectNode();
        properties.put("value", "test");
        return properties;
    }

    @Override
    public String geoJsonGeometry(ObjectMapper objectMapper) {
        return "{\n" +
                "  \"type\": \"Point\",\n" +
                "  \"coordinates\": [125.6, 10.1]\n" +
                "}";
    }
}
