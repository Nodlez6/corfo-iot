package com.iot.project.com.iot.project.dto.wrapper;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

public class ServiceResponse<T> {

    private final Map<String, Map<String, T>> response;

    public ServiceResponse(String responseKey, ActionMethod actionMethod, T data) {
        this.response = Map.of(responseKey, Map.of(actionMethod.name(), data));
    }

    @JsonAnyGetter
    public Map<String, Map<String, T>> getResponse() {
        return response;
    }
}
