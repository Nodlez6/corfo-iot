package com.iot.project.com.iot.project.dto.wrapper;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;



public class ServiceResponse<T> {

    private final Map<String, Map<String, T>> response;

    public ServiceResponse(String responseKey, ActionMethod actionMethod, T data) {
        // Usamos el valor dinámico de responseKey y el nombre del enum para construir la respuesta
        this.response = Map.of(responseKey, Map.of(actionMethod.name(), data));
    }

    @JsonAnyGetter
    public Map<String, Map<String, T>> getResponse() {
        return response;
    }












    // private final Map<String, T> response;

    // public ServiceResponse(String wrapperKey, T data) {
    //     if (wrapperKey == null || wrapperKey.isEmpty()) {
    //         wrapperKey = "DEFAULT_KEY";
    //     }
    //     this.response = Map.of(wrapperKey, data);
    // }

    // @JsonAnyGetter
    // public Map<String, T> getResponse() {
    //     return response;
    // }
}
