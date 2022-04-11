package com.example.demo.utils.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Ensemble d'utilitaires pour la manipulation de JSON.
 */
public final class JsonUtils {

    private JsonUtils() {
    }

    /**
     * Retourne un Optional<JsonNode> à partir d'un JsonNode, en vérifiant que le noeud n'est pas null et n'est pas un NullNode.
     *
     * @param node le noeud.
     * @return le noeud wrappé dans un Optional.
     */
    public static Optional<JsonNode> ofNullableNode(JsonNode node) {
        return Optional.ofNullable(node).filter(n -> !n.isNull());
    }

    /**
     * Transforme une liste d'objets {@link com.example.demo.utils.json.Jsonable} en tableau JSON de type {@link com.fasterxml.jackson.databind.node.ArrayNode}.
     *
     * @param jsonableElements la liste d'objets à transformer
     * @return le tableau JSON correspondant à la liste d'objets en entrée
     */
    public static ArrayNode toJsonArray(List<? extends Jsonable> jsonableElements) {
        return toJsonArray(jsonableElements, new ObjectMapper());
    }

    /**
     * Transforme une liste d'objets {@link com.example.demo.utils.json.Jsonable} en tableau JSON de type {@link com.fasterxml.jackson.databind.node.ArrayNode}.
     *
     * @param jsonableElements la liste d'objets à transformer
     * @param objectMapper     l'ObjectMapper à réutiliser pour la création des noeuds JSON
     * @return le tableau JSON correspondant à la liste d'objets en entrée
     */
    public static ArrayNode toJsonArray(List<? extends Jsonable> jsonableElements, ObjectMapper objectMapper) {
        return toJsonArray(jsonableElements, objectMapper, element -> element.toJson(objectMapper));
    }

    /**
     * Transforme une liste d'objets en tableau JSON de type {@link com.fasterxml.jackson.databind.node.ArrayNode}.
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

    /**
     * Transforme une liste de chaînes de caractères en tableau JSON de type {@link com.fasterxml.jackson.databind.node.ArrayNode}.
     *
     * @param strings la liste de chaînes de caractères
     * @return le tableau JSON correspondant à la liste de chaînes de caractères en entrée
     */
    public static ArrayNode toJsonStringArray(List<String> strings) {
        return toJsonStringArray(strings, new ObjectMapper());
    }

    /**
     * Transforme une liste de chaînes de caractères en tableau JSON de type {@link com.fasterxml.jackson.databind.node.ArrayNode}.
     *
     * @param strings      la liste de chaînes de caractères
     * @param objectMapper l'ObjectMapper à réutiliser pour la création des noeuds JSON
     * @return le tableau JSON correspondant à la liste de chaînes de caractères en entrée
     */
    public static ArrayNode toJsonStringArray(List<String> strings, ObjectMapper objectMapper) {
        ArrayNode arrayNode = objectMapper.createArrayNode();
        arrayNode.addAll(strings.stream().map(TextNode::valueOf).collect(Collectors.toList()));
        return arrayNode;
    }

    /**
     * Créé une Feature GeoJSON à partir d'une "geometry" (zone) et des "properties" associées.
     *
     * @param rawGeoJson la "geometry" (zone).
     * @param properties les properties.
     * @return la Feature GeoJSON.
     */
    public static JsonNode toGeoJsonFeature(String rawGeoJson, ObjectNode properties) {
        return toGeoJsonFeature(rawGeoJson, properties, new ObjectMapper());
    }

    /**
     * Créé une Feature GeoJSON à partir d'une "geometry" (zone) et des "properties" associées.
     *
     * @param rawGeoJson   la "geometry" (zone).
     * @param properties   les properties.
     * @param objectMapper l'ObjectMapper à réutiliser.
     * @return la Feature GeoJSON.
     */
    public static JsonNode toGeoJsonFeature(String rawGeoJson, ObjectNode properties, ObjectMapper objectMapper) {
        ObjectNode node = objectMapper.createObjectNode();
        try {
            return node
                    .put("type", "Feature")
                    .<ObjectNode>set("geometry", objectMapper.readTree(rawGeoJson))
                    .set("properties", properties);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Bad Geojson geometry");
        }
    }

    /**
     * Créé une FeatureCollection GeoJSON à partir d'une liste de Features GeoJSON.
     *
     * @param geoJsonFeatures la liste des Features GeoJSON.
     * @return la FeatureCollection GeoJSON.
     */
    public static JsonNode toGeoJsonFeatureCollection(List<? extends GeoJsonable> geoJsonFeatures) {
        return toGeoJsonFeatureCollection(geoJsonFeatures, new ObjectMapper());
    }

    /**
     * Créé une FeatureCollection GeoJSON à partir d'une liste de Features GeoJSON.
     *
     * @param geoJsonFeatures la liste des Features GeoJSON.
     * @param objectMapper    l'ObjectMapper à réutiliser.
     * @return la FeatureCollection GeoJSON.
     */
    public static JsonNode toGeoJsonFeatureCollection(List<? extends GeoJsonable> geoJsonFeatures, ObjectMapper objectMapper) {
        ObjectNode node = objectMapper.createObjectNode();
        node
                .put("type", "FeatureCollection")
                .set("features", JsonUtils.toJsonArray(geoJsonFeatures, objectMapper, element -> element.toGeoJson(objectMapper)));
        return node;
    }

    /**
     * Fusionne un objet JSON dans un autre objet JSON.
     *
     * @param mainNode   l'objet principal dans lequel fusionner l'objet secondaire.
     * @param updateNode l'objet secondaire, à fusionner dans l'objet principal.
     * @return le noeud principal.
     */
    public static JsonNode deepMerge(JsonNode mainNode, JsonNode updateNode) {
        Iterator<Map.Entry<String, JsonNode>> fieldNames = updateNode.fields();
        fieldNames.forEachRemaining(current -> {
            String fieldName = current.getKey();
            JsonNode currentValue = current.getValue();
            JsonNode jsonNode = mainNode.get(fieldName);
            // if field exists and is an embedded object
            if (jsonNode != null && jsonNode.isObject()) {
                deepMerge(jsonNode, currentValue);
            } else {
                if (mainNode instanceof ObjectNode) {
                    // Overwrite field
                    ((ObjectNode) mainNode).set(fieldName, currentValue);
                }
            }
        });

        return mainNode;
    }

}