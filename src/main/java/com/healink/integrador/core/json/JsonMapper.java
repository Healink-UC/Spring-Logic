package com.healink.integrador.core.json;

import com.fasterxml.jackson.databind.JsonNode;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JsonMapper {

    default String jsonNodeToString(JsonNode jsonNode) {
        return JsonDTO.toJsonString(jsonNode);
    }

    default JsonNode stringToJsonNode(String jsonString) {
        return JsonDTO.toJsonNode(jsonString);
    }
}