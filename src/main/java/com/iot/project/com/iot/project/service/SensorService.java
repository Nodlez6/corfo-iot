package com.iot.project.com.iot.project.service;

import java.util.List;

import com.iot.project.com.iot.project.entity.Company;
import com.iot.project.com.iot.project.entity.Location;
import com.iot.project.com.iot.project.entity.Sensor;
import com.iot.project.com.iot.project.exception.UnauthorizedException;
import org.springframework.stereotype.Service;

@Service
public class SensorService {
    //private final SensorRepository sensorRepository;

    public List<Sensor> getAllSensors() {
        //return sensorRepository.getLocations();
        return null;
    }

    public Sensor getSensorById(Long id) {
        //Sensor sensor = sensorRepository.findById(id);
//        if(!location){
//            throw NotFoundException("Location not found");
//        }
        return null;
    }

    public Sensor createSensor(Sensor sensor) {
        //return sensorRepository.save(sensor);
        return null;
    }

    public Sensor updateSensor(Long id, Sensor sensorDetails) {
//        Sensor sensor = sensorRepository.getById(id);
//        sensor.setName(sensorDetails.getName());
//        return sensorRepository.save(sensor);
        return null;
    }

    public boolean deleteSensor(Long id) {
        //Sensor sensor = sensorRepository.getById(id);
       // sensorRepository.delete(sensor);
        return true;
    }

    public long authenticateSensorApiKey(String sensorApiKey){
        //sensorRepository.find o algo asi,
       // if( si ese sensorapikey no existe para algun sensor ){
          //  throw new UnauthorizedException("Invalid sensor api key");
        //}
        //AQUI RETORNAR el id del sensor
        return 1;
    }
}
