package com.example.demo.utils.json;

import com.example.demo.mocks.MockGeoJsonable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GeoJsonableTests {

    @Test
    public void testToJsonString() {
        MockGeoJsonable mock = new MockGeoJsonable();
        assertEquals(
                "{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[125.6,10.1]},\"properties\":{\"value\":\"test\"}}",
                mock.toJsonString()
        );
    }

    @Test
    public void testToGeoJson() {
        MockGeoJsonable mock = new MockGeoJsonable();
        assertEquals(
                "{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[125.6,10.1]},\"properties\":{\"value\":\"test\"}}",
                mock.toGeoJson().toString()
        );
    }

}
