package com.example.demo.controllers;

import com.example.demo.mocks.MockController;
import com.example.demo.mocks.MockJsonable;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BaseControllerTests {

    @Test
    public void testNotFound() {
        MockController mockController = new MockController();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String badRequestPayload = mockController.notFound(response);
        assertEquals("{ \"message\": \"Not Found\" }", badRequestPayload);
        assertEquals(404, response.getStatus());
    }

    @Test
    public void testProcessOptionalStringWithEmpty() {
        MockController mockController = new MockController();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String payload = mockController.processOptionalString(Optional.empty(), response);
        assertEquals("{ \"message\": \"Not Found\" }", payload);
        assertEquals(404, response.getStatus());
    }

    @Test
    public void testProcessOptionalString() {
        MockController mockController = new MockController();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String payload = mockController.processOptionalString(Optional.of("Hello world"), response);
        assertEquals("Hello world", payload);
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testProcessOptionalJsonableWithEmpty() {
        MockController mockController = new MockController();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String payload = mockController.processOptionalJsonable(Optional.empty(), response);
        assertEquals("{ \"message\": \"Not Found\" }", payload);
        assertEquals(404, response.getStatus());
    }

    @Test
    public void testProcessOptionalJsonable() {
        MockController mockController = new MockController();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String payload = mockController.processOptionalJsonable(Optional.of(new MockJsonable("123", "name1")), response);
        assertEquals("{\"id\":\"123\",\"name\":\"name1\"}", payload);
        assertEquals(200, response.getStatus());
    }

}
