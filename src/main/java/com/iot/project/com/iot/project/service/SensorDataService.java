package com.iot.project.com.iot.project.service;

import java.time.Instant;
import java.util.ArrayList;
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
import org.springframework.transaction.annotation.Transactional;

import static com.iot.project.com.iot.project.exception.ConstantsExceptions.ENTITY_NOT_FOUND_BY_COMPANY;
import static com.iot.project.com.iot.project.exception.ConstantsExceptions.INVALID_SENSOR_API_KEY;
import static com.iot.project.com.iot.project.exception.ConstantsExceptions.RESOURCE_NOT_FOUND;

@Service
@AllArgsConstructor
public class SensorDataService {
    private final SensorDataHeaderRepository sensorDataHeaderRepository;
    private final SensorService sensorService;
    private final SensorDataDetailRepository sensorDataDetailRepository;
    private final SensorMetricRepository sensorMetricRepository;

    public List<SensorDataHeader> getAllSensorData(Long companyId) {
        return sensorDataHeaderRepository.findAllByCompanyId(companyId);
    }


    public List<SensorDataHeader> getAllSensorData(GetSensorDataRequest request, Long companyId) {
        Instant from = Instant.ofEpochSecond(Long.parseLong(request.getFrom()));
        Instant to = Instant.ofEpochSecond(Long.parseLong(request.getTo()));
        return sensorDataHeaderRepository.findBySensorIdsAndDateRangeAndCompanyApiKey(companyId, request.getSensorIds(), from, to);
    }
    @Transactional
    public SensorDataHeader createSensorData(CreateSensorDataRequest request) {
        Sensor sensor = sensorService.getSensorBySensorApiKey(request.getApiKey())
                .orElseThrow(() -> new NotFoundException(INVALID_SENSOR_API_KEY));

        Set<SensorDataDetail> sensorDataDetailList = new HashSet<>();

        SensorDataHeader sensorDataHeader = SensorDataHeader.builder()
                .sensorId(sensor.getSensorId())
                .build();

        SensorDataHeader sensorDataHeaderResponse = sensorDataHeaderRepository.save(sensorDataHeader);

        request.getJsonData().forEach(reading -> {
            Instant readingTimestamp = Instant.ofEpochSecond(Long.parseLong(reading.getDatetime()));

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

        sensorDataDetailRepository.saveAll(new ArrayList<>(sensorDataDetailList));
        sensorDataHeaderResponse.setDetails(sensorDataDetailList);
        return sensorDataHeaderResponse;
    }

    public SensorDataHeader updateSensorData(Long sensorDataId, CreateSensorDataRequest request, Long companyId) {
        SensorDataHeader existingHeader = sensorDataHeaderRepository.findById(sensorDataId)
                .orElseThrow(() -> new NotFoundException(RESOURCE_NOT_FOUND));

        sensorService.getSensorById(existingHeader.getSensorId(), companyId);

        sensorDataDetailRepository.deleteAll(existingHeader.getDetails());
        Set<SensorDataDetail> newDetails = new HashSet<>();

        request.getJsonData().forEach(reading -> {
            Instant readingTimestamp = Instant.ofEpochSecond(Long.parseLong(reading.getDatetime()));
            reading.getMetrics().forEach((metricName, metricValue) -> {
                SensorMetric sensorMetric = sensorMetricRepository.findByMetricName(metricName)
                        .orElseGet(() -> sensorMetricRepository.save(
                                SensorMetric.builder().metricName(metricName).build()
                        ));
                SensorDataDetail detail = SensorDataDetail.builder()
                        .sensorDataHeaderId(existingHeader.getId())
                        .metric(sensorMetric)
                        .value(metricValue)
                        .timestamp(readingTimestamp)
                        .build();
                newDetails.add(detail);
            });
        });

        sensorDataDetailRepository.saveAll(newDetails);
        existingHeader.setDetails(newDetails);
        return sensorDataHeaderRepository.save(existingHeader);
    }

    public void deleteSensorData(Long sensorDataId, Long companyId) {
        SensorDataHeader header = sensorDataHeaderRepository.findById(sensorDataId)
                .orElseThrow(() -> new NotFoundException(RESOURCE_NOT_FOUND));

        Sensor sensor = sensorService.getSensorById(header.getSensorId(), companyId);

        if(!sensor.getSensorId().equals(header.getSensorId())){
            throw new NotFoundException(ENTITY_NOT_FOUND_BY_COMPANY);
        }
        sensorDataHeaderRepository.deleteById(sensorDataId);
    }
}
