package com.example.demo.utils.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Interface permettant de définir un objet comme pouvant être sérialisé en JSON.
 */
public interface Jsonable {

    /**
     * Retourne l'objet courant sous forme d'un noeud JSON de type {@link JsonNode}.
     *
     * @return l'objet courant sous forme d'un noeud JSON.
     */
    default JsonNode toJson() {
        return toJson(new ObjectMapper());
    }

    /**
     * Retourne l'objet courant sous forme d'un noeud JSON de type {@link JsonNode}.
     *
     * @param objectMapper l'ObjectMapper à ré-utiliser.
     * @return l'objet courant sous forme d'un noeud JSON.
     */
    JsonNode toJson(ObjectMapper objectMapper);

    /**
     * Sérialise l'objet courant en JSON.
     *
     * @return le JSON de l'objet courant.
     */
    default String toJsonString() {
        return toJson().toString();
    }

}