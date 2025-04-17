package com.healink.integrador.core.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonDTO {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJsonString(JsonNode jsonNode) {
        try {
            return jsonNode == null ? null : objectMapper.writeValueAsString(jsonNode);
        } catch (Exception e) {
            throw new RuntimeException("Error al convertir JsonNode a String", e);
        }
    }

    public static JsonNode toJsonNode(String jsonString) {
        try {
            return jsonString == null || jsonString.isEmpty() ? null : objectMapper.readTree(jsonString);
        } catch (Exception e) {
            throw new RuntimeException("Error al convertir String a JsonNode", e);
        }
    }
}