package com.example.demo.models.data;

import com.example.demo.utils.json.Jsonable;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Optional;

public class DataProvider implements Jsonable {

    public final String id;
    public final String name;
    public final Optional<String> urlOpt;
    public final Optional<String> descriptionOpt;

    public DataProvider(String id, String name, Optional<String> urlOpt, Optional<String> descriptionOpt) {
        this.id = id;
        this.name = name;
        this.urlOpt = urlOpt;
        this.descriptionOpt = descriptionOpt;
    }

    @Override
    public JsonNode toJson(ObjectMapper objectMapper) {
        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode
                .put("id", id)
                .put("name", name);

        urlOpt.ifPresent(url -> jsonNode.put("url", url));
        descriptionOpt.ifPresent(description -> jsonNode.put("description", description));

        return jsonNode;
    }
}
