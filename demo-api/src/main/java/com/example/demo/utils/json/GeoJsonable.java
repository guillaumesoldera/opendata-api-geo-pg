package com.example.demo.utils.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Interface permettant de définir un objet comme pouvant être sérialisé en GeoJSON.
 */
public interface GeoJsonable extends Jsonable {

    /**
     * Retourne les "properties" de l'objet GeoJSON.
     *
     * @param objectMapper l'ObjectMapper à ré-utiliser.
     * @return les "properties" de l'objet GeoJSON.
     */
    ObjectNode geoJsonProperties(ObjectMapper objectMapper);

    /**
     * Retourne la partie "geometry" (zone) de l'objet GeoJSON.
     *
     * @param objectMapper l'ObjectMapper à ré-utiliser.
     * @return la partie "geometry" (zone) de l'objet GeoJSON.
     */
    String geoJsonGeometry(ObjectMapper objectMapper);

    /**
     * Retourne l'objet courant sous forme d'un noeud GeoJSON de type {@link JsonNode}.
     *
     * @param objectMapper l'ObjectMapper à ré-utiliser.
     * @return l'objet courant sous forme d'un noeud GeoJSON.
     */
    default JsonNode toGeoJson(ObjectMapper objectMapper) {
        return JsonUtils.toGeoJsonFeature(geoJsonGeometry(objectMapper), geoJsonProperties(objectMapper));
    }

    /**
     * Retourne l'objet courant sous forme d'un noeud GeoJSON de type {@link JsonNode}.
     *
     * @return l'objet courant sous forme d'un noeud GeoJSON.
     */
    default JsonNode toGeoJson() {
        return toGeoJson(new ObjectMapper());
    }

    /**
     * {@inheritDoc}
     */
    default JsonNode toJson(ObjectMapper objectMapper) {
        return toGeoJson(objectMapper);
    }
}
