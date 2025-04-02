package com.iot.project.com.iot.project.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.iot.project.com.iot.project.dto.sensorDataHeader.CreateSensorDataRequest;
import com.iot.project.com.iot.project.entity.Sensor;
import com.iot.project.com.iot.project.entity.SensorDataDetail;
import com.iot.project.com.iot.project.entity.SensorDataHeader;
import com.iot.project.com.iot.project.entity.SensorMetric;
import com.iot.project.com.iot.project.exception.NotFoundException;
import com.iot.project.com.iot.project.repository.SensorDataDetailRepository;
import com.iot.project.com.iot.project.repository.SensorDataHeaderRepository;
import com.iot.project.com.iot.project.repository.SensorMetricRepository;
import com.iot.project.com.iot.project.repository.SensorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.iot.project.com.iot.project.exception.ConstantsExceptions.RESOURCE_NOT_FOUND;

@Service
@AllArgsConstructor
public class SensorDataService {
    private final SensorDataHeaderRepository sensorDataHeaderRepository;
    private final SensorService sensorService;
    private final SensorRepository sensorRepository;
    private final SensorMetricRepository sensorMetricRepository;
    private final SensorDataDetailRepository sensorDataDetailRepository;

    public String saveSensorData(){
        //long sensorId = sensorService.authenticateSensorApiKey( aqui iria el api key);
        //construir un objeto SensorData con la data y el id obtenido de la auth
        //sensorDataRepository.save( )
        return null;
    }

//    public List<SensorData> getAllSensorData(GetSensorDataRequest request, Long companyId) {
//        return sensorDataRepository.findSensorDataBySensorIdsAndTimestampBetweenAndCompanyId(request.getSensorIds(), request.getTo(), request.getFrom(), companyId);
//    }

    public SensorDataHeader createSensorData(CreateSensorDataRequest request) {
        Sensor sensor = sensorService.getSensorBySensorApiKey(request.getApiKey())
                .orElseThrow(() -> new NotFoundException(RESOURCE_NOT_FOUND));

        Set<SensorDataDetail> sensorDataDetailList = new HashSet<>();

        SensorDataHeader sensorDataHeader = SensorDataHeader.builder()
                .sensorId(sensor.getSensorId())
                .build();

        SensorDataHeader sensorDataHeaderResponse = sensorDataHeaderRepository.save(sensorDataHeader);

        request.getJsonData().forEach(reading -> {
            Instant readingTimestamp = reading.getDatetime();

            reading.getMetrics().forEach((metricName, metricValue) -> {
                SensorMetric sensorMetric = sensorMetricRepository.findByMetricName(metricName)
                        .orElseGet(() -> sensorMetricRepository.save(
                                SensorMetric.builder()
                                        .metricName(metricName)
                                        .build()
                        ));

                SensorDataDetail sensorDataDetail = SensorDataDetail.builder()
                        .sensorDataHeaderId(sensorDataHeaderResponse.getId())
                        .metric(sensorMetric)
                        .value(metricValue)
                        .timestamp(readingTimestamp)
                        .build();
                sensorDataDetailList.add(sensorDataDetail);
            });
        });

        sensorDataDetailRepository.saveAll(sensorDataDetailList);
        sensorDataHeader.setDetails(sensorDataDetailList);
        return sensorDataHeader;
    }
}
