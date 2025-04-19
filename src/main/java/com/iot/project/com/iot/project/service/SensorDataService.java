package com.iot.project.com.iot.project.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
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

import jakarta.persistence.EntityNotFoundException;
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


    public List<SensorDataHeader> getAllSensorDataByDateTimeRange(GetSensorDataRequest request, Long companyId) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        LocalDateTime fromDateTime = LocalDateTime.parse(request.getFrom(), formatter);
        LocalDateTime toDateTime = LocalDateTime.parse(request.getTo(), formatter);

        // Convertir a Instant usando ZoneOffset si es necesario
        Instant from = fromDateTime.toInstant(ZoneOffset.UTC);
        Instant to = toDateTime.toInstant(ZoneOffset.UTC);

        return sensorDataHeaderRepository.findBySensorIdsAndDateRangeAndCompanyApiKey(companyId, request.getSensorIds(), from, to);
    }




    @Transactional
    public SensorDataHeader createSensorData(CreateSensorDataRequest request) {
        
        Sensor sensor = sensorService.getSensorBySensorApiKey(request.getApiKey());

        Set<SensorDataDetail> sensorDataDetailList = new HashSet<>();

        SensorDataHeader sensorDataHeader = SensorDataHeader.builder()
                .sensorId(sensor.getSensorId())
                .build();

        SensorDataHeader sensorDataHeaderResponse = sensorDataHeaderRepository.save(sensorDataHeader);

        request.getJsonData().forEach(reading -> {
            Instant readingTimestamp = Instant.ofEpochSecond(Long.parseLong(reading.getDatetime()));
    
            reading.getMetrics().forEach((metricName, metricValue) -> {
                String valueAsString = String.valueOf(metricValue);
    
                SensorMetric sensorMetric = sensorMetricRepository.findByMetricName(metricName)
                        .orElseGet(() -> sensorMetricRepository.save(
                                SensorMetric.builder()
                                        .metricName(metricName)
                                        .build()
                        ));
    
                SensorDataDetail sensorDataDetail = SensorDataDetail.builder()
                        .sensorDataHeaderId(sensorDataHeaderResponse.getId())
                        .metric(sensorMetric)
                        .value(valueAsString)  
                        .timestamp(readingTimestamp)
                        .build();
    
                sensorDataDetailList.add(sensorDataDetail);
            });
        });

        sensorDataDetailRepository.saveAll(new ArrayList<>(sensorDataDetailList));
        sensorDataHeaderResponse.setDetails(sensorDataDetailList);
        return sensorDataHeaderResponse;
    }


    @Transactional
    public int deleteSensorDataByDateTimeRange(GetSensorDataRequest request, Long companyId) {
        System.out.println("Método deleteSensorDataByDateTimeRange iniciado");
    
        // Formato de fecha esperado
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
        // Parseo de las fechas
        LocalDateTime fromDateTime = LocalDateTime.parse(request.getFrom(), formatter);
        LocalDateTime toDateTime = LocalDateTime.parse(request.getTo(), formatter);
    
        // Conversión de las fechas a Instant
        Instant from = fromDateTime.toInstant(ZoneOffset.UTC);
        Instant to = toDateTime.toInstant(ZoneOffset.UTC);
    
        System.out.println("Rango de fechas: de " + from + " a " + to);
    
        // Verificar qué registros existen en la base de datos para esas fechas
        List<SensorDataHeader> allRecordsInRange = sensorDataHeaderRepository.findAllByTimestampBetween(from, to);
        System.out.println("Registros encontrados en la base de datos entre las fechas: " + allRecordsInRange.size());
        allRecordsInRange.forEach(record -> System.out.println("Registro: " + record.getTimestamp()));
    
        // Obtener los IDs de las cabeceras que coincidan
        List<Long> headerIds = sensorDataHeaderRepository.findIdsBySensorIdsAndDateRangeAndCompanyApiKey(
                companyId, request.getSensorIds(), from, to
        );
        System.out.println("IDs encontrados para eliminación: " + headerIds);
    
        if (headerIds.isEmpty()) {
            System.out.println("No se encontraron registros para eliminar.");
            return 0;
        }
    
        // Eliminar detalles primero (si no tienes cascade)
        sensorDataDetailRepository.deleteBySensorDataHeaderIdIn(headerIds);
        System.out.println("Detalles eliminados.");
    
        // Eliminar cabeceras
        sensorDataHeaderRepository.deleteByIdIn(headerIds);
        System.out.println("Cabeceras eliminadas.");
    
        return headerIds.size();
    }

}



    // public SensorDataHeader updateSensorData(Long sensorDataId, CreateSensorDataRequest request, Long companyId) {
    //     SensorDataHeader existingHeader = sensorDataHeaderRepository.findById(sensorDataId)
    //             .orElseThrow(() -> new NotFoundException(RESOURCE_NOT_FOUND));

    //     sensorService.getSensorById(existingHeader.getSensorId(), companyId);

    //     sensorDataDetailRepository.deleteAll(existingHeader.getDetails());
    //     Set<SensorDataDetail> newDetails = new HashSet<>();

    //     request.getJsonData().forEach(reading -> {
    //         Instant readingTimestamp = Instant.ofEpochSecond(Long.parseLong(reading.getDatetime()));
    //         reading.getMetrics().forEach((metricName, metricValue) -> {
    //             String valueAsString = String.valueOf(metricValue);

    //             SensorMetric sensorMetric = sensorMetricRepository.findByMetricName(metricName)
    //                     .orElseGet(() -> sensorMetricRepository.save(
    //                             SensorMetric.builder().metricName(metricName).build()
    //                     ));
    //             SensorDataDetail detail = SensorDataDetail.builder()
    //                     .sensorDataHeaderId(existingHeader.getId())
    //                     .metric(sensorMetric)
    //                     .value(valueAsString)
    //                     .timestamp(readingTimestamp)
    //                     .build();
    //             newDetails.add(detail);
    //         });
    //     });

    //     sensorDataDetailRepository.saveAll(newDetails);
    //     existingHeader.setDetails(newDetails);
    //     return sensorDataHeaderRepository.save(existingHeader);
    // }