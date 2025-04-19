package com.iot.project.com.iot.project.dto.wrapper;

public enum ActionType {
    CREATED,          // Para la acción de creación
    UPDATED,          // Para la acción de actualización
    DELETED,          // Para la acción de eliminación
    GET_BY_ID,        // Para obtener una compañía por su ID
    GET_BY_APIKEY,    // Para obtener una compañía por su apiKey
    LIST_ALL,         // Para obtener una lista de compañías
    SEARCH            // Para buscar una compañía según otros criterios
}
