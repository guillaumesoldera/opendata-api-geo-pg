package com.example.demo.utils.json;

import com.example.demo.mocks.MockGeoJsonable;
import com.example.demo.mocks.MockJsonable;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class JsonUtilsTests {

    @Test
    public void testOfNullableNodeWithNull() {
        assertTrue(JsonUtils.ofNullableNode(null).isEmpty());
    }

    @Test
    public void testOfNullableNodeWithNullNode() {
        assertTrue(JsonUtils.ofNullableNode(NullNode.instance).isEmpty());
    }

    @Test
    public void testOfNullableNodeWithNonNullNode() throws Exception {
        JsonNode node = new ObjectMapper().readTree("{\"name\": \"toto\"}");
        assertFalse(JsonUtils.ofNullableNode(node).isEmpty());
    }

    @Test
    public void testToJsonArrayEmpty() {
        ArrayNode emptyJsonArray = JsonUtils.toJsonArray(Lists.emptyList());
        String expectedJsonArray = "[]";
        assertEquals(expectedJsonArray, emptyJsonArray.toString());
    }

    @Test
    public void testToJsonArray() {
        Jsonable misc1 = new MockJsonable("123", "name1");
        Jsonable misc2 = new MockJsonable("456", "name2");
        ArrayNode jsonArray = JsonUtils.toJsonArray(Arrays.asList(misc1, misc2));
        String expectedJsonArray = "[{\"id\":\"123\",\"name\":\"name1\"},{\"id\":\"456\",\"name\":\"name2\"}]";
        assertEquals(expectedJsonArray, jsonArray.toString());
    }

    @Test
    public void testToJsonStringArrayEmpty() {
        ArrayNode emptyJsonArray = JsonUtils.toJsonStringArray(Lists.emptyList());
        String expectedJsonArray = "[]";
        assertEquals(expectedJsonArray, emptyJsonArray.toString());
    }

    @Test
    public void testToJsonStringArray() {
        ArrayNode jsonArray = JsonUtils.toJsonStringArray(Arrays.asList("foo", "bar"));
        String expectedJsonArray = "[\"foo\",\"bar\"]";
        assertEquals(expectedJsonArray, jsonArray.toString());
    }

    @Test
    public void testToGeoJsonFeature() {
        String rawGeometry = "{\n" +
                "  \"type\": \"Point\",\n" +
                "  \"coordinates\": [125.6, 10.1]\n" +
                "}";
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode properties = objectMapper.createObjectNode();
        properties.put("value", "test");
        assertEquals(
                "{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[125.6,10.1]},\"properties\":{\"value\":\"test\"}}",
                JsonUtils.toGeoJsonFeature(rawGeometry, properties).toString()
        );
    }

    @Test
    public void testToGeoJsonFeatureBadGeometry() {
        String rawGeometry = "{xxx}";
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode properties = objectMapper.createObjectNode();
        properties.put("value", "test");
        try {
            JsonUtils.toGeoJsonFeature(rawGeometry, properties);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Bad Geojson geometry", e.getMessage());
        }
    }

    @Test
    public void testToGeoJsonFeatureCollection() {
        MockGeoJsonable geoJsonable = new MockGeoJsonable();
        assertEquals(
                "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[125.6,10.1]},\"properties\":{\"value\":\"test\"}}]}",
                JsonUtils.toGeoJsonFeatureCollection(Collections.singletonList(geoJsonable)).toString()
        );
    }

    @Test
    public void testSimpleMerge() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode mainNode = objectMapper.createObjectNode();
        ObjectNode secondNode = objectMapper.createObjectNode();
        mainNode.put("field1", "value1");
        secondNode.put("field2", "value2");
        JsonNode node = JsonUtils.deepMerge(mainNode, secondNode);
        Assertions.assertThat(node.get("field1").asText()).isEqualTo("value1");
        Assertions.assertThat(node.get("field2").asText()).isEqualTo("value2");
    }

    @Test
    public void testDeepMerge() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode mainNode = objectMapper.createObjectNode();
        ObjectNode secondNode = objectMapper.createObjectNode();
        mainNode.set("group1", objectMapper.createObjectNode().put("field1", "value1"));
        secondNode.set("group1", objectMapper.createObjectNode().put("field2", "value2"));
        JsonNode node = JsonUtils.deepMerge(mainNode, secondNode);
        Assertions.assertThat(node.get("group1").isObject()).isTrue();
        Assertions.assertThat(node.get("group1").get("field1").asText()).isEqualTo("value1");
        Assertions.assertThat(node.get("group1").get("field2").asText()).isEqualTo("value2");
    }

}
