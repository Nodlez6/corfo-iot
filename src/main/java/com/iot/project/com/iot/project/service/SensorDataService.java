package com.iot.project.com.iot.project.service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.iot.project.com.iot.project.dto.sensorData.CreateSensorDataRequest;
import com.iot.project.com.iot.project.dto.sensorData.GetSensorDataRequest;
import com.iot.project.com.iot.project.entity.Sensor;
import com.iot.project.com.iot.project.entity.SensorDataDetail;
import com.iot.project.com.iot.project.entity.SensorDataHeader;
import com.iot.project.com.iot.project.entity.SensorMetric;
import com.iot.project.com.iot.project.exception.NotFoundException;
import com.iot.project.com.iot.project.repository.SensorDataDetailRepository;
import com.iot.project.com.iot.project.repository.SensorDataHeaderRepository;
import com.iot.project.com.iot.project.repository.SensorMetricRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.iot.project.com.iot.project.exception.ConstantsExceptions.INVALID_SENSOR_API_KEY;

@Service
@AllArgsConstructor
public class SensorDataService {
    private final SensorDataHeaderRepository sensorDataHeaderRepository;
    private final SensorService sensorService;
    private final SensorDataDetailRepository sensorDataDetailRepository;
    private final SensorMetricRepository sensorMetricRepository;

    public List<SensorDataHeader> getAllSensorData(GetSensorDataRequest request, Long companyId) {
        return sensorDataHeaderRepository.findBySensorIdsAndDateRangeAndCompanyApiKey(companyId, request.getSensorIds(), request.getFrom(), request.getTo());
    }

    public SensorDataHeader createSensorData(CreateSensorDataRequest request) {
        Sensor sensor = sensorService.getSensorBySensorApiKey(request.getApiKey())
                .orElseThrow(() -> new NotFoundException(INVALID_SENSOR_API_KEY));

        Set<SensorDataDetail> sensorDataDetailList = new HashSet<>();

        SensorDataHeader sensorDataHeader = SensorDataHeader.builder()
                .sensorId(sensor.getSensorId())
                .build();

        SensorDataHeader sensorDataHeaderResponse = sensorDataHeaderRepository.save(sensorDataHeader);

        request.getJsonData().forEach(reading -> {
            Instant readingTimestamp = Instant.parse(reading.getDatetime());

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

    public void deleteSensorData(Long sensorDataId, UUID sensorApiKey) {
        sensorService.getSensorBySensorApiKey(sensorApiKey)
                .orElseThrow(() -> new NotFoundException(INVALID_SENSOR_API_KEY));
        sensorDataHeaderRepository.deleteById(sensorDataId);
    }
}
