package com.example.demo.utils.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Ensemble d'utilitaires pour la manipulation de JSON.
 */
public final class JsonUtils {

    private JsonUtils() {
    }

    /**
     * Transforme une liste d'objets {@link com.example.demo.utils.json.Jsonable} en tableau JSON de type {@link ArrayNode}.
     *
     * @param jsonableElements la liste d'objets à transformer
     * @return le tableau JSON correspondant à la liste d'objets en entrée
     */
    public static ArrayNode toJsonArray(List<? extends Jsonable> jsonableElements) {
        return toJsonArray(jsonableElements, new ObjectMapper());
    }

    /**
     * Transforme une liste d'objets {@link com.example.demo.utils.json.Jsonable} en tableau JSON de type {@link ArrayNode}.
     *
     * @param jsonableElements la liste d'objets à transformer
     * @param objectMapper     l'ObjectMapper à réutiliser pour la création des noeuds JSON
     * @return le tableau JSON correspondant à la liste d'objets en entrée
     */
    public static ArrayNode toJsonArray(List<? extends Jsonable> jsonableElements, ObjectMapper objectMapper) {
        return toJsonArray(jsonableElements, objectMapper, element -> element.toJson(objectMapper));
    }

    /**
     * Transforme une liste d'objets en tableau JSON de type {@link ArrayNode}.
     *
     * @param objects      la liste d'objets.
     * @param objectMapper l'ObjectMapper à réutiliser.
     * @param jsonMapper   la fonction de transformation des objets en JSON.
     * @param <T>          le type des objets.
     * @return la liste des objets transformés en JSON.
     */
    public static <T extends Jsonable> ArrayNode toJsonArray(List<T> objects, ObjectMapper objectMapper, Function<T, JsonNode> jsonMapper) {
        ArrayNode arrayNode = objectMapper.createArrayNode();
        return arrayNode.addAll(objects.stream().map(jsonMapper).collect(Collectors.toList()));
    }

}