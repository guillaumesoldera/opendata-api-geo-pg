package com.example.demo.models.data;

import com.example.demo.utils.json.Jsonable;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class DataSource implements Jsonable {

    public final String id;
    public final String name;
    public final Optional<String> urlOpt;
    public final Optional<String> descriptionOpt;
    public final Optional<LocalDate> publicationDateOpt;
    public final DataProvider dataProvider;

    public DataSource(
            String id,
            String name,
            Optional<String> urlOpt,
            Optional<String> descriptionOpt,
            Optional<LocalDate> publicationDateOpt,
            DataProvider dataProvider
    ) {
        this.id = id;
        this.name = name;
        this.urlOpt = urlOpt;
        this.descriptionOpt = descriptionOpt;
        this.publicationDateOpt = publicationDateOpt;
        this.dataProvider = dataProvider;
    }

    @Override
    public JsonNode toJson(ObjectMapper objectMapper) {
        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode
                .put("id", id)
                .put("name", name);

        urlOpt.ifPresent(url -> jsonNode.put("url", url));
        descriptionOpt.ifPresent(description -> jsonNode.put("description", description));
        publicationDateOpt.ifPresent(publicationDate -> jsonNode.put("publication_date", publicationDate.format(DateTimeFormatter.ISO_DATE)));

        jsonNode.set("provider", dataProvider.toJson(objectMapper));

        return jsonNode;
    }
}
