package com.iot.project.com.iot.project.service;

import java.time.Instant;
import java.util.List;

import com.iot.project.com.iot.project.entity.SensorData;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SensorDataService {
    //private final SensorDataRepository sensorDataRepository;
    private final SensorService sensorService;

    public String saveSensorData(){
        //long sensorId = sensorService.authenticateSensorApiKey( aqui iria el api key);
        //construir un objeto SensorData con la data y el id obtenido de la auth
        //sensorDataRepository.save( )
        return null;
    }

    public List<SensorData> getSensorData(Instant from, Instant to, List<Long> sensorIds) {
        // return List<SensorData> sensorDataList = sensorDataRepository.findByTimestampBetweenAndSensorIdIn(from, to, sensorIds);
        return null;
    }
}
