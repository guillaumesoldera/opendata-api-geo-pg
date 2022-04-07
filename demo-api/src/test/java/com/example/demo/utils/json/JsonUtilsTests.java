package com.example.demo.utils.json;

import com.example.demo.mocks.MockJsonable;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonUtilsTests {

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

}
